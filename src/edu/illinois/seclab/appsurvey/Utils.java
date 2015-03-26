package edu.illinois.seclab.appsurvey;

import java.sql.Timestamp;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author soteris
 *
 */
public class Utils {

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void storeSharedPref(String key, boolean value) {
		SharedPreferences settings = MainActivity.ctx.getSharedPreferences(MainActivity.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	      
	    //persist acceptedPolicy
	    editor.putBoolean(key, value);
	    // Commit the edits!
	    editor.commit();
		
	}
	
	public static String getPolicy(){
		String policy = (String) Preferences.policyTxt;
		
		return policy.replaceAll("\\s+", " ");
	}
	
	/**
	 * 
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
		
		
		// get flags
		ApplicationInfo applicationInfo = app.applicationInfo;
		String flags = applicationInfo.flags + "";
		
		//GET UID
		String uid = applicationInfo.uid + "";
		
		String market = "N/A";
		
		String result = version + ":";
		
		for(String permission : permissions){
			result += permission + "+";
		}
		result = result.substring(0, result.length() - 1);
		
		result += ":" + firstInstallTime + ":" + lastUpdateTime + ":" + flags + ":" + uid + ":" + market;
		
		return result;
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
