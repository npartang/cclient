package com.neustar.dece;


	import java.io.*;
	import java.net.*;
	import javax.net.ssl.*;
	import java.security.*;
	import java.nio.*;
	import javax.net.SocketFactory;
	import javax.net.ssl.SSLContext;
	import javax.net.ssl.TrustManager;
	
//	import org.apache.commons.httpclient.ConnectTimeoutException;
//	import org.apache.commons.httpclient.HttpClientError;
//	import org.apache.commons.httpclient.params.HttpConnectionParams;
//	import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
	//import org.apache.commons.logging.Log;
	//import org.apache.commons.logging.LogFactory; 

	
	public class SSLConnect {
		
		//private static final Log LOG = LogFactory.getLog(SSLConnect.class);
		private SSLContext sslcontext = null; 
		private static char keystorepass[] = "decetest".toCharArray();
		private SSLContext context;
		//private static SSLEngine sslEngine; 

		

		
		private void createSSLContext() {
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream("C:\\Program Files\\Java\\jdk1.6.0_23\\jre\\lib\\security\\decekeystore.jks"), keystorepass);
				KeyManagerFactory kmf = 
				    KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, keystorepass);
				SSLContext sslContext = SSLContext.getInstance("SSL");
				
				sslContext.init(kmf.getKeyManagers(), null, null); 				
				context = sslContext;
				//sslEngine = sslContext.createSSLEngine();
				
			}
			catch (Exception e) {
				//LOG.error(e.getMessage(), e);
				//throw new HttpClientError(e.toString()); 
			}
		
		}
		
		private  SSLContext getSSLContext() {
				if (context == null) {
					createSSLContext();
			 	}
				return context;
		} 

	public SSLSocket createSocket(String host, int port) throws IOException,
			UnknownHostException {			
		//SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		//SSLServerSocketFactory sf = this.getSSLContext().getServerSocketFactory();
		SSLSocketFactory sf = this.getSSLContext().getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sf.createSocket(host, port);
		//SSLServerSocket sslSocket = (SSLServerSocket) sf.createServerSocket();

		//sslSocket.bind(new InetSocketAddress( (InetAddress.getByName(host)), port)); 
		//verifyHostname(sslSocket);
		return sslSocket; 			
	}
	public SSLSocketFactory createSocketFactory() throws IOException,
		UnknownHostException {			
		//SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		//SSLServerSocketFactory sf = this.getSSLContext().getServerSocketFactory();
		SSLSocketFactory sf = this.getSSLContext().getSocketFactory();			
		return sf; 			
	}
}
