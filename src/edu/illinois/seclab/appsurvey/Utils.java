package edu.illinois.seclab.appsurvey;

import java.sql.Timestamp;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author soteris
 *
 */
public class Utils {

	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/*************************************                 PREFERENCES           **********************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	
	/**
	 * Load shared preferences in memory
	 */
	public static void loadPrefs() {
		SharedPreferences settings = MainActivity.ctx.getSharedPreferences(MainActivity.PREFS_NAME, 0);
		
		//load data written
		Preferences.dataWritten = settings.getBoolean("dataWritten", false);
		
		//load user id
		Preferences.userId = settings.getString("userId", "");
		
		//load accepted Policy
		Preferences.acceptedPolicy = settings.getBoolean("acceptedPolicy", false);
	}
	
	/**
	 * Stores the acceptedPolicy boolean and the random userId on SharedPreferences
	 */
	public static void storePrefs(){
		// We need an Editor object to make preference changes.
	      // All objects are from android.context.Context
	      SharedPreferences settings = MainActivity.ctx.getSharedPreferences(MainActivity.PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      
	      //persist acceptedPolicy
	      editor.putBoolean("acceptedPolicy", Preferences.acceptedPolicy);
	      //persist userId
	      editor.putString("userId", Preferences.userId);

	      // Commit the edits!
	      editor.commit();
	}
	
	/**
	 * Stores a boolean shared preference
	 * @param key The preference identifier
	 * @param value The preference value
	 */
	public static void storeSharedPref(String key, boolean value) {
		SharedPreferences settings = MainActivity.ctx.getSharedPreferences(MainActivity.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	      
	    //persist acceptedPolicy
	    editor.putBoolean(key, value);
	    // Commit the edits!
	    editor.commit();
		
	}
	
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/*************************************                 ALERTS                **********************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	
	/**
	 * Shows an alert to the user 
	 */
	public static void alert(String msg, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setPositiveButton("OK", null).show();
		
	}

	
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/*************************************            FORMATTING              *************************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	
	public static String getPolicy(){
		String policy = (String) Preferences.policyTxt;
		
		return policy.replaceAll("\\s+", " ");
	}
	
	/**
	 * Gets the current time as a Timestamp instance
	 * @return
	 */
	public static String getCurrentTime(){
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		return timestamp.toString();
	}

	/**
	 * FORMAT: <b>version:p1+p2+p3:firstInstallTime:lastUpdtadeTime:flags:uid:market <br /></b>
	 * e.g: <i>version:p1+p2+p3:1365500000000:1365527172511:156:12:10001:market</i>
	 * @param app
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getPackageInfoString(PackageInfo app) {
		
		//GET VERSION
		String version = app.versionName;
		//GET PERMISSIONS
		String[] permissions = app.requestedPermissions;
		//GET FIRST INSTALL TIME
		String firstInstallTime = app.firstInstallTime + "";
		//GET LAST UPDATE TIME
		String lastUpdateTime = app.lastUpdateTime + "";
		//GET FLAGS
		ApplicationInfo applicationInfo = app.applicationInfo;
		String flags = applicationInfo.flags + "";
		//GET UID
		String uid = applicationInfo.uid + "";
		//GET MARKET
		String market = "N/A";
		
		//      BUILT THE RESULT STRING         //
		String result = version + ":";
		
		for(String permission : permissions){
			result += permission + "+";
		}
		result = result.substring(0, result.length() - 1);
		
		result += ":" + firstInstallTime + ":" + lastUpdateTime + ":" + flags + ":" + uid + ":" + market;
		
		return result;
	}
	
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/*************************************            MISCELLANEOUS              **********************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	
	/**
	 * 
	 * @param pi
	 * @return
	 */
	public static boolean isSystemPackage(PackageInfo pi) {
		return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isNetworkAvailable(Context ctx) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


}
