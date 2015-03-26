package edu.illinois.seclab.appsurvey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.util.Log;

public class Security {

	private static final String TAG = "Security";

	/**
	 * 
	 * @return A String representation of a hashed pseudorandom number, or an empty String if it fails
	 */
	public static String getRandomId(){
		Random tmpRand = new Random();
		String deviceId = String.valueOf(tmpRand.nextLong());
		
		return getDigest(deviceId);
		
	}
	
	/**
	 * 
	 * @param plaintext
	 * @return A String representation of the ID, or an empty String if it fails
	 */
	public static String getDigest(String plaintext) {
		String str_digest = "";
		// Compute digest
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			byte[] digest = sha1.digest((plaintext).getBytes());
			
			StringBuilder sb = new StringBuilder();
			 
	    	for (byte b : digest)
	    	{
	    	    sb.append(String.format("%02X", b));
	    	}
	 
	    	str_digest = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			// 
			//e.printStackTrace();
			Log.e(TAG, "Could not build message digest!");
		}
		
		return str_digest;
	}

}
