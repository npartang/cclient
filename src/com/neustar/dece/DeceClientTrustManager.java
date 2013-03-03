package com.neustar.dece;



import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;



public class DeceClientTrustManager implements X509TrustManager {

	/** Host name verify flag. */
	static Logger LOGGER = Logger.getLogger(DeceClientTrustManager.class);
	//private final static Logger LOGGER = Logger.getLogger(DeceClientTrustManager.class.getCanonicalName());
	
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		
		LOGGER.debug("DeceClientTrustManager::checkClientTrusted:Checking checkClienttrusted");
		
	}

	
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		LOGGER.debug("DeceClientTrustManager::checkServertTrusted:Checking checkServertrusted");
		
	}

	
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

}


