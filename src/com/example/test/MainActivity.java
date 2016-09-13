package com.example.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.service.MyService;

import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					String json = HttpUtils.doGet("http://www.0102003.com/1111.txt");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
		new Thread(new Runnable() {
			public void run() {
				webService();
			}
		}).start();
		
	}
	
	public void webService() {
		// 命名空间  
        String nameSpace = "http://sigmund.eu/webservice/";  
        // 调用的方法名称  
        String methodName = "HelloWorld";  
        // EndPoint (WebService地址,通常是WSDL地址末尾的"?wsdl"去除后剩余的部分)
        String endPoint = "http://www.webservicesigmundtest.sigmundtest.cn/service.asmx";  
        // SOAP Action  
        String soapAction = "http://sigmund.eu/webservice/HelloWorld"; 
  
        // 指定WebService的命名空间和调用的方法名  
        SoapObject rpc = new SoapObject(nameSpace, methodName);  
        
        // 设置需调用WebService接口需要传入的参数 
        rpc.addProperty("json", true);  
  
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本  
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);  
  
        envelope.bodyOut = rpc;  
        // 设置是否调用的是dotNet开发的WebService  
        envelope.dotNet = true;  
        // 等价于envelope.bodyOut = rpc;  
        envelope.setOutputSoapObject(rpc);  
  
        HttpTransportSE transport = new HttpTransportSE(endPoint);  
        try {  
            // 调用WebService  
            transport.call(soapAction, envelope);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        // 获取返回的数据  
        SoapObject object = (SoapObject) envelope.bodyIn;
        System.out.println("WebService Result:" + object.toString());
        //Log.i("test", "WebService Result:" + object.toString());
        // 获取返回的结果  
        String result = object.getProperty("HelloWorldResult").toString();
        System.out.println("Result:" + object.toString());
        //Log.i("test", "WebService Result:" + result);
	}
	
	public void start(View view) {
		startService(new Intent(this, MyService.class));
	}
	
	
	public void stop(View view) {
		startService(new Intent(this, MyService.class));
	}
	
	public void collback(View view) {
		Toast.makeText(this, "开始执行耗时操作", Toast.LENGTH_SHORT).show();
		test(new ICollBack() {
			
			@Override
			public void onFinish(final String s) {
				// TODO Auto-generated method stub
				// onFinish()方法和 onError()方法最终还是在子线程中运行的，因此我们不可以在这里执行任何的 UI 操作
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void test(final ICollBack collBack){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
					collBack.onFinish("哈哈,接口这里有结果了");
					Log.i("test", "回调onFinish函数");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
