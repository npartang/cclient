package com.neustar.dece;
import java.io.*;
//import java.net.*;
//import javax.net.ssl.*;
//import java.security.*;
//import java.nio.*;
//import java.util.*;
//import java.lang.*;
//import java.security.KeyStore;
//import java.security.cert.CertificateException;
//import org.apache.commons.*;/
//import javax.net.ssl.*;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.http.*;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import javax.net.SocketFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import org.apache.http.conn.ssl.*;
import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.Header;
//import java.nio.channels.SocketChannel;
//import org.apache.commons.httpclient.URI;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.protocol.*;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.conn.scheme.*;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory; 
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class APIs {
	
	static Logger log = Logger.getLogger(APIs.class);
	private static String accountOID;
	private static String samlAssertion;

	public static void executeCreate(String url, String xmlData, HttpClient httpclient, String api){
		try{
	        log.log(Level.DEBUG, "****Starting " + api + "*****");
	        //url = "https://qa.p.uvvu.com:"+port+"/rest/1/0/Provision/Account";
	        log.info(api + " URL:" + url);
	        PostMethod httppost = CreateRequest.getPostRequest(url, xmlData, httpclient);
	        httpclient.executeMethod(httppost); 
	        CreateRequest.printResponse(httppost);
	        if(httppost.getStatusCode() == 201){
		        String location = httppost.getResponseHeader("Location").getValue();
		        if (api.equals("AccountCreate")){
		        	String accountOID = location.substring(location.lastIndexOf("/")+1, location.length());
			        log.debug("accountOID="+ accountOID);
			        APIs.setAccountOID(accountOID);
		        }
		        log.info("location="+location);
	        
		        log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httppost.releaseConnection();
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
	}
	
	
	public static void executeCreate(String url, String xmlData, HttpClient httpclient, String saml, String api){
		try{
	        log.log(Level.DEBUG, "****Starting " + api + "*****");
	        //url = "https://qa.p.uvvu.com:"+port+"/rest/1/0/Provision/Account";
	        log.info(api + " URL:" + url);
	        PostMethod httppost = CreateRequest.getPostRequest(url, xmlData, httpclient);
	        httppost.addRequestHeader("Authorization", "SAML2 assertion="+ saml);
	        httpclient.executeMethod(httppost); 
	        CreateRequest.printResponse(httppost);
	        if(httppost.getStatusCode() == 201){
		        String location = httppost.getResponseHeader("Location").getValue();
		        if (api.equals("AccountCreate")){
		        	String accountOID = location.substring(location.lastIndexOf("/")+1, location.length());
			        log.debug("accountOID="+ accountOID);
			        APIs.setAccountOID(accountOID);
		        }
		        log.info("location="+location);
	        
		        log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httppost.releaseConnection();
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
	}
	
	public static void executeGet(String url, HttpClient httpclient, String saml, String api){
		
		try{
			log.log(Level.DEBUG, "****Starting " + api + "*****");
			log.info(api+" URL:" + url);
			GetMethod httpget = CreateRequest.getGetRequest(url, httpclient);
			httpget.addRequestHeader("Authorization", "SAML2 assertion="+ saml);
			httpclient.executeMethod(httpget);
			CreateRequest.printResponse(httpget);
	        if(httpget.getStatusCode() == 200){
	        	System.out.println(httpget.getResponseBodyAsString());   
	        	 log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httpget.releaseConnection();
			
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
		
	}
	
	public static void executeUpdate(String url, String xmlData, HttpClient httpclient, String saml, String api){
		
		try{
			log.log(Level.DEBUG, "****Starting " + api + "*****");
			log.info(api+" URL:" + url);
			PutMethod httpupdate = CreateRequest.getUpdateRequest(url, xmlData, httpclient);
			httpupdate.addRequestHeader("Authorization", "SAML2 assertion="+ saml);
			httpclient.executeMethod(httpupdate);
			CreateRequest.printResponse(httpupdate);
	        if(httpupdate.getStatusCode() == 200){
	        	System.out.println(httpupdate.getResponseBodyAsString());   
	        	 log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httpupdate.releaseConnection();
			
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
		
	}
	
	public static void executeDelete(String url, HttpClient httpclient, String saml, String api){
		
		try{
			log.log(Level.DEBUG, "****Starting " + api + "*****");
			log.info(api+" URL:" + url);
			DeleteMethod httpdelete = CreateRequest.getDeleteRequest(url, httpclient);
			httpdelete.addRequestHeader("Authorization", "SAML2 assertion="+ saml);
			httpclient.executeMethod(httpdelete);
			CreateRequest.printResponse(httpdelete);
	        if(httpdelete.getStatusCode() == 200){
	        	System.out.println(httpdelete.getResponseBodyAsString());   
	        	log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httpdelete.releaseConnection();
			
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
		
	}
	
	public static void executeHead(String url, HttpClient httpclient, String api){
		
		try{
			log.log(Level.DEBUG, "****Starting " + api + "*****");
			log.info(api+" URL:" + url);
			HeadMethod httphead = CreateRequest.getHeadRequest(url, httpclient);
			httpclient.executeMethod(httphead);
			CreateRequest.printResponse(httphead);
	        if(httphead.getStatusCode() == 200){
	        	System.out.println(httphead.getResponseBodyAsString());   
	        	log.info( "****completed " + api + "*****");
	        }
	        
	        else log.error("****" + api+ " has some errors. Please check UV.log*****");
	        httphead.releaseConnection();
			
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("API:" + api+ ":IO Exception",e);
		}
		
	}
	
	public static String getAccountOID(){
		return accountOID;
	}
	
	public static void setAccountOID(String accountOID){
		APIs.accountOID = accountOID;
	}
	
	public static String getSamlAssertion(){
		return samlAssertion;
	}
	
	public static void setSamlAssertion(String samlAssertion){
		APIs.samlAssertion= samlAssertion;
	}
}
