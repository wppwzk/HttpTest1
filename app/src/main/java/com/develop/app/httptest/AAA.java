package com.develop.app.httptest;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by YBC on 2017/6/21 10:50.
 */

public class AAA extends Activity {
    private int a, b, c;

    public void sendGet(String url) throws IOException {
        HttpClient httpClient=new DefaultHttpClient(defaultHttpParams());
        HttpGet httpGet=new HttpGet(url);
        httpGet.addHeader("Connection","Keep-Alive");
        HttpResponse response=httpClient.execute(httpGet);
        HttpEntity entity=response.getEntity();
        if(entity!=null){
            InputStream inputStream=entity.getContent();
         /*   String result=convertStreamToString(inputStream);
            Log.e("","hhh"+result);*/
            inputStream.close();
        }
    }
    private static HttpParams defaultHttpParams(){
        HttpParams mDefaultParams=new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(mDefaultParams,10000);
        HttpConnectionParams.setSoTimeout(mDefaultParams,15000);
        HttpConnectionParams.setTcpNoDelay(mDefaultParams,true);
        HttpConnectionParams.setStaleCheckingEnabled(mDefaultParams,false);
        HttpProtocolParams.setVersion(mDefaultParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUseExpectContinue(mDefaultParams,true);
        return mDefaultParams;
    }

    //private
}
