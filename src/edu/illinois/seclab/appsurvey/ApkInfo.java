package edu.illinois.seclab.appsurvey;

import android.app.Activity;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * @author http://theopentutorials.com/tutorials/android/listview/how-to-get-list-of-installed-apps-in-android/
 *
 */
public class ApkInfo extends Activity {

	TextView appLabel, packageName, version, features;
	TextView permissions, andVersion, installed, lastModify, path;
	PackageInfo packageInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apkinfo);

		findViewsById();

		packageInfo = AppListApp.getPackageInfo();

		setValues();

	}

	private void findViewsById() {
		appLabel = (TextView) findViewById(R.id.applabel);
		packageName = (TextView) findViewById(R.id.package_name);
		version = (TextView) findViewById(R.id.version_name);
		features = (TextView) findViewById(R.id.req_feature);
		permissions = (TextView) findViewById(R.id.req_permission);
		andVersion = (TextView) findViewById(R.id.andversion);
		path = (TextView) findViewById(R.id.path);
	}

	private void setValues() {
		// APP name
		appLabel.setText(getPackageManager().getApplicationLabel(
				packageInfo.applicationInfo));

		// package name
		packageName.setText(packageInfo.packageName);

		// version name
		version.setText(packageInfo.versionName);

		// target version
		andVersion.setText(Integer
				.toString(packageInfo.applicationInfo.targetSdkVersion));

		// path
		path.setText(packageInfo.applicationInfo.sourceDir);

		// features
		if (packageInfo.reqFeatures != null)
			features.setText(getFeatures(packageInfo.reqFeatures));
		else
			features.setText("-");

		// uses-permission
		if (packageInfo.requestedPermissions != null)
			permissions
					.setText(getPermissions(packageInfo.requestedPermissions));
		else
			permissions.setText("-");
	}

	// Convert string array to comma separated string
	private String getPermissions(String[] requestedPermissions) {
		String permission = "";
		for (int i = 0; i < requestedPermissions.length; i++) {
			permission = permission + requestedPermissions[i] + ",\n";
		}
		return permission;
	}

	// Convert string array to comma separated string
	private String getFeatures(FeatureInfo[] reqFeatures) {
		String features = "";
		for (int i = 0; i < reqFeatures.length; i++) {
			features = features + reqFeatures[i] + ",\n";
		}
		return features;
	}
}
