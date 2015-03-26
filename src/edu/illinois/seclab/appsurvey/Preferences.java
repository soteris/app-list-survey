package edu.illinois.seclab.appsurvey;

public class Preferences {

	public static final String SERVER_SUCCESS_RESPONSE = "OK";
	public static final String PREFIX = "appsurvey";
	public static final String TRUSTSTORE_PASSWORD = "";
	public static boolean acceptedPolicy = false;
	public static String userId = "";
	
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
	
	public static CharSequence policyTxt2 = "I have	read and understand	the	above consent form,	I	certify	that	I	am	18	years	old " +
			"or	older,	and	by	clicking	the	agree	button I	indicate	my	willingness	to	voluntarily	" +
			"take	part in	the	study	and agree to	have	the	list	of	installed	packages	and	" +
			"applications	on	your	phone	sent	to	the	research	team at the University of Illinois at Urbana-Champaign.";
	
	
	public static String server_url = "https://siebl-4309a-03.cs.illinois.edu/scea/mhandler.php";
	//public static String server_url = "https://seclab.illinois.edu/android/scea/handler.php";
	//public static String server_url = "https://seclab.illinois.edu/test.php";
	//public static String server_url = "http://www.reliply.org/tools/requestheaders.php"; //test
	//public static String server_url = "https://google.com"; //test
	
	public static boolean dataWritten = false;
	public static String userAgent = "";

}
