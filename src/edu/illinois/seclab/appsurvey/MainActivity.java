package edu.illinois.seclab.appsurvey;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class MainActivity extends ActionBarActivity {

	static final String PREFS_NAME = "SharedPreferences";
	private static final String TAG = "MainActivity";
	public static boolean decision = false;
	public static Context ctx;
	public static Handler mHandler;
	
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		MainActivity.ctx = this;
		Preferences.userAgent = new WebView(MainActivity.ctx).getSettings().getUserAgentString();
		
		final PlaceholderFragment placeholderFragment = new PlaceholderFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment).commit();
		}	
		
		// Listen for asynchronous messages from other threads
	    MainActivity.mHandler = new Handler(Looper.getMainLooper()){
	    	 /*
	         * handleMessage() defines the operations to perform when
	         * the Handler receives a new Message to process.
	         */
	        @Override
	        public void handleMessage(Message inputMessage) {
	            switch(inputMessage.what){
	            	case Client.DATA_SENT:
	            		Utils.alert("App completed successfully! You may now uninstall the app if you wish.", ctx);
	            		break;
	            	case Client.DATA_SENT_ERROR:
	            		Utils.alert("Error sending data!", ctx);
	            		break;
	            	case Client.SERVER_CONNECT_ERROR:
	            		Utils.alert("Error connecting to the server!", ctx);
	            		break;
	            	default:
	            }
	            
	        }

	    };
		
	}


	@Override
    protected void onStop(){
       super.onStop();

      // We need an Editor object to make preference changes.
      // All objects are from android.context.Context
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      
      //persist acceptedPolicy
      editor.putBoolean("acceptedPolicy", Preferences.acceptedPolicy);
      //persist userId
      editor.putString("userId", Preferences.userId);

      // Commit the edits!
      editor.commit();
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, UserSettings.class);
            startActivityForResult(i, UserSettings.RESULT_SETTINGS);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case UserSettings.RESULT_SETTINGS:
            //showUserSettings();
            break;
 
        }
 
    }
	
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/
	/*****************************************              FRAGMENT            ********************************************************/
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/
	/***********************************************************************************************************************************/

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment{
		PackageManager packageManager;
		DialogInterface.OnClickListener dialogClickListener;
		TextView tv_uid;
		ListView lv_apps;
		View rootView;
		private boolean resumeHasRun = false;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			prepareListeners();
			
			Utils.loadPrefs();
			
			updateUI_id(fetchId());
			
			if(checkPolicyConsent()){
				normalExecution();
			}
			
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			//Log.d(TAG, "OnResume");
			
			if(resumeHasRun){
				//do not go through with this the first time
				if(!Preferences.dataWritten && Preferences.acceptedPolicy){
					/*
					if(Utils.isNetworkAvailable(ctx)){
						Client cl = new Client(getAppList());
						cl.start();
					}
					else{
						informNoInternet();
					}
					*/
					normalExecution();
				}
				else{
					Log.d(TAG, "Data already sent or policy is rejected. Do nothing!");
				}
			}
			else{
				resumeHasRun = true;
			}
			
			super.onResume();
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		/**
		 * Prepare button listeners
		 */
		private void prepareListeners() {
			dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            //Yes button clicked
			        	MainActivity.decision = true;
			        	Preferences.acceptedPolicy = true;
			        	Utils.storePrefs();
			        	
			        	normalExecution();
			        	
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			        	MainActivity.decision = false;
			            break;
			        }
			    }
			};
		}

		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		/**
		 * Write user id on display
		 */
		private void updateUI_id(String id) { 
			
			tv_uid = (TextView) rootView.findViewById(R.id.tv_uid);
			tv_uid.setText(id.substring(0, 6));
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/

		/**
		 * Creates a random user id
		 * @return the first 6 characters of the randomly generated id
		 */
		private String fetchId() {		
			String userId = Preferences.userId;
			
			if(userId.compareTo("") == 0){
				//create userId
				userId = getUserId();
				if(userId == null){
					//show notification of failure
					Log.d(TAG, "Failed to create ID!");
				}
				else{
					//Success
					Preferences.userId = userId;
					Log.d(TAG, "User ID generated: " + Preferences.userId);
				}
			}
			else{
				Log.d(TAG, "User id has been set before.");
			}
			
			return userId;
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		
		/**
		 * 
		 * @return A String representation of a random ID or null
		 */
		private String getUserId() {
			// get IMEI
			
			//get digest
			String id = Security.getRandomId();
			if(id.compareTo("") == 0){
				return null;
			}
			
			return id;
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		

		/**
		 * Presents the user with the policy and reads her input
		 * @return true id user conceded, false otherwise
		 */
		private boolean checkPolicyConsent() {
			boolean result = false;

			if(!Preferences.acceptedPolicy){
				getPolicyInput(MainActivity.ctx);
			}
			else{
				result = true;
			}
			
			return result;
				
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		
		/**
		 * Presents the user with the policy
		 * @param ctx
		 */
		private void getPolicyInput(Context ctx) {

			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage(Html.fromHtml(Utils.getPolicy())).setPositiveButton("Agree", dialogClickListener)
			    .setNegativeButton("Decline", dialogClickListener);
			builder.setTitle("Policy");
			builder.show();
			
		}
		
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		/**
		 * Updates the UI with the list of installed app, and sends the data to the server if not already sent and if Internet is available.
		 * If there is no Internet it informs the user.
		 */
		protected void normalExecution() {
			//get List of Apps
			List<PackageInfo> appList = getAppList();
			
			//update UI
			setUIappList(appList);
			
			//store List of Apps in File - overwrite previous if any - 
			
			//if internet is on send list of apps
			//Client.sendData(appList);
			if(!Preferences.dataWritten){
				
				if(Utils.isNetworkAvailable(ctx)){
					Client cl = new Client(appList);
					cl.start();
				}
				else{
					Utils.alert("Survey cannot be completed. Please connect to the Internet and relaunch the app!", ctx);
				}
			}
			else{
				Log.d(TAG, "Data already sent. Do nothing!");
			}
			
		}

		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		/**
		 * Displays the device's list of installed apps on the main screen. The app names are clickable and they lead to a screen with information regarding the selected app.
		 * @param appList
		 */
		private void setUIappList(List<PackageInfo> appList) {
			lv_apps = (ListView) rootView.findViewById(R.id.lv_apps);
			lv_apps.setAdapter(new ApkAdapter(getActivity(), appList, packageManager));
			
			lv_apps.setOnItemClickListener(new OnItemClickListener()
			{
			    @Override
			    public void onItemClick(AdapterView<?> parent, View arg1,int position, long arg3)
			    { 
			    	PackageInfo packageInfo = (PackageInfo) parent.getItemAtPosition(position);
					AppListApp.setPackageInfo(packageInfo);
					Intent appInfo = new Intent(getActivity().getApplicationContext(), ApkInfo.class);
					
					startActivity(appInfo);
			    }
			});
		}

		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		/**************************************************************************************************************************/
		
		/**
		 * Gets the list of installed apps from the device. It does not filter out the system apps.
		 * @return A list of installed apps.
		 */
		private List<PackageInfo> getAppList() {
			packageManager = MainActivity.ctx.getPackageManager();
			List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

			List<PackageInfo> packageList1 = new ArrayList<PackageInfo>();
			
			/*To filter out System apps*/
			for(PackageInfo pi : packageList) {
				//boolean b = Utils.isSystemPackage(pi);
				//if(!b) {
					packageList1.add(pi);
				//}
			}
			
			return packageList1;
		}
	}

	

}
