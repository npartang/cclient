package com.neustar.dece;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.nio.*;
import java.util.*;
//import java.lang.*;


import java.security.KeyStore;
import java.security.cert.CertificateException;

//import org.apache.commons.*;
import javax.net.ssl.*;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.http.*;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
//import org.apache.http.conn.ssl.*;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Header;
import java.nio.channels.SocketChannel;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.protocol.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.scheme.*;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory; 
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
public class CreateRequest {
	
	static Logger log = Logger.getLogger(CreateRequest.class);	
	

	
	public static PostMethod getPostRequest (String url, String xmlString, HttpClient httpclient){
		PostMethod httppost = new PostMethod();
		try{			
	        RequestEntity entity = new ByteArrayRequestEntity(xmlString.getBytes()); 
	        httppost.setRequestEntity(entity);
	        httppost.setURI(new URI(url, true));
	        httppost.setRequestHeader("Content-Type", "application/xml");	
	        //httppost.setRequestHeader("User-Agent", "Jakarta Commons-HttpClient/3.1");
            //httppost.setRequestHeader("Host", "qa.p.uvvu.com:7001");
            //httppost.setRequestHeader("Content-Length", "249");
	        //httppost.setRequestHeader("Accept-Encoding", "gzip,deflate");
		}
		catch(IOException e){
			log.error("Error creating Post Request");
			log.error("CreateRequest:getPostRequest:Error creating Post Request",e);
			e.printStackTrace();
		}
		return httppost;
	}
	public static GetMethod getGetRequest (String url, HttpClient httpclient){
		GetMethod httpget = new GetMethod();
		try{			
	        //RequestEntity entity = new ByteArrayRequestEntity(xmlString.getBytes()); 
	        //httppost.setRequestEntity(entity);
	        httpget.setURI(new URI(url, true));
	        httpget.setRequestHeader("Content-Type", "application/xml");	
	        //httppost.setRequestHeader("User-Agent", "Jakarta Commons-HttpClient/3.1");
            //httppost.setRequestHeader("Host", "qa.p.uvvu.com:7001");
            //httppost.setRequestHeader("Content-Length", "249");
	        //httppost.setRequestHeader("Accept-Encoding", "gzip,deflate");
		}
		catch(IOException e){
			log.error("Error creating Get Request");
			log.error("CreateRequest:getGetRequest:Error creating Get Request",e);
			e.printStackTrace();
		}
		return httpget;
	}
	
	
	public static PutMethod getUpdateRequest (String url, String xmlString, HttpClient httpclient){
        PutMethod httpupdate = new PutMethod();
		
        try{
	        RequestEntity entity = new ByteArrayRequestEntity(xmlString.getBytes()); 
	        httpupdate.setRequestEntity(entity);
        	httpupdate.setURI(new URI(url, true));
        	httpupdate.setRequestHeader("Content-Type", "application/xml");
        }
		catch(IOException e){
			log.error("Error creating Update (Put) Request");
			log.error("CreateRequest:getUpdateRequest:Error creating Update Request",e);
			e.printStackTrace();
		}
		return httpupdate;
	}
	
	
	public static DeleteMethod getDeleteRequest (String url, HttpClient httpclient){
		DeleteMethod httpdelete = new DeleteMethod();
		try{			
	        //RequestEntity entity = new ByteArrayRequestEntity(xmlString.getBytes()); 
	        //httppost.setRequestEntity(entity);
	        httpdelete.setURI(new URI(url, true));
	        httpdelete.setRequestHeader("Content-Type", "application/xml");	
	        //httppost.setRequestHeader("User-Agent", "Jakarta Commons-HttpClient/3.1");
            //httppost.setRequestHeader("Host", "qa.p.uvvu.com:7001");
            //httppost.setRequestHeader("Content-Length", "249");
	        //httppost.setRequestHeader("Accept-Encoding", "gzip,deflate");
		}
		catch(IOException e){
			log.error("Error creating Delete Request");
			log.error("CreateRequest:getDeleteRequest:Error creating Delete Request",e);
			e.printStackTrace();
		}
		return httpdelete;
	}
	
	public static HeadMethod getHeadRequest (String url, HttpClient httpclient){
		HeadMethod httphead = new HeadMethod();
		try{			
	        //RequestEntity entity = new ByteArrayRequestEntity(xmlString.getBytes()); 
	        //httppost.setRequestEntity(entity);
	        httphead.setURI(new URI(url, true));
	        httphead.setRequestHeader("Content-Type", "application/xml");	
	        //httppost.setRequestHeader("User-Agent", "Jakarta Commons-HttpClient/3.1");
            //httppost.setRequestHeader("Host", "qa.p.uvvu.com:7001");
            //httppost.setRequestHeader("Content-Length", "249");
	        //httppost.setRequestHeader("Accept-Encoding", "gzip,deflate");
		}
		catch(IOException e){
			log.error("Error creating Head Request");
			log.error("CreateRequest:getHeadRequest:Error creating Head Request",e);
			e.printStackTrace();
		}
		return httphead;
	}
	public static void printResponse(PostMethod httppost){
		
		try{
			
	        log.info("message = " + httppost.getResponseBodyAsString());
	        log.info("status code = " + httppost.getStatusCode());
	        log.info("status text = " + httppost.getStatusText());
	        log.info(httppost.getStatusLine().toString());
	        //Header [] respHeaders =  httppost.getResponseHeaders();
	        //for(int i=0; i<respHeaders.length; i++){	        	
	        	//log.info("Header Name = " + respHeaders[i].getName() + " Header Value = " + respHeaders[i].getValue());	        	
	        //}
		}
		catch(IOException e){
			log.debug("Error printing response");
			log.error("CreateRequest:printResponse:Error printing response",e);
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void printResponse(GetMethod httpget){
		
		try{
			
	        log.info("message = " + httpget.getResponseBodyAsString());
	        log.info("status code = " + httpget.getStatusCode());
	        log.info("status text = " + httpget.getStatusText());
	        log.info(httpget.getStatusLine().toString());
	        //Header [] respHeaders =  httpget.getResponseHeaders();
	        //for(int i=0; i<respHeaders.length; i++){	        	
	        	//log.debug("Header Name = " + respHeaders[i].getName() + " Header Value = " + respHeaders[i].getValue());	        	
	        //}
		}
		catch(IOException e){
			log.error("CreateRequest:printResponse:Error printing response",e);
			e.printStackTrace();
		}
	}
	
	
	public static void printResponse(PutMethod httpupdate){
		
		try{
			
	        log.info("message = " + httpupdate.getResponseBodyAsString());
	        log.info("status code = " + httpupdate.getStatusCode());
	        log.info("status text = " + httpupdate.getStatusText());
	        log.info(httpupdate.getStatusLine().toString());
	        //Header [] respHeaders =  httpget.getResponseHeaders();
	        //for(int i=0; i<respHeaders.length; i++){	        	
	        	//log.debug("Header Name = " + respHeaders[i].getName() + " Header Value = " + respHeaders[i].getValue());	        	
	        //}
		}
		catch(IOException e){
			log.error("CreateRequest:printResponse:Error printing response",e);
			e.printStackTrace();
		}
	}
	
	public static void printResponse(DeleteMethod httpdelete){
		
		try{
			
	        log.info("message = " + httpdelete.getResponseBodyAsString());
	        log.info("status code = " + httpdelete.getStatusCode());
	        log.info("status text = " + httpdelete.getStatusText());
	        log.info(httpdelete.getStatusLine().toString());
	        //Header [] respHeaders =  httpget.getResponseHeaders();
	        //for(int i=0; i<respHeaders.length; i++){	        	
	        	//log.debug("Header Name = " + respHeaders[i].getName() + " Header Value = " + respHeaders[i].getValue());	        	
	        //}
		}
		catch(IOException e){
			log.error("CreateRequest:printResponse:Error printing response",e);
			e.printStackTrace();
		}
	}
	
	public static void printResponse(HeadMethod httphead){
		
		try{
			
	        log.info("message = " + httphead.getResponseBodyAsString());
	        log.info("status code = " + httphead.getStatusCode());
	        log.info("status text = " + httphead.getStatusText());
	        log.info(httphead.getStatusLine().toString());
	        //Header [] respHeaders =  httpget.getResponseHeaders();
	        //for(int i=0; i<respHeaders.length; i++){	        	
	        	//log.debug("Header Name = " + respHeaders[i].getName() + " Header Value = " + respHeaders[i].getValue());	        	
	        //}
		}
		catch(IOException e){
			log.error("CreateRequest:printResponse:Error printing response",e);
			e.printStackTrace();
		}
	}

}
