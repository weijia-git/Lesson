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

public class ClassesActivity extends AppCompatActivity {

    static String htmlurl = "http://qg.peizheng.net.cn/ZNPK/KBFB_ClassSel.aspx";
    static String cookie = "";

    private Spinner semSp;
    private List<String> semList = new ArrayList<String>();
    private ArrayAdapter<String> semSpAdapter;
    Elements semester;
    Map<String, String> sMap = new HashMap<String, String>();
    private String sValue = "";

    private Spinner classesSp;
    private List<String> classesList = new ArrayList<String>();
    private ArrayAdapter<String> classesSpAdapter;
    Elements classes;
    Map<String,String> cMap = new HashMap<String,String>();
    private EditText classesEt;
    private String cValue = "";

    private String yzm = "";
    private ImageView yzmImage;
    private EditText yzmEt;
    private String yzmPath = "//data/data/com.example.pc.lesson/files/";

    private TextView titleTv;
    private TextView XQXNTv;
    private TextView gradeTv;
    private TextView majorTv;
    private TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        semSp = (Spinner) findViewById(R.id.semester_Sp);
        semSpAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semList);
        semSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSp.setAdapter(semSpAdapter);

        classesSp = (Spinner) findViewById(R.id.classes_Sp);
        classesSpAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,classesList);
        classesSpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classesSp.setAdapter(classesSpAdapter);
        classesEt = (EditText) findViewById(R.id.classes_editView);

        yzmImage = (ImageView) findViewById(R.id.yzm_Image);
        yzmEt = (EditText) findViewById(R.id.yzm_Et);

        classesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String classesEtText = classesEt.getText().toString();
                classesList.clear();

                for (String cname:cMap.keySet()) {
                    if(cname.contains(classesEtText)){
                        classesList.add(cname);
                    }
                }
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        classesSpAdapter.notifyDataSetChanged();
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
                        ClassesActivity.this.runOnUiThread(new Runnable() {
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
        gradeTv = (TextView) findViewById(R.id.grade_textView);
        majorTv = (TextView) findViewById(R.id.major_textView);
        nameTv = (TextView) findViewById(R.id.name_textView);

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
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        semSpAdapter.notifyDataSetChanged();
                    }
                });

                //set classes spinner
                classes = getClassesOptions(cMap);
                for (Element cla : classes ) {
                    classesList.add(cla.text());
                }
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        classesSpAdapter.notifyDataSetChanged();
                    }
                });

                //-----set yzmImage-------------
                getPic();
                ClassesActivity.this.runOnUiThread(new Runnable() {
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
            url = new URL(htmlurl);
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

    public static Elements getClassesOptions(Map<String,String> lessonsMap) {
        URL url;
        Elements options = null;
        try {
            String name = URLEncoder.encode("902", "GBK");
            String XNXQ = "20161";
            String xzbj = "11";
            String urlstr = "http://qg.peizheng.net.cn/ZNPK/Private/List_XZBJ.aspx?xnxq="+XNXQ+"&xzbj="+xzbj+"&t="+name;
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
                lessonsMap.put(option.attr("value"),option.text());
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
            Log.i("john","------get pic cookie"+cookie);

            conn.setRequestProperty("Referer", htmlurl);
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

    public static Classes toClassinfo(String classesinfo,String lessons){
//		classesinfo = "承担单位：计算机科学与工程系??课程：[BCim1300]计算机组成原理??总学时：54??学分：3.0";
        classesinfo = "年级：2013??专业：(本)经济学??行政班级：13本经济学1班";
        int index1 = classesinfo.indexOf("专业");
        classesinfo = classesinfo.substring(0,index1-2) + classesinfo.substring(index1);

        index1 = classesinfo.indexOf("行政");
        classesinfo = classesinfo.substring(0,index1-2) + classesinfo.substring(index1);

        System.out.println(classesinfo);

//		年级：2016专业：(本)文化产业管理行政班级：16本文化产业管理1班
        int index = classesinfo.indexOf("专业");
        int indexend = classesinfo.indexOf("行政班级");
        String major = classesinfo.substring(index+3,indexend);
        index = classesinfo.indexOf("行政班级");
        String name = classesinfo.substring(index+5);
        index = classesinfo.indexOf("年级");
        indexend = classesinfo.indexOf("专业");
        String grade = classesinfo.substring(index+3,indexend);
        Classes classes = new Classes();
        classes.setName(name);
        classes.setMajor(major);
        classes.setGrade(grade);
        classes.setLessons("");
        return classes;
    }

    private static String SendPost(String termId,String xzbj,String tid,String yzm,String cookie) {
        String html = "";
        try {
            URL url = new URL("http://qg.peizheng.net.cn/ZNPK/KBFB_ClassSel_rpt.aspx");
            String param  = "Sel_XNXQ="+termId+"&txtxzbj="+xzbj+"&Sel_XZBJ="+tid+"&type="+"1"+"&txt_yzm="+yzm;
            //Connection con = Jsoup.connect("http://jw.zzti.edu.cn/ZNPK/TeacherKBFB_rpt.aspx");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Referer",htmlurl);
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

    public Classes getClasses(){
        String lessons = "";
        Classes l = new Classes();
        try {

            yzm = yzmEt.getText().toString();
            Log.i("john",yzm+"--send post cookie"+cookie);

            String html = SendPost("20161","1","2013230901",yzm,cookie);
            if (html.indexOf("验证码错误") != -1) {
                Log.i("john", "------------yzm error----------");
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                });
                getPic();
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = BitmapFactory.decodeFile(yzmPath + "p1.jpg");
                        yzmImage.setImageBitmap(bm);
                        Log.i("john", "------------yzm change-----------");
                    }
                });
            }

            Log.i("john","--------"+html);

            Document doc = Jsoup.parse(html);
            Elements tables = doc.getElementsByTag("table");

            lessons = tables.text();



            String classesinfo = tables.tagName("table").tagName("table").get(2).text();

            l = toClassinfo(classesinfo,lessons);
            Log.i("john",classesinfo);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return l;
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

    public void lessonSearch(View view) {

        ClassesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("john", "------------set text primary----------");
                titleTv.setText("");
                XQXNTv.setText("");
                gradeTv.setText("");
                majorTv.setText("");
                nameTv.setText("");
            }
        });

        new Thread(){
            @Override
            public void run() {
                final Classes c = getClasses();
                ClassesActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("john", "------------set text primary----------");
                        titleTv.setText("广东培正学院教师课表");
                        XQXNTv.setText(semSp.getSelectedItem().toString());
                        gradeTv.setText("年级：" + c.getGrade());
                        majorTv.setText("专业：" + c.getMajor());
                        nameTv.setText("行政班级：" + c.getName());
                    }
                });



            }
        }.start();
    }
}
