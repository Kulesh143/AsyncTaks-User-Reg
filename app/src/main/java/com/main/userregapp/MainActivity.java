package com.main.userregapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.main.userregapp.model.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SignUp(View view) {
        String uid = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String uname = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String mobile = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText4)).getText().toString();

        final User u = new User(uid,uname,mobile,password);
        Gson gson = new Gson() ;
        final String jsonString = gson.toJson(u);

        AsyncTask asyncTask = new AsyncTask() {
            String responseText = null ;
            HttpURLConnection connection = null ;
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                System.out.println("Message Send ");
            }

            @Override
            protected void onPostExecute(Object o) {
                Toast toast = Toast.makeText(getApplicationContext() , ""+responseText, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    URL url = new URL("http://192.168.43.54:8080/MyAndroid/Register?jsonString="+jsonString);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    //connection.setChunkedStreamingMode(0);
                    //connection.setDoOutput(true);
                    connection.setDoInput(true);
//                    OutputStream out = connection.getOutputStream() ;
//                    String param = "jsonString="+jsonString ;
//                    out.write(param.getBytes());
//                    out.flush();
                    if(connection.getResponseCode()== HttpsURLConnection.HTTP_OK){
                        InputStream in = connection.getInputStream() ;
                        int i = 0 ;
                        while ((i = in.read()) != -1){
                            responseText += (char) i ;
                        }
                        publishProgress();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }

                return null;
            }
        };
        asyncTask.execute();
    }
}
