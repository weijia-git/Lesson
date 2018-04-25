package com.example.pc.lesson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseActivity extends AppCompatActivity {
    private Spinner semSp;
    private List<String> semList = new ArrayList<String>();
    private ArrayAdapter<String> semSpAdapter;
    Elements semester;
    Map<String, String> sMap = new HashMap<String, String>();
    private String sValue = "";


    private Spinner lessonSp;
    private List<String> lessonList = new ArrayList<String>();
    private ArrayAdapter<String> lessonSpAdapter;
    Elements lesson;
    Map<String,String> lMap = new HashMap<String,String>();
    private EditText lessonEt;
    private String lValue = "";

    private String yzm = "";
    private ImageView yzmImage;
    private EditText yzmEt;
    private String yzmPath = "//data/data/com.example.pc.lesson/files/";

    static String htmlurl = "http://qg.peizheng.net.cn/ZNPK/KBFB_LessonSel.aspx";
    static String cookie = "";


    private TextView titleTv;
    private TextView XQXNTv;
    private TextView unitsTv;
    private TextView lessonnameTv;
    private TextView learningperiodTv;
    private TextView creditsTv;
    private List<Lessoninfo> lessoninfos = new ArrayList<Lessoninfo>();
    private ListView lessonLv;
    private LessoninfoAdapter lessoninfoAdapter;

    private MyOpenHelper helper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        semSp = (Spinner) findViewById(R.id.semester_Sp);
        semSpAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
        semSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSp.setAdapter(semSpAdapter);

        lessonSp = (Spinner) findViewById(R.id.lesson_Sp);
        lessonSpAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lessonList);
        lessonSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lessonSp.setAdapter(lessonSpAdapter);
        lessonEt = (EditText) findViewById(R.id.lesson_editView);

        yzmImage = (ImageView) findViewById(R.id.yzm_Image);
        yzmEt = (EditText) findViewById(R.id.yzm_Et);

        lessonEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String lessonEtText = lessonEt.getText().toString();
                lessonList.clear();

                for (String lname:lMap.keySet()) {
                    if(lname.contains(lessonEtText)){
                        lessonList.add(lname);
                    }
                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lessonSpAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        yzmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("john", "------------yzm onclick-----------");
                new Thread() {
                    @Override
                    public void run() {

                        getPic();
                        CourseActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bm = BitmapFactory.decodeFile(yzmPath + "p1.jpg");
                                yzmImage.setImageBitmap(bm);
                                Log.i("john", "------------yzm change-----------");
                            }
                        });

                    }
                }.start();

            }
        });

        preview();//初始化搜索界面

        titleTv = (TextView) findViewById(R.id.title_txetView);
        XQXNTv = (TextView) findViewById(R.id.XNXQ_textView);
        unitsTv = (TextView) findViewById(R.id.units_textView);
        lessonnameTv = (TextView) findViewById(R.id.lessonname_textView);
        learningperiodTv = (TextView) findViewById(R.id.learningperiod_textView);
        creditsTv = (TextView) findViewById(R.id.credit_textView);

        lessonLv = (ListView) findViewById(R.id.lesson_listView);
        lessoninfoAdapter = new LessoninfoAdapter(this,R.layout.lessoninfo_layout,lessoninfos);
        lessonLv.setAdapter(lessoninfoAdapter);

    }

    public void preview() {


        new Thread() {
            @Override
            public void run() {

                //-set semester spinner----
                semester = getSemesterOptions();
                for (Element sem : semester) {
                    semList.add(sem.text());

                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        semSpAdapter.notifyDataSetChanged();
                    }
                });

                //set lesson spinner
                lesson = getLessonsOptions(lMap);
                for (Element les : lesson ) {
                    lessonList.add(les.text());

                }
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lessonSpAdapter.notifyDataSetChanged();
                    }
                });

                //-----set yzmImage-------------
                getPic();
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(yzmPath + "p1.jpg");
                        yzmImage.setImageBitmap(bm);
                    }
                });





            }
        }.start();

    }

    public Elements getSemesterOptions() {
        URL url;
        Elements options = null;
        try {
            url = new URL("http://qg.peizheng.net.cn/ZNPK/TeacherKBFB.aspx");
            String html = doGet(url);
            Document doc = Jsoup.parse(html);
            options = doc.getElementsByTag("option");
            for (Element option : options) {
                sMap.put(option.text(), option.attr("value"));
//                Log.i("john",option.attr("value"));
//                Log.i("john",option.text());
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return options;
    }

//    计算机--------------------------------
    public static Elements getLessonsOptions(Map<String,String> lessonsMap) {
        URL url;
        Elements options = null;
        try {
            String name = URLEncoder.encode("计算机", "GBK");
            String urlstr = "http://qg.peizheng.net.cn/ZNPK/Private/List_XNXQKC.aspx?xnxq=20161&kc="+name;
            url = new URL(urlstr);
            String html = doGet(url);
            Document doc = Jsoup.parse(html);
            Elements scriptstr = doc.getElementsByTag("script");
            doc = Jsoup.parse(scriptstr.toString().substring(53,scriptstr.toString().length() - 10));
            options = doc.getElementsByTag("option");
            for (Element option : options) {
                if(option.attr("value") == ""){
                    continue;
                }
                lessonsMap.put(option.text(),option.attr("value"));
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return options;
    }

    public void getPic() {

        try {
            URL url = new URL("http://qg.peizheng.net.cn/sys/ValidateCode.aspx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Referer", "http://qg.peizheng.net.cn/ZNPK/TeacherKBFB.aspx");
            InputStream in = conn.getInputStream();
            FileOutputStream fo = openFileOutput("p1.jpg", MODE_PRIVATE);
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
            in.close();
            fo.close();
            conn.disconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String doGet(URL url) {
        String html = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();
            cookie = map.get("Set-Cookie").get(0);
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gbk"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            html = builder.toString();
            reader.close();
            conn.disconnect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html;
    }

    public static List<Lessoninfo> tolessonlist(String lessons){
//		lessons = "广东培正学院课程课表 2016-2017学年第二学期 承担单位：计算机科学与工程系??课程：[BCim1300]计算机组成原理??总学时：54??学分：3.0 任课教师 上课 班号 上课 人数 课程类别 考核 方式 上课班级构成 周次 节次 地点 魏文芬 003 42 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 四[1-3节] C305-图形图像制作实训室 魏文芬 004 59 专业课/必修课 考试 15本网络工程2班 15本网络工程1班 15本计算机科学与技术2班 15本计算机科学与技术1班 1-3 五[1-3节] C305-图形图像制作实训室 4 五[1-3节] C305-图形图像制作实训室 5-16 五[1-3节] C305-图形图像制作实训室 龚健虎 001 63 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 一[1-3节] 3611 龚健虎 002 53 专业课/必修课 考试 15本网络工程2班 15本网络工程1班 15本计算机科学与技术2班 15本计算机科学与技术1班 1-16 一[5-7节] 3611 广东培正学院课程课表 2016-2017学年第二学期 承担单位：计算机科学与工程系??课程：[BCim1300]计算机组成原理??总学时：54??学分：3.0 任课教师 上课 班号 上课 人数 课程类别 考核 方式 上课班级构成 周次 节次 地点 魏文芬 003 42 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 四[1-3节] C305-图形图像制作实训室 魏文芬 004 59 专业课/必修课 考试 15本网络工程2班 15本网络工程1班 15本计算机科学与技术2班 15本计算机科学与技术1班 1-3 五[1-3节] C305-图形图像制作实训室 4 五[1-3节] C305-图形图像制作实训室 5-16 五[1-3节] C305-图形图像制作实训室 龚健虎 001 63 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 一[1-3节] 3611 龚健虎 002 53 专业课/必修课 考试 15本网络工程2班 15本网络工程1班 15本计算机科学与技术2班 15本计算机科学与技术1班 1-16 一[5-7节] 3611";
        int indexzhu = lessons.length()/2;
        lessons = lessons.substring(0,indexzhu);
        indexzhu = lessons.indexOf("地点");
        lessons = lessons.substring(indexzhu+2);
        List<Lessoninfo> list = new ArrayList<Lessoninfo>();
        int index = 0;
        int indexend = 0;
        String temp;
        while(lessons.length() != 0){
            index = lessons.indexOf("课");
            indexend = lessons.indexOf("]");
            temp = lessons.substring(index-13,indexend);
            lessons = lessons.substring(indexend);
            index = lessons.indexOf("]");
            indexend = lessons.indexOf("课");
            if(lessons.contains("课") == false) {
                temp = temp + lessons.substring(index);
            }else{
                temp = temp + lessons.substring(index,indexend-14);
            }
//            System.out.println(temp);
            Lessoninfo l = tolesson(temp);
            list.add(l);
            if(lessons.length() < 50) {
                break;
            }else{
                lessons = lessons.substring(indexend-13);
            }
            if(lessons.contains("课") == false) {
                break;
            }
//            System.out.println(lessons);
        }
        return list;
    }

    public static Lessoninfo tolesson(String s){
//		temp = "魏文芬 003 42 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 四[1-3节] C305-图形图像制作实训室";
//		temp = "龚健虎 001 63 专业课/必修课 考试 15本计算机科学与技术1班 15本计算机科学与技术2班 15本网络工程1班 15本网络工程2班 1-16 一[1-3节] 3611 ";
//		temp = "魏文芬 004 59 专业课/必修课 考试 15本网络工程2班 15本网络工程1班 15本计算机科学与技术2班 15本计算机科学与技术1班 1-3 五[1-3节] C305-图形图像制作实训室 4 五[1-3节] C305-图形图像制作实训室 5-16 五[1-3节] C305-图形图像制作实训室 ";
        int beginIndex = 0;
        int endIndex = 0;
        String teachername=s.substring(0,4);
        String classnum=s.substring(4,8);
        String studentnum=s.substring(8,11);
        String subject=s.substring(11,19);
        String test=s.substring(19,22);
        endIndex = s.indexOf("-")-1;
        String classes=s.substring(22,endIndex);
        beginIndex = s.indexOf("班 1-")+1;
        endIndex = s.indexOf("班 1-")+14;
        String courseperiod=s.substring(beginIndex,endIndex);
        beginIndex = s.lastIndexOf("节]")+2;
        String place=s.substring(beginIndex);
        Lessoninfo l=new Lessoninfo(teachername,classnum,studentnum,subject,test,classes,courseperiod,place);
        return l;
    }

//query insert-------------
    public String getLessons(){
        String lessons = "";
        try {

            CourseActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("john", "------------set text primary----------");
                    titleTv.setText("");
                    XQXNTv.setText("");
                    unitsTv.setText("");
                    lessonnameTv.setText("");
                    learningperiodTv.setText("");
                    creditsTv.setText("");
                }
            });

            yzm = yzmEt.getText().toString();
            sValue = sMap.get(semSp.getSelectedItem().toString());
            lValue = lMap.get(lessonSp.getSelectedItem().toString());
            String html = "";
            URL url = new URL(htmlurl);

//            final Lesson lesson = queryLesson(lValue);----------------------------------------
//            数据库中查不到，sendpost
//            if (lesson == null) {
                Log.i("john", "------------data on net----------");
//                html = SendPost(sValue, lValue, yzm, cookie);-------------------------------

                html = SendPost("20161","280051", yzm, cookie);
                Log.i("john", "------------sendpost----------" + sValue + lValue);
                Log.i("john", "------------sendpost get html----------" + html);

                //验证码错误
                if (html.indexOf("验证码错误") != -1) {
                    Log.i("john", "------------yzm error----------");
                    CourseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    getPic();
                    CourseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bm = BitmapFactory.decodeFile(yzmPath + "p1.jpg");
                            yzmImage.setImageBitmap(bm);
                            Log.i("john", "------------yzm change-----------");
                        }
                    });
                }

                Document doc = Jsoup.parse(html);
                Log.i("john", "------------jsoup parse----------");
                Elements tables = doc.getElementsByTag("table");
                lessons = tables.text();
            Log.i("john",lessons);

                if(lessons.length()==0){
                    Log.i("john", "------------no lesson----------");
                    CourseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "这个老师没有课0_o", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                String lessonInfo = tables.tagName("table").tagName("table").get(2).text();
                Log.i("john", "lessonInfo :" + lessonInfo);

                final Lesson l = toLessoninfo(lessonInfo,lessons);
                Log.i("john", "------------parse teacher----------");

//                insertLessons(l);------------------------
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("john", "------------set text----------");
                        titleTv.setText("广东培正学院教师课表");
                        XQXNTv.setText(semSp.getSelectedItem().toString());
                        unitsTv.setText("承担单位："+l.getUnits());
                        lessonnameTv.setText("课程："+l.getLessonname());
                        learningperiodTv.setText("总学时："+l.getLearningperiod());
                        creditsTv.setText("学分："+l.getCredit());
                    }
                });
//            }
            //数据库中查到，直接输出
//            else{
//                CourseActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("john", "------------set text----------");
//                        titleTv.setText("广东培正学院教师课表");
//                        XQXNTv.setText(semSp.getSelectedItem().toString());
//                        unitsTv.setText("承担单位："+l.getUnits());
//                        lessonnameTv.setText("课程"+l.getLessonname());
//                        learningperiodTv.setText("总学时"+l.getLearningperiod());
//                        creditsTv.setText("学分"+l.getCredit());
//                    }
//                });
//                lessons = lesson.getLessons();
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lessons;
    }

    public static Lesson toLessoninfo(String lessoninfo,String lessons){
//		lessoninfo = "承担单位：计算机科学与工程系??课程：[BCim1300]计算机组成原理??总学时：54??学分：3.0";
        int index1 = lessoninfo.indexOf("系");
        int index2 = lessoninfo.indexOf("课程");
        lessoninfo = lessoninfo.substring(0,index1+1) + lessoninfo.substring(index2);
        System.out.println(lessoninfo);

        index1 = lessoninfo.indexOf("总学时");
        lessoninfo = lessoninfo.substring(0,index1-2) + lessoninfo.substring(index1);

        index1 = lessoninfo.indexOf("学分");
        lessoninfo = lessoninfo.substring(0,index1-2) + lessoninfo.substring(index1);

//		System.out.println(lessoninfo);

//		承担单位：计算机科学与工程系课程：[BCim1300]计算机组成原理总学时：54学分：3.0
        int index = lessoninfo.indexOf("课程");
        int indexend = lessoninfo.indexOf("总学时");
        String lname = lessoninfo.substring(index+3,indexend);
        index = lessoninfo.indexOf("总学时");
        indexend = lessoninfo.indexOf("学分");
        String learningperiod = lessoninfo.substring(index+4,indexend);
        index = lessoninfo.indexOf("学分");
        String credit = lessoninfo.substring(index+3);
        index = lessoninfo.indexOf("承担单位");
        indexend = lessoninfo.indexOf("课程");
        String units = lessoninfo.substring(index+5,indexend);
        Lesson lesson = new Lesson();
        lesson.setLessonname(lname);
        lesson.setCredit(credit);
        lesson.setLearningperiod(learningperiod);
        lesson.setUnits(units);
        lesson.setLessons(lessons);
        return lesson;
    }

    private static String SendPost(String termId,String tid,String yzm,String cookie) {
        String html = "";
        try {
            URL url = new URL("http://qg.peizheng.net.cn/ZNPK/KBFB_LessonSel_rpt.aspx");
            String param  = "Sel_XNXQ="+termId+"&Sel_KC="+tid+"&type="+"1"+"&txt_yzm="+yzm;
            //Connection con = Jsoup.connect("http://jw.zzti.edu.cn/ZNPK/TeacherKBFB_rpt.aspx");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Referer", htmlurl);
            conn.setRequestProperty("Origin", "http://qg.peizheng.net.cn");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(param);
            dos.flush();
            dos.close();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            html = builder.toString();
//			System.out.println(html);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html;
    }

    public void lessonSearch(View view) {
        new Thread(){
            @Override
            public void run() {
                List<Lessoninfo> ls = tolessonlist(getLessons());
                lessoninfos.clear();
                lessoninfos.addAll(ls);
                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lessoninfoAdapter.notifyDataSetChanged();

                    }
                });
            }
        }.start();
    }
}
