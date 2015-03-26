package edu.illinois.seclab.appsurvey;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.webkit.WebView;

public class Hermes {

    private static final int SOCKET_TIMEOUT = 3000;

	HttpClient httpClient;
	
	public Hermes(){
		KeyStore localTrustStore;
		try {
			localTrustStore = KeyStore.getInstance("BKS");
			InputStream in = MainActivity.ctx.getResources().openRawResource(R.raw.mytruststore);
			localTrustStore.load(in, Preferences.TRUSTSTORE_PASSWORD.toCharArray());

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
			                .getSocketFactory(), 80));
			SSLSocketFactory sslSocketFactory = new SSLSocketFactory(localTrustStore);
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			HttpParams params = new BasicHttpParams();
			ClientConnectionManager cm = 
			    new ThreadSafeClientConnManager(params, schemeRegistry);

			httpClient = new DefaultHttpClient(cm, params);
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//httpClient = new DefaultHttpClient();
	}
	
	public HttpResponse send_post(List<NameValuePair> data_list, String server_url) throws ClientProtocolException, IOException{
		
		HttpPost httpost = new HttpPost(server_url);

		try {		
			//Header[] headers = httpost.getAllHeaders();
			//printHeaders(headers);
			
			httpost.setEntity(new UrlEncodedFormEntity(data_list));
			
			//exec
			HttpResponse response = httpClient.execute(httpost);
			
			return response;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void printHeaders(Header[] headers) {
		for(Header hd : headers){
			System.out.println(hd.getName() + ": " + hd.getValue());
		}
		
	}

	public HttpResponse send_post2(List<NameValuePair> data_list, String server_url){
		URL url;
		try {
			url = new URL(Preferences.server_url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
			        new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(data_list));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	


	private String getQuery(List<NameValuePair> data_list) {
		StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : data_list)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        try {
	        	
				result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				result.append("=");
		        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		        
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }

	    return result.toString();
	}

	public HttpResponse send_post3(ArrayList<NameValuePair> data_list,
			String server_url) {
		
		try {
			HttpPost httpost = new HttpPost(server_url);
			
			JSONObject holder = new JSONObject();
			
			holder.put("testkey", "testvalue");
			
			StringEntity se = new StringEntity(holder.toString());
			
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");
			
			//exec
			HttpResponse response = httpClient.execute(httpost);
			
			return response;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public HttpResponse send_post4(ArrayList<NameValuePair> data_list,
			String server_url) {
		
		HttpGet httpget = new HttpGet(server_url);
		
		try {
			httpget.setURI(new URI(server_url + "?testkey=testvalue&anotherkey=anothervalue"));
			
			//exec
			HttpResponse response = httpClient.execute(httpget);
			return response;
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
		
		
	}

}


