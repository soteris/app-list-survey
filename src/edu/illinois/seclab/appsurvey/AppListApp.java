package edu.illinois.seclab.appsurvey;

import android.app.Application;
import android.content.pm.PackageInfo;

public class AppListApp extends Application {

	static PackageInfo packageInfo;

	public static PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public static void setPackageInfo(PackageInfo packageInfo) {
		AppListApp.packageInfo = packageInfo;
	}
	
}
