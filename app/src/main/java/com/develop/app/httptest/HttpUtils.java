package com.develop.app.httptest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class HttpUtils {

	public static int httpGet(final Context context, final String urlPath, final Handler mHandler, final int msgWhat) {

		new Thread() {

			@Override
			public void run() {

				int statusCode = -1;
				String content = "";

				// ---------------------请求数据-----------------------
				try {
					URL url = new URL(urlPath.trim());
					Log.i("logB", "send url=" + url.toString());

					//打开连接
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

					statusCode = urlConnection.getResponseCode();

					if (200 == urlConnection.getResponseCode()) {
						//得到输入流
						InputStream is = urlConnection.getInputStream();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						while (-1 != (len = is.read(buffer))) {
							baos.write(buffer, 0, len);
							baos.flush();
						}
						content = baos.toString("utf-8");
						sendHandler(context, mHandler, msgWhat, content, statusCode);
					} else {
						sendHandler(context, mHandler, msgWhat, content, statusCode);
					}
				} catch (MalformedURLException e) {
					sendHandler(context, mHandler, msgWhat, content, statusCode);
					e.printStackTrace();
				} catch (IOException e) {
					sendHandler(context, mHandler, msgWhat, content, statusCode);
					e.printStackTrace();
				}
			}
		}.start();
		return 0;
	}

	public static void httpPost(final Context context,final String urlPath, final String content, final Handler handler, final int msgWhat){

		new Thread()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				URL url;
				try {
					url = new URL(urlPath);
					Log.i("logB","content="+content);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000);//5
					conn.setReadTimeout(10000);
					conn.setDoOutput(true);// 设置允许输出
					conn.setRequestMethod("POST");
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");// "Fiddler"
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestProperty("Charset", "UTF-8");

					OutputStream os = conn.getOutputStream();
					os.write(content.getBytes());
					os.close();

					/* 服务器返回的响应码 */
					int code = conn.getResponseCode();
					Log.i("logB","code="+code);
					if (code == 200) {
						BufferedReader in = new BufferedReader(
								new InputStreamReader(conn.getInputStream(), "UTF-8"));
						String retData = null;
						String responseData = "";
						while ((retData = in.readLine()) != null) {
							responseData += retData;
						}
						Log.i("logB","responseData="+responseData);
						in.close();
						sendHandler(context,handler,msgWhat,responseData,code);
					} else {
						sendHandler(context,handler,msgWhat,"",code);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					Log.i("logB","MalformedURLException");
					sendHandler(context,handler,msgWhat,"",-1);
				} catch (IOException e) {
					e.printStackTrace();
					Log.i("logB","IOException");
					sendHandler(context,handler,msgWhat,"",-1);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					sendHandler(context,handler,msgWhat,"",-1);
				}
				Looper.loop();
			}

		}.start();
	}

	private static void sendHandler(Context context, Handler mHandler, int msgWhat, String content, int statusCode){

		if (context != null) {
			Message msg = mHandler.obtainMessage(msgWhat);
			Bundle bundle = new Bundle();
			bundle.putString("content", content); // 往Bundle中存放数据
			bundle.putInt("statusCode", statusCode); // 往Bundle中存放statusCode
			msg.setData(bundle);
			mHandler.sendMessage(msg);
		}
	}

}
