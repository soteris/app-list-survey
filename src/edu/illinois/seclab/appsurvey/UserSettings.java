package edu.illinois.seclab.appsurvey;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserSettings extends Activity{

	public static final int RESULT_SETTINGS = 8975;
	private static final String TAG = "UserSettings";
	SharedPreferences.Editor editor;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_usersettings);		
 
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        editor = settings.edit();
        
        TextView textView = (TextView) findViewById(R.id.settings_policy);
        textView.setText(Html.fromHtml(Utils.getPolicy()));
        
        checkConsentButton();
    }
	
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/****************************************          RADIO BUTTONS    *******************************************************/
	/**************************************************************************************************************************/
	/**
	 * Checks the previous user selection on the policy.
	 */
	private void checkConsentButton() {
		// TODO Auto-generated method stub
		RadioButton radioButtonAccept = (RadioButton) findViewById(R.id.radio_accept);
        RadioButton radioButtonReject = (RadioButton) findViewById(R.id.radio_reject);
        
        // Make radio buttons sticky. Show previous selection.
        if(Preferences.acceptedPolicy){
        	radioButtonAccept.setChecked(true);
        	radioButtonReject.setChecked(false);
        }
        else{
        	radioButtonReject.setChecked(true);
        	radioButtonAccept.setChecked(false);
        }
	}

	/**
	 * Store user's selection w.r.t. <b>Agree</b> or <b>Decline</b> on the policy.
	 * @param view
	 */
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_accept:
	            if (checked)
	            // Policy Accepted
	            //persist acceptedPolicy
	            Log.d(TAG, "Policy accepted!");
	            Preferences.acceptedPolicy = true;
	            editor.putBoolean("acceptedPolicy", Preferences.acceptedPolicy);
	            // Commit the edits!
	            editor.commit();
	            break;
	        case R.id.radio_reject:
	            if (checked)
	            	//persist acceptedPolicy
	            	Log.d(TAG, "Policy rejected!");
		            Preferences.acceptedPolicy = false;
		            editor.putBoolean("acceptedPolicy", Preferences.acceptedPolicy);
		            // Commit the edits!
		            editor.commit();
	            break;
	    }
	}
}
