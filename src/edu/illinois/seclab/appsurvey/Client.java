package edu.illinois.seclab.appsurvey;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.pm.PackageInfo;
import android.os.Message;
import android.util.Log;

public class Client extends Thread{

	private static final String TAG = "Client";
	protected static final int DATA_SENT = 92836;
	protected static final int DATA_SENT_ERROR = 92837;
	protected static final int SERVER_CONNECT_ERROR = 92838;
	List<PackageInfo> appList;

	public Client(List<PackageInfo> appList) {
		this.appList = appList;
	}


	@Override
	public void run() {
		sendData(appList);
	}
	

	public static void sendData(final List<PackageInfo> appList) {
		// TODO Auto-generated method stub
				int counter = 0;
				
				 Hermes hermes = new Hermes();
				 ArrayList<NameValuePair> data_list = new ArrayList<NameValuePair>();
				    
				 data_list.add(new BasicNameValuePair(Preferences.PREFIX + "_id", Preferences.userId));
				 data_list.add(new BasicNameValuePair(Preferences.PREFIX + "_timestamp", Utils.getCurrentTime()));
				 data_list.add(new BasicNameValuePair(Preferences.PREFIX + "_numOfapps", appList.size() + ""));
				 
				 for(PackageInfo app : appList){
					 int id = counter++;
					 String app_id = Preferences.PREFIX + "_app_" + id;
					 //Log.d(TAG, "App_id: " + app_id);
					 data_list.add(new BasicNameValuePair(app_id, app.packageName));
					 data_list.add(new BasicNameValuePair("app_info_" + id, Utils.getPackageInfoString(app)));
				 }
				 
				 
				 
				 Log.i(TAG, "Sending data ...");
				 HttpResponse response;
				try {
					response = hermes.send_post(data_list, Preferences.server_url);
					
					if (response != null){
					 	String result;
						result = EntityUtils.toString( response.getEntity()).trim();
						//Log.i(TAG, "Server result: " + result + ". Result length: " + result.length());
							
						if(result.compareTo(Preferences.SERVER_SUCCESS_RESPONSE) == 0){
						//if(result.startsWith(Preferences.SERVER_SUCCESS_RESPONSE)){
							Log.i(TAG, "SUCCESS!");
							Preferences.dataWritten = true;
							Utils.storeSharedPref("dataWritten", Preferences.dataWritten);
								
							//Notify UI thread
							Message completeMessage = MainActivity.mHandler.obtainMessage(Client.DATA_SENT);
				            completeMessage.sendToTarget();
				        }
						else{
							//Notify UI thread
							Message completeMessage = MainActivity.mHandler.obtainMessage(Client.DATA_SENT_ERROR);
				               completeMessage.sendToTarget();
						}
					  	
					 }	
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//Notify UI thread
					Message connectError = MainActivity.mHandler.obtainMessage(Client.SERVER_CONNECT_ERROR);
					connectError.sendToTarget();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message connectError = MainActivity.mHandler.obtainMessage(Client.SERVER_CONNECT_ERROR);
					connectError.sendToTarget();
				}
				 
		
	}

}
