package com.ybc.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button get,dengl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get = (Button) findViewById(R.id.buttonget);
        dengl= (Button) findViewById(R.id.buttondengl);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Doget();
                doPost();
            }
        });
dengl.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        doPostlog();
    }
});
    }

    private void Doget() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("http://bbs.bmbhome.org/api/register?name=kikik&email=qqwwq@sina.com&password=123456").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ff", "fall");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("ss", response + "");
            }
        });
    }
    private void doPost(){
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builderf = new FormBody.Builder();
        builderf.add("name","yyyy").add("email","1069402027@qq.com").add("password","123456");
        RequestBody formBody = builderf.build();
        Request.Builder builder = new Request.Builder();
        Request request=builder.url("http://bbs.bmbhome.org/api/register").post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ff", "fall");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("ss", response + "");
            }
        });
    }
    private void doPostlog(){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builderf = new FormBody.Builder();
        builderf.add("email","1069402027@qq.com").add("password","123456");
        RequestBody formBody = builderf.build();
        Request.Builder builder = new Request.Builder();
        Request request=builder.url("http://bbs.bmbhome.org/api/login").post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ff", "fall");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("ss", response + "");
            }
        });
    }

}
