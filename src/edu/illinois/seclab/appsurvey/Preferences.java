package edu.illinois.seclab.appsurvey;

public class Preferences {

	/** Server response when successfully read the sent data. */
	public static final String SERVER_SUCCESS_RESPONSE = "xx";
	/** Prefix for all Post IDs sent. */
	public static final String PREFIX = "xx";
	/** Keystore password */
	public static final String TRUSTSTORE_PASSWORD = "xx";
	
	/** The Application Policy. User must consent before sending any data to the server. */
	public static CharSequence policyTxt = "You	are	invited	to participate in a research study examining the use of	application " +
			"information to profile users. By clicking on “Agree” you consent to this application’s	" +
			"collection	of	a	list	of	the	applications	and	packages	installed	on	your	phone.	This " +
			"study	will	take	approximately	5-7 minutes	of	your	time	and asks	basic	" +
			"demographics	questions	as	well	as	information	about	your	current	health	" +
			"conditions	and	online	search	and	purchasing	habits.	Your	decision	to	participate	or	" +
			"decline	participation	in	this	study	is	completely	voluntary,	and	you	have	the	right	to	" +
			"terminate	your	participation	at	any	time	without	penalty.	If	you	do	not	wish	to " +
			"complete	this	study,	just	click	“Decline” and	delete	the	application	off	of	your	phone.	" +
			"Your	participation	in	this	research	will	be	anonymous. If	you	have	questions	about	" +
			"this	project,	you	may	contact	the	research	team	at seclab-android@illinois.edu." +
			"<br/>" + "<br/>" +
			"I have	read and understand	the	above consent form,	I	certify	that	I	am	18	years	old " +
			"or	older,	and	by	clicking	the	agree	button I	indicate	my	willingness	to	voluntarily	" +
			"take	part in	the	study	and agree to	have	the	list	of	installed	packages	and	" +
			"applications	on	your	phone	sent	to	the	research	team at the University of Illinois at Urbana-Champaign.";
	
	/** Server URL */
	public static String server_url = "xx";
	
	/** Boolean indicating whether the user has accepted the policy. This mirrors the value in SharedPreferences.*/
	public static boolean acceptedPolicy = false;
	/** The randomly generated User ID. */
	public static String userId = "";
	/** Boolean indicating whether the data has been successfully sent to the server. */
	public static boolean dataWritten = false;
	/** Device User Agent. <i>Currently not used.</i> */
	public static String userAgent = "";

}
