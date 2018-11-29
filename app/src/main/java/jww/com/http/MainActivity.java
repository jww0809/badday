package jww.com.http;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_login);
        Button btnPost = findViewById(R.id.btn_login_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {

                        String msg = null;
                        try {
                            EditText edit_name = findViewById(R.id.edit_user);
                            String name = edit_name.getText().toString();
                            EditText edit_pwd = findViewById(R.id.edit_pwd);
                            String pwd = edit_pwd.getText().toString();

                            URL url = new URL("http://10.200.1.30:8080/SW/http");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            //连接超时时间
                            urlConnection.setConnectTimeout(50 * 1000);
                            //读取超时
                            urlConnection.setReadTimeout(5 * 1000);
                            ////是否使用缓存
                            urlConnection.setUseCaches(false);
                            //设置允许输入输出
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setRequestProperty("Charset", "utf-8");
                            urlConnection.connect();
                            //请求的数据
                            String data = "name=" + URLEncoder.encode(name, "utf-8") + "pwd=" + URLEncoder.encode(pwd, "utf-8");
                            //获取输出流，并将请求内容写入该流
                            OutputStream outputStream = urlConnection.getOutputStream();
                            outputStream.write(data.getBytes());
                            outputStream.flush();

                            if (urlConnection.getResponseCode() == 200) {
                                //获取响应的输入流对象
                                InputStream inputStream = urlConnection.getInputStream();
                                //创建字节输出流对象
                                ByteArrayOutputStream message = new ByteArrayOutputStream();
                                int len = 0;
                                byte[] buffer = new byte[1024];
                                while ((len = inputStream.read(buffer)) != -1) {
                                    message.write(buffer, 0, len);
                                }
                                inputStream.close();
                                message.close();
                                msg = new String(message.toByteArray());
                                Log.i("http(post)请求结果", msg);
                            }

                        } catch (
                                MalformedURLException e) {
                        } catch (
                                IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

     /*   //使用get方式访问
       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            URL url = new URL("http://10.200.1.30:8080/SW/http");
                            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                            urlConnection.connect();
                            if(urlConnection.getResponseCode()==200){
                                //读取数据
                                InputStream inputStream = urlConnection.getInputStream();
                                InputStreamReader isr = new InputStreamReader(inputStream,"UTF-8");
                                BufferedReader br=new BufferedReader(isr);
                                String temp;
                                StringBuffer stringBuffer = new StringBuffer();
                                while((temp=br.readLine())!=null){
                                    stringBuffer.append(temp);
                                }
                                br.close();
                                isr.close();
                                inputStream.close();
                                Log.i("http请求结果",stringBuffer.toString());
                            }else{
                                Log.i("http请求结果","fail-Connection");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
       */

    }


}
