package com.develop.app.httptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import cn.bmob.v3.Bmob;

public class MainActivity extends Activity {

	private Button btn_register, btn_login;
    private EditText et_user_name, et_password;

	//防止按钮连续点击
	private static long lastClickTime=0;
	private boolean islanServertest=true;//false

	static final int UPDATE_REGISTER =1001;
	static final int QUERY_LOGIN =1002;

	/**
     * 创建一个Handler实例，用于接收支付查询返回消息
     */
    private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
			String text;
			int statusCode;

            switch (msg.what) {
            
            case UPDATE_REGISTER:
            	statusCode = msg.getData().getInt("statusCode");
            	Log.i("logB","QUERY_REGISTER: statusCode="+statusCode);
            	if(statusCode==200){
	            	text = msg.getData().getString("content");
	            	if((text!=null) && (text.length()>2)){
	            		text = text.replace("\"", "");
	            	}
	            	Log.i("logB","get content="+text);
				} else if(statusCode == 500){
					Toast.makeText(MainActivity.this,"服务器异常，请稍后重试",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this,"网络错误，请重试",Toast.LENGTH_SHORT).show();
				}
            	break;
			case QUERY_LOGIN:
				statusCode = msg.getData().getInt("statusCode");
				Log.i("logB","QUERY_LOGIN: statusCode="+statusCode);
				if(isFinishing()){
					Log.i("logB","activity isFinishing");
					return;
				}
				String strHintTryAgain="";
				if(statusCode==200){
					try {
						text = msg.getData().getString("content");//content;
						Log.i("logB", "get content=" + text);
						JSONObject jb = new JSONObject(text);
						String result = jb.getString("result");
						if (result.equals("fail")) {
							strHintTryAgain = "注册失败，请重试";
						} else if (result.equals("success")) {
							strHintTryAgain = "注册成功！";
						} else {
							strHintTryAgain = "信息提交中心，网络错误，请重试";
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				} else if(statusCode == 500){
					strHintTryAgain = "服务器异常，请稍后重试";
				} else {
					strHintTryAgain = "网络错误，请重试";
				}
				Toast.makeText(MainActivity.this, strHintTryAgain, Toast.LENGTH_SHORT).show();
				break;
            }
        }        
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register.setOnClickListener(new LoginOnClickListener());
        btn_login.setOnClickListener(new LoginOnClickListener());
    }

	// 登录事件
	public class LoginOnClickListener implements View.OnClickListener {
		
		@Override
		public void onClick(View v) {
			if (v == btn_register) {
				//若是连续点击，只响应第一次
		        if(isContinuousClick()){
		        	return;
		        }
				try {
					String strUserName = et_user_name.getText().toString().trim();
					String strPassword = et_password.getText().toString().trim();
					StringBuilder sb = new StringBuilder();
					String url;
					if(islanServertest) {
						sb.append("name=" + strUserName);
						sb.append("&password=" + strPassword);
						sb.append("&email="+"ybybuuds@sina.com");
						Log.i("logB", "params=" + sb.toString());
						//url = "http://115.159.120.114:8080/webtest-ok1/api/register";
						url = "http://bbs.bmbhome.org/api/login";
					} else {
						url = "http://www.baidu.com";
					}
					Log.i("logB","url="+url);
					HttpUtils.httpPost(MainActivity.this,
							url,
							sb.toString(),
							mHandler,
                            UPDATE_REGISTER
							);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(v == btn_login){
				//若是连续点击，只响应第一次
		        if(isContinuousClick()){
		        	return;
		        }
                String strUserName = et_user_name.getText().toString().trim();
                String strPassword = et_password.getText().toString().trim();
                StringBuilder sb = new StringBuilder();

				String url;
				if(islanServertest) {
					sb.append("name=" + strUserName);
					sb.append("&password=" + strPassword);
					Log.i("logB", "params=" + sb.toString());
					//url = "http://115.159.120.114:8080/webtest-ok1/api/login"+"?"+sb.toString();
					url = "http://bbs.bmbhome.org/api/login"+"?"+sb.toString();
				} else {
					url = "http://www.baidu.com";
				}

				Log.i("logB","url="+url);
                HttpUtils.httpGet(MainActivity.this,
						url,
                        mHandler,
                        QUERY_LOGIN
                );
			}
		}
	}

    private boolean isContinuousClick(){
        //若连续点击，只响应第一次，并且提示。只提示第一次的连续点击，避免长时间显示提示信息。
        //Toast.LENGTH_LONG（3.5秒）和Toast.LENGTH_SHORT（2秒）
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            Log.i("logB","isFastDoubleClick, ignore this click");
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
