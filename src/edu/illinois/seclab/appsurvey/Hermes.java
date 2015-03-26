package edu.illinois.seclab.appsurvey;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * 
 * @author soteris
 *
 */
public class Hermes {

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
	
	/**
	 * Send the POST data
	 * @param data_list A List of POST NameValuePairs to be sent
	 * @param server_url The server's secure URL
	 * @return The server's response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse send_post(List<NameValuePair> data_list, String server_url) throws ClientProtocolException, IOException{
		
		HttpPost httpost = new HttpPost(server_url);

		try {		
			
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

}


