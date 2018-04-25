package com.example.pc.lesson;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import static android.R.attr.id;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {
    private Spinner semSp;
    private Spinner teacherSp;
    private List<String> semList = new ArrayList<String>();
    private List<String> teacherList = new ArrayList<String>();
    private ArrayAdapter<String> semSpAdapter;
    private ArrayAdapter<String> teacherSpAdapter;

    private EditText teacherEt;
    private ImageView yzmImage;
    private EditText yzmEt;

    Elements semester;
    Elements teacher;
    Map<String, String> sMap = new HashMap<String, String>();
    static Map<String, String> tMap = new HashMap<String, String>();

    static String cookie = "";
    private String sValue = "";
    private String tValue = "";
    private String yzm = "";


    private String lessons = "";
    private String yzmPath = "//data/data/com.example.pc.lesson/files/";

    static Map<String, String> teachersMap = new HashMap<String, String>();
    private TextView titleTv;
    private TextView XQXNTv;
    private TextView depTv;
    private TextView nameTv;
    private TextView sexTv;
    private TextView protitleTv;
    private ListView courseLv;
    private CourseAdapter courseAdapter;
    private List<Courseinfo> courses = new ArrayList<Courseinfo>();

    private MyOpenHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        semSp = (Spinner) findViewById(R.id.semester_Sp);
        semSpAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
        semSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSp.setAdapter(semSpAdapter);

        teacherSp = (Spinner) findViewById(R.id.teacher_Sp);
        teacherSpAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teacherList);
        teacherSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacherSp.setAdapter(teacherSpAdapter);
        teacherEt = (EditText) findViewById(R.id.teacher_editView);

        yzmImage = (ImageView) findViewById(R.id.yzm_Image);
        yzmEt = (EditText) findViewById(R.id.yzm_Et);

        titleTv = (TextView) findViewById(R.id.title_txetView);
        XQXNTv = (TextView) findViewById(R.id.XNXQ_textView);
        depTv = (TextView) findViewById(R.id.dep_textView);
        nameTv = (TextView) findViewById(R.id.name_textView);
        sexTv = (TextView) findViewById(R.id.sex_textView);
        protitleTv = (TextView) findViewById(R.id.protitle_textView);
        courseLv = (ListView) findViewById(R.id.course_listView);
        courseAdapter = new CourseAdapter(this, R.layout.course_layout, courses);
        courseLv.setAdapter(courseAdapter);

        helper = new MyOpenHelper(this, "wj.db", null, 2);

        preview();

        yzmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("john", "------------yzm onclick-----------");
                new Thread() {
                    @Override
                    public void run() {

                        getPic();
                        MainActivity.this.runOnUiThread(new Runnable() {
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

        teacherEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String teacherEtText = teacherEt.getText().toString();
                teacherList.clear();
                for (String tname:tMap.keySet()) {
                    if(tname.contains(teacherEtText)){
                        teacherList.add(tname);
                    }
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teacherSpAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


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
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        semSpAdapter.notifyDataSetChanged();
                    }
                });


                //-set teacher spinner----
                teacher = getteachersOptions();
                for (Element t : teacher) {
                    teacherList.add(t.text());
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teacherSpAdapter.notifyDataSetChanged();
                    }
                });

                //-----set yzmImage-------------
                getPic();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(yzmPath + "p1.jpg");
                        yzmImage.setImageBitmap(bm);
                    }
                });



            }
        }.start();

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

    public Elements getteachersOptions() {
        URL url;
        Elements options = null;
        try {
            String name = URLEncoder.encode("", "GBK");
            String urlstr = "http://qg.peizheng.net.cn/ZNPK/Private/List_JS.aspx?xnxq=20161&js=" + name;
            url = new URL(urlstr);
            String html = doGet(url);
            Document doc = Jsoup.parse(html);
            Elements scriptstr = doc.getElementsByTag("script");
            doc = Jsoup.parse(scriptstr.toString().substring(53, scriptstr.toString().length() - 10));
            options = doc.getElementsByTag("option");
            for (Element option : options) {
                tMap.put(option.text(), option.attr("value"));
//                System.out.println(option.attr("value"));
//                Log.i("john",option.text());
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

    public String doGet(URL url) {
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

    public String getLessons() {

        try {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("john", "------------set text primary----------");
                    titleTv.setText("");
                    XQXNTv.setText("");
                    depTv.setText("");
                    nameTv.setText("");
                    sexTv.setText("");
                    protitleTv.setText("");
                }
            });
            yzm = yzmEt.getText().toString();
            sValue = sMap.get(semSp.getSelectedItem().toString());
            tValue = tMap.get(teacherSp.getSelectedItem().toString());
            String html = "";

            final Teacher teacher = queryTeacher(tValue);

            //数据库中查不到，sendpost
            if (teacher == null) {
                Log.i("john", "------------data on net----------");
                html = SendPost(sValue, tValue, yzm, cookie);
                Log.i("john", "------------sendpost----------" + sValue + tValue);
                Log.i("john", "------------sendpost get html----------" + html);

                //验证码错误
                if (html.indexOf("验证码错误") != -1) {
                    Log.i("john", "------------yzm error----------");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                    getPic();
                    MainActivity.this.runOnUiThread(new Runnable() {
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
                if(lessons.length()==0){
                    Log.i("john", "------------no lesson----------");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "这个老师没有课0_o", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                String teacherInfo = tables.tagName("table").tagName("table").get(2).text();
                Log.i("john", "teacherInfo :" + teacherInfo);

                final Teacher t = toTeacher(teacherInfo, lessons);
                Log.i("john", "------------parse teacher----------");

                insertTeachers(t);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("john", "------------set text----------");
                        titleTv.setText("广东培正学院教师课表");
                        XQXNTv.setText(semSp.getSelectedItem().toString());
                        depTv.setText("部门：" + t.getDep());
                        nameTv.setText("教师：" + t.getName());
                        sexTv.setText("性别：" + t.getSex());
                        protitleTv.setText("职称：" + t.getProtitle());
                    }
                });
            }
            //数据库中查到，直接输出
            else{
                Log.i("john", "------------data on db----------");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("john", "------------set text----------");
                        titleTv.setText("广东培正学院教师课表");
                        XQXNTv.setText(semSp.getSelectedItem().toString());
                        depTv.setText("部门：" + teacher.getDep());
                        nameTv.setText("教师：" + teacher.getName());
                        sexTv.setText("性别：" + teacher.getSex());
                        protitleTv.setText("职称：" + teacher.getProtitle());
                    }
                });
                lessons = teacher.getLessons();
                Log.i("john", teacher.getId() + teacher.getName() + teacher.getProtitle() + teacher.getSex() + teacher.getDep());
                Log.i("john", "lessons :" + lessons);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lessons;
    }

    public static List<Courseinfo> tolessonlist(String lessons) {
        int indexzhu = lessons.length() / 2;
        Log.i("john", "------------" + lessons + "-----------");
        lessons = lessons.substring(0, indexzhu);
        List<Courseinfo> list = new ArrayList<Courseinfo>();
        int index = 0;
        int indexend = 0;
        int i = 1;
        while (lessons.length() != 0) {
            if (lessons.contains("[") == false) {
                break;
            }
            index = lessons.indexOf("[");
            indexend = lessons.indexOf("人数");
            String temp = lessons.substring(index, indexend + 5);
            Courseinfo c = tolesson(i + "", temp);
            lessons = lessons.substring(indexend + 4);
            i++;
            list.add(c);
        }
        return list;
    }

    public static Courseinfo tolesson(String id, String temp) {
        int beginIndex = 0;
        int endIndex = 0;
//		String s = "[BEco6051]金融市场学（双语）(双语) 总学时：48 讲授学时：48 安排学时：48 [1-16周]1-3节 3210 ；14本经济学1班 14本经济学2班 14本经济学3班 14本经济学4班 14本经济统计学1班 14本经济统计学2班 人数：52";
        String s = temp;
        beginIndex = s.indexOf("[");
        endIndex = s.lastIndexOf(" 总");
        String coursename = s.substring(beginIndex, endIndex);
        beginIndex = s.indexOf("总");
        endIndex = s.lastIndexOf("[") - 1;
        String learningperiod = s.substring(beginIndex, endIndex);
        beginIndex = s.indexOf(" [") + 1;
        endIndex = s.lastIndexOf("节") + 1;
        String courseperiod = s.substring(beginIndex, endIndex);
        beginIndex = s.indexOf("节") + 1;
        endIndex = s.lastIndexOf("；") - 1;
        String classroom = s.substring(beginIndex, endIndex);
        beginIndex = s.indexOf("；") + 1;
        endIndex = s.lastIndexOf("人");
        String calss = s.substring(beginIndex, endIndex);
        beginIndex = s.indexOf("人数") + 3;
        String studentnum = s.substring(beginIndex);

        Courseinfo[] courseinfos = new Courseinfo[4];

        Courseinfo c = new Courseinfo(id, coursename, learningperiod, courseperiod, classroom, calss, studentnum);
        return c;
    }

    public static Teacher toTeacher(String teacherInfo, String lessons) {
        int index1 = 0;
        int index2 = teacherInfo.indexOf("教师");
        teacherInfo = teacherInfo.substring(0, index2 - 2) + teacherInfo.substring(index2);


        index1 = teacherInfo.indexOf("性别");
        teacherInfo = teacherInfo.substring(0, index1 - 2) + teacherInfo.substring(index1);

        index1 = teacherInfo.indexOf("职称");
        teacherInfo = teacherInfo.substring(0, index1 - 2) + teacherInfo.substring(index1);



        int index = teacherInfo.indexOf("教师");
        int indexend = teacherInfo.indexOf("性别");
        String techname = teacherInfo.substring(index + 3, indexend);
        index = teacherInfo.indexOf("性别");
        indexend = teacherInfo.indexOf("职称");
        String sex = teacherInfo.substring(index + 3, indexend);
        index = teacherInfo.indexOf("职称");
        String protitle = teacherInfo.substring(index + 3);
        index = teacherInfo.indexOf("部门");
        indexend = teacherInfo.indexOf("教师");
        String part = teacherInfo.substring(index + 3, indexend);
        Teacher teacher = new Teacher();

        teacher.setId(tMap.get(techname));
        teacher.setName(techname);
        teacher.setProtitle(protitle);
        teacher.setSex(sex);
        teacher.setDep(part);
        teacher.setLessons(lessons);
        ;
//		System.out.println(teacher.getId() + teacher.getName() + teacher.getProtitle()+teacher.getSex()+teacher.getPart());
        return teacher;
    }

    public Teacher queryTeacher(String value) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from teacher where id = '" + value +"'";
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() == 0) {
            c.close();
            db.close();
            return null;
        } else {
            Teacher t = new Teacher();
            while (c.moveToNext()) {
                t.setId(c.getString(c.getColumnIndex("id")));
                t.setName(c.getString(c.getColumnIndex("name")));
                t.setDep(c.getString(c.getColumnIndex("dep")));
                t.setProtitle(c.getString(c.getColumnIndex("protitle")));
                t.setSex(c.getString(c.getColumnIndex("sex")));
                t.setLessons(c.getString(c.getColumnIndex("lessons")));

                Log.i("weijia", id + "......" + name);
            }
            c.close();
            db.close();
            return t;
        }

    }

    public void insertTeachers(Teacher teacher) {
        Log.i("john", "------------insert teacher----------");

        SQLiteDatabase db = helper.getWritableDatabase();
//        String teacherInfo = "部门：经济学系??教师：王长虹??性别：男??职称：讲师";
//        String teacherInfo = "部门："+t.getDep()+"??教师："+t.getName()+"??性别："+t.getSex()+"??职称："+ t.getProtitle();
//        Teacher teacher = toTeacher(teacherInfo, t.getLessons());

        // id name sex protitle dep lessons
        Object[] o = new Object[6];
        o[0] = tMap.get(teacher.getName());
        o[1] = teacher.getName();
        o[2] = teacher.getSex();
        o[3] = teacher.getProtitle();
        o[4] = teacher.getDep();
        o[5] = teacher.getLessons();
        String sql = "insert into teacher (id,name,sex,protitle,dep,lessons) values (?,?,?,?,?,?)";
        db.execSQL(sql, o);
        db.close();
        Log.i("john", "------------insert teacher---finish-------");

    }

    private static String SendPost(String termId, String tid, String yzm, String cookie) {
        String html = "";
        try {
            URL url = new URL("http://qg.peizheng.net.cn/ZNPK/TeacherKBFB_rpt.aspx");
            String param = "Sel_XNXQ=" + termId + "&Sel_JS=" + tid + "&type=" + "1" + "&txt_yzm=" + yzm;
            //Connection con = Jsoup.connect("http://jw.zzti.edu.cn/ZNPK/TeacherKBFB_rpt.aspx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Referer", "http://qg.peizheng.net.cn/ZNPK/TeacherKBFB.aspx");
            conn.setRequestProperty("Origin", "http://qg.peizheng.net.cn");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(param);
            dos.flush();
            dos.close();

            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gbk"));
            StringBuilder builder = new StringBuilder();
            String line = null;


            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            html = builder.toString();
            in.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html;
    }

    public void lessonSearch(View view) {
        new Thread() {
            @Override
            public void run() {
                List<Courseinfo> cs = tolessonlist(getLessons());
                courses.clear();
                courses.addAll(cs);
                Log.i("john", "-------------course to list----------------" + courses.size());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("john", "------------course changed run on ui-----------");
                        courseAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();

    }


}
