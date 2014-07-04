package org.lisah.openfluid.wareshubclient.importWizards;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.eclipsesource.json.JsonObject;

public class RemoteWaresHub {

	public class WareData {
		String ID;

		String Description;
	}


	private String remoteURL;

	private JsonObject remoteInfos;

	private ArrayList<String> remoteWareTypes;

	private HashMap<String, ArrayList<WareData>> remoteWares;

	private String errorMsg;

	
	// =====================================================================
	// =====================================================================
	
	
	public RemoteWaresHub() {
		remoteWares = new HashMap<String,ArrayList<WareData>>();
		remoteWareTypes = new ArrayList<String>();

		resetInfos();
	}


	// =====================================================================
	// =====================================================================
	
	
	private void resetInfos() {
		errorMsg = "";
		remoteInfos = null;
		remoteWares.clear();
		remoteWareTypes.clear();
	}

	
	// =====================================================================
	// =====================================================================
	

	public void setURL(String requestedURL) {
		remoteURL = requestedURL+"/fluidhub.php?request=wares-list-detailed";	
	}


	// =====================================================================
	// =====================================================================


	private void parseAvailableWares() {

		String[] types = {"simulators", "observers", "builderexts"};

		JsonObject waresObject = remoteInfos.get("wares").asObject();

		if (waresObject.isObject()) {
			for (String t: types) {

				JsonObject wareTypeObject = waresObject.get(t).asObject();
				if (!wareTypeObject.isEmpty()) {
					remoteWareTypes.add(t);					
					ArrayList<WareData> waresList = new ArrayList<WareData>();

					for(String ID : wareTypeObject.names() ) {
						WareData wareData = new WareData();
						wareData.ID = ID;
						wareData.Description = 
								wareTypeObject.get(ID).asObject()
								.get("shortdesc").asString();
						waresList.add(wareData);
					}					
					remoteWares.put(t,waresList);
				}
			}
		}		
	}


	// =====================================================================
	// =====================================================================

	
	public String getErrorMsg() {
		return errorMsg;
	}


	// =====================================================================
	// =====================================================================
	
	
	public String getURL() {
		return remoteURL;
	}

	
	// =====================================================================
	// =====================================================================

	
	public HashMap<String,ArrayList<WareData>> getAvailableWares() {
		return remoteWares;	
	}

	
	// =====================================================================
	// =====================================================================

	
	public JsonObject getAvailableInfos() {
		return remoteInfos;	
	}

	
	// =====================================================================
	// =====================================================================
	

	public boolean connect(boolean noSSLVerify) throws SSLHandshakeException
	{
		resetInfos();

		if (noSSLVerify)
		{
			TrustManager[] trustAllCerts = new TrustManager[] { 
					new X509TrustManager() {
						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}
						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}
						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}
					}
			};

			// Install the all-trusting trust manager
			SSLContext sc = null;
			try {
				sc = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			try {
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
			} catch (KeyManagementException e1) {
				e1.printStackTrace();
			}
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}


		URL url = null;
		try {
			url = new URL(remoteURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			errorMsg = "Malformed URL";
			return false;

		}

		InputStream is = null;
		try {
			is = url.openStream();
			Reader reader = new InputStreamReader(is);
			remoteInfos = JsonObject.readFrom(reader);

			parseAvailableWares();

		} catch (SSLHandshakeException e) {
			
			throw e;
			
		} catch (IOException e) {
			e.printStackTrace();
			errorMsg = "Cannot connect to URL";
			return false;
		}
		return true;
	}

	
	// =====================================================================
	// =====================================================================

	
	public void disconnect()
	{
		resetInfos();		
	}	

}
