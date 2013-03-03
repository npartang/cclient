package com.neustar.dece;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;



public class SSLProtocolSocketFactory implements SecureProtocolSocketFactory {

	static Logger log = Logger.getLogger(SSLProtocolSocketFactory.class);
	
	//private final static Logger LOGGER = Logger.getLogger(DeceClientSSLProtocolSocketFactory.class.getCanonicalName());
	//private final Logger log=Logger.getLogger("com.neustar.mids.appliances.MidsSSLProtocolSocketFactory");
	
	private SSLContext sslcontext = null;
	private URL keystoreUrl = null;
	private String keystorePassword = null;
	private String keyPassword=null;
	
	//private static final String SECURE_ALGORITHM = "SunX509";
	private static final String SECURE_PROTOCOL = "TLS";
	//private static final String KEYSTORE_TYPE = "JKS";
	
	private KeyManager[] keyManager=null;
	private TrustManager[] trustManager=null;
	
	/**
	 * @return the trustManager
	 */
	public TrustManager[] getTrustManager() {
		if(this.trustManager==null){
			DeceClientTrustManager deceClientTrustManager=new DeceClientTrustManager();
			DeceClientTrustManager[] midsTrustMan={deceClientTrustManager};
			this.trustManager=(TrustManager[])midsTrustMan;
			
		}
		return this.trustManager;
	}
	/**
	 * @param trustManager the trustManager to set
	 */
	public void setTrustManager(TrustManager[] trustManager) {
		this.trustManager = trustManager;
	}
	public SSLProtocolSocketFactory(URL keystoreUrl,
			String keystorePassword, String keyPassword,boolean verifyHostname) {
		super();
		this.keystoreUrl = keystoreUrl;
		this.keystorePassword = keystorePassword;
		this.keyPassword=keyPassword;
		this.verifyHostname = verifyHostname;
		
	}

	
	
	private boolean verifyHostname = true;
	
	/**
     * Constructor for MIDSSSLProtocolSocketFactory.
     */
    public SSLProtocolSocketFactory() {
        super();
    }
    private void getKeyManagers() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
    	DeceClientKeyManager deceClientKeyManager=new DeceClientKeyManager(this.keystoreUrl,this.keystorePassword,this.keyPassword);
    	this.setKeyManager(deceClientKeyManager.getKman());
    }
    /**
	 * @return the keyManager
     * 
     * @throws CertificateException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyStoreException 
     * @throws UnrecoverableKeyException 
     * @throws IOException 
	 */
	public KeyManager[] getKeyManager() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		if(this.keyManager==null){
			this.getKeyManagers();
		}
		
		return this.keyManager;
	}
	/**
	 * @param keyManager the keyManager to set
	 */
	public void setKeyManager(KeyManager[] keyManager) {
		this.keyManager = keyManager;
	}
	private  SSLContext createMIDSSSLContext() throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException {
		 SSLContext sslcontext = SSLContext.getInstance(this.SECURE_PROTOCOL);
         
		 log.debug("Key manager length="+this.getKeyManager().length);
		 
		 sslcontext.init(this.getKeyManager(), this.getTrustManager(), null);
         
         return sslcontext;
	}
        

	 

	
	public Socket createSocket(Socket socket,
	        String host,
	        int port,
	        boolean autoClose)
			throws IOException, UnknownHostException {
		     
		try {
			//log.info("DeceClientSSLProtocolSocketFactory::createSocket:Executing  method ");
			return getSslcontext().getSocketFactory().createSocket(
			        socket,
			        host,
			        port,
			        autoClose
			    );
			
		} catch (KeyManagementException e) {
			log.error("SSLProtocolSocketFactory::createSocket:Key Management Exception",e);
			throw new IOException(e);
		} catch (UnrecoverableKeyException e) {
			log.error("SSLProtocolSocketFactory::createSocket:Unrecoverable Key Exception",e);
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			log.error("SSLProtocolSocketFactory::createSocket:No Such Algorith Exception",e);
			throw new IOException(e);
		} catch (KeyStoreException e) {
			log.error("SSLProtocolSocketFactory::createSocket:Key Store Exception",e);
			throw new IOException(e);
		} catch (CertificateException e) {
			log.error("SSLProtocolSocketFactory::createSocket:Certificate Exception",e);
			throw new IOException(e);
		}

	}

	
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		
		try {
			log.debug("MidsSSLProtocolSocketL::createSocket:Executing this method");
			return getSslcontext().getSocketFactory().createSocket(
			        host,
			        port
			        
			    );
		} catch (KeyManagementException e) {
			log.error("Key Management Exception",e);
			throw new IOException(e);
		} catch (UnrecoverableKeyException e) {
			log.error("Unrecoverable Key Exception",e);
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			log.error("No Such Algorith Exception",e);
			throw new IOException(e);
		} catch (KeyStoreException e) {
			log.error("Key Store Exception",e);
			throw new IOException(e);
		} catch (CertificateException e) {
			log.error("Certificate Exception",e);
			throw new IOException(e);
		}
	}

	
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
			throws IOException, UnknownHostException {
		
		  try {
			log.debug("MidsSSLProtocolSocket::crateSocket:Executing this method2");
			return getSslcontext().getSocketFactory().createSocket(
			        host,
			        port,
			        clientHost,
			        clientPort
			    );
		} catch (KeyManagementException e) {
			log.error("Key Management Exception",e);
			throw new IOException(e);
		} catch (UnrecoverableKeyException e) {
			log.error("Unrecoverable Key Exception",e);
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			log.error("No Such Algorith Exception",e);
			throw new IOException(e);
		} catch (KeyStoreException e) {
			log.error("Key Store Exception",e);
			throw new IOException(e);
		} catch (CertificateException e) {
			log.error("Certificate Exception",e);
			throw new IOException(e);
		}

	}

	
	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		

		
		if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
		
        int timeout = params.getConnectionTimeout();
        log.debug("Executing this method3 with timeout="+timeout);
        timeout=100000;
        SocketFactory socketfactory;
		try {
			socketfactory = getSslcontext().getSocketFactory();
		} catch (KeyManagementException e) {
			log.error("Key Management Exception",e);
			throw new IOException(e);
		} catch (UnrecoverableKeyException e) {
			log.error("Unrecoverable Key Exception",e);
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			log.error("No Such Algorith Exception",e);
			throw new IOException(e);
		} catch (KeyStoreException e) {
			log.error("Key Store Exception",e);
			throw new IOException(e);
		} catch (CertificateException e) {
			log.error("Certificate Exception",e);
			throw new IOException(e);
		}
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = (SocketAddress) new InetSocketAddress(host, port);
       
        
            //socket.bind(new java.net.InetSocketAddress(0));
            socket.bind(localaddr);
            //System.out.println(socket.getLocalAddress().toString());;
            //System.out.println(socket.getLocalPort());
            socket.connect(remoteaddr, timeout);
            //System.out.println("after loacaddr bind");
            //System.out.println(socket.getInetAddress());
            String[] cipherSuites=((SSLSocket)socket).getEnabledCipherSuites();
            /*for(int i=0;i<cipherSuites.length;i++){
            	LOGGER.finest("MidsSSLPorotoclSocket::createSocket::"+cipherSuites[i]);
            }*/
            String[] ciphers={"TLS_DHE_RSA_WITH_AES_128_CBC_SHA"};
            ((SSLSocket)socket).setEnabledCipherSuites(ciphers);
   
           
            return socket;
        }

	}
	/**
	 * @return the sslcontext
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnrecoverableKeyException 
	 * @throws KeyManagementException 
	 */
	public SSLContext getSslcontext() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		if (this.sslcontext == null) {
            this.sslcontext = this.createMIDSSSLContext();
        }
        return this.sslcontext;

	}
	/**
	 * @param sslcontext the sslcontext to set
	 */
	public void setSslcontext(SSLContext sslcontext) {
		this.sslcontext = sslcontext;
		
	}
	/**
     * Describe <code>verifyHostname</code> method here.
     *
     * @param socket a <code>SSLSocket</code> value
     * @exception SSLPeerUnverifiedException  If there are problems obtaining
     * the server certificates from the SSL session, or the server host name 
     * does not match with the "Common Name" in the server certificates 
     * SubjectDN.
     * @exception UnknownHostException  If we are not able to resolve
     * the SSL sessions returned server host name. 
     */
    private void verifyHostname(SSLSocket socket) 
        throws SSLPeerUnverifiedException, UnknownHostException {
        if (! verifyHostname) 
            return;

        SSLSession session = socket.getSession();
        String hostname = session.getPeerHost();
        try {
            InetAddress addr = InetAddress.getByName(hostname);
        } catch (UnknownHostException uhe) {
            throw new UnknownHostException("Could not resolve SSL sessions "
                                           + "server hostname: " + hostname);
        }
        
        X509Certificate[] certs = session.getPeerCertificateChain();
        if (certs == null || certs.length == 0) 
            throw new SSLPeerUnverifiedException("No server certificates found!");
        
        //get the servers DN in its string representation
        String dn = certs[0].getSubjectDN().getName();

        //might be useful to print out all certificates we receive from the
        //server, in case one has to debug a problem with the installed certs.
        if (log.isEnabledFor(Level.DEBUG)) {
        	log.debug("Server certificate chain:");
            for (int i = 0; i < certs.length; i++) {
            	log.debug("X509Certificate[" + i + "]=" + certs[i]);
            }
        }
        //get the common name from the first cert
        String cn = getCN(dn);
        if (hostname.equalsIgnoreCase(cn)) {
            if (log.isEnabledFor(Level.DEBUG)) {
            	log.debug("Target hostname valid: " + cn);
            }
        } else {
            throw new SSLPeerUnverifiedException(
                "HTTPS hostname invalid: expected '" + hostname + "', received '" + cn + "'");
        }
    }
    /**
     * Parses a X.500 distinguished name for the value of the 
     * "Common Name" field.
     * This is done a bit sloppy right now and should probably be done a bit
     * more according to <code>RFC 2253</code>.
     *
     * @param dn  a X.500 distinguished name.
     * @return the value of the "Common Name" field.
     */
    private String getCN(String dn) {
        int i = 0;
        i = dn.indexOf("CN=");
        if (i == -1) {
            return null;
        }
        //get the remaining DN without CN=
        dn = dn.substring(i + 3);  
        // System.out.println("dn=" + dn);
        char[] dncs = dn.toCharArray();
        for (i = 0; i < dncs.length; i++) {
            if (dncs[i] == ','  && i > 0 && dncs[i - 1] != '\\') {
                break;
            }
        }
        return dn.substring(0, i);
    }
    public boolean equals(Object obj) {
        if ((obj != null) && obj.getClass().equals(SSLProtocolSocketFactory.class)) {
            return ((SSLProtocolSocketFactory) obj).getHostnameVerification() 
                == this.verifyHostname;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return SSLProtocolSocketFactory.class.hashCode();
    }

    /**
     * Gets the status of the host name verification flag.
     *
     * @return  Host name verification flag.  Either <code>true</code> if host
     * name verification is turned on, or <code>false</code> if host name
     * verification is turned off.
     */
    public boolean getHostnameVerification() {
        return verifyHostname;
    }


}
