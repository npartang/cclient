package com.neustar.dece;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

import org.apache.log4j.Logger;



public class DeceClientKeyManager {

	//final Log LOG = LogFactory.getLog(this.getClass());
	static Logger log = Logger.getLogger(DeceClientKeyManager.class);
	//private final static Logger log = Logger.getLogger(DeceClientKeyManager.class.getCanonicalName());
	private static final String KEYSTORE_TYPE = "JKS";
	private static final String SECURE_ALGORITHM = "SunX509";
	
	
	private KeyManager[] kman;
	
	public DeceClientKeyManager(String keyStoreFilename,
            String keyStorePassword, String keyPassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException{
		

		log.debug("DeceClientKeyManager:init::Initializing key store with file="+keyStoreFilename);
		log.debug("Initializing key store with file="+keyStoreFilename);

		KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
		
		ks.load(new FileInputStream(keyStoreFilename),keyStorePassword.toCharArray() );
		log.debug("DeceClientKeyManager:init:Loaded file from  key store");
		
		 Enumeration<String> aliases = ks.aliases();
		 
         while (aliases.hasMoreElements()) {
        	 
             String alias = (String)aliases.nextElement();   
             log.debug("DeceClientKeyManager:init:Inside while loop with alias="+alias);
            Certificate certs = ks.getCertificate(alias);
             if (certs != null) {
                 log.debug("DeceClientKeyManager:init:Certificate chain '" + alias + "':");
                // for (int c = 0; c < certs.length; c++) {
                     if (certs instanceof X509Certificate) {
                         X509Certificate cert = (X509Certificate)certs;
                         log.debug(" Certificate "  + ":");
                         log.debug("  Subject DN: " + cert.getSubjectDN());
                         log.debug("  Signature Algorithm: " + cert.getSigAlgName());
                         log.debug("  Valid from: " + cert.getNotBefore() );
                         log.debug("  Valid until: " + cert.getNotAfter());
                         log.debug("  Issuer: " + cert.getIssuerDN());
                    // }
                 }
             }
         }
		
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(SECURE_ALGORITHM);
        if(keyPassword==null)
        	kmf.init(ks,null);
        else
        kmf.init(ks,keyPassword.toCharArray());
        kman = kmf.getKeyManagers();
	}
	public DeceClientKeyManager(URL keyStoreFilename,
            String keyStorePassword, String keyPassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException{
		

		
		this(keyStoreFilename.getFile(),keyStorePassword,keyPassword);
	}
	/**
	 * @return the kman
	 */
	public KeyManager[] getKman() {
		return kman;
	}
	/**
	 * @param kman the kman to set
	 */
	public void setKman(KeyManager[] kman) {
		this.kman = kman;
	}
	
}
