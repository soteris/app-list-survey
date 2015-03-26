package edu.illinois.seclab.appsurvey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.pm.PackageInfo;
import android.os.Message;
import android.util.Log;

/**
 * Networking must be done on a separate than the UI thread. Client will send the collected data to the server.
 * @author soteris
 *
 */
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

	/**
	 * Prepare and send the POST data to the Server
	 * @param appList A list of PackageInfo with the device installed apps
	 */
	public static void sendData(final List<PackageInfo> appList) {
		// TODO Auto-generated method stub
				int counter = 0;
				
				 Hermes hermes = new Hermes();
				 
				 // Create POST Data
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
					// send the data
					response = hermes.send_post(data_list, Preferences.server_url);
					
					if (response != null){
					 	String result;
						result = EntityUtils.toString( response.getEntity()).trim();
						//Log.i(TAG, "Server result: " + result + ". Result length: " + result.length());
							
						if(result.compareTo(Preferences.SERVER_SUCCESS_RESPONSE) == 0){
							Log.i(TAG, "SUCCESS!");
							// Remember successful transmission of data to avoid re-sending it.
							Preferences.dataWritten = true;
							Utils.storeSharedPref("dataWritten", Preferences.dataWritten);
								
							//Notify UI thread for successful transmission of data
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
					e1.printStackTrace();
					//Notify UI thread regarding the error
					Message connectError = MainActivity.mHandler.obtainMessage(Client.SERVER_CONNECT_ERROR);
					connectError.sendToTarget();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					//Notify UI thread regarding the error
					Message connectError = MainActivity.mHandler.obtainMessage(Client.SERVER_CONNECT_ERROR);
					connectError.sendToTarget();
				}
				 
		
	}

}
