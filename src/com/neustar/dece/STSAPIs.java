package com.neustar.dece;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class STSAPIs {

	static Logger log = Logger.getLogger(STSAPIs.class);
	private static String assertion;
	private static String finalAssertion;
	public static void executeSTSCreate(String url, String xmlData, HttpClient httpclient){
		try{
	        //System.out.println("***********Starting STSCreate***********************");
	        log.info("****Starting STSCreate*****");
            //url = "https://qa.p.uvvu.com:7001/rest/1/0/SecurityToken/SecurityTokenExchange?tokentype=urn:dece:type:tokentype:usernamepassword";
	        //System.out.println("STSCreate URL:" + url);
	        PostMethod httppost = CreateRequest.getPostRequest(url, xmlData, httpclient);
	        httpclient.executeMethod(httppost);  
	        //parse the location and get the assertion
            String assertionLocation = httppost.getResponseHeader("Location").getValue();
            //get the assertion
            String assertion = assertionLocation.substring(assertionLocation.lastIndexOf("/")+1, assertionLocation.length());
	        log.info(assertion);
	        STSAPIs.setAssertion(assertion);
	        
	        CreateRequest.printResponse(httppost);
	        httppost.releaseConnection();
	        log.info("****completed STSCreate*****");
	        //System.out.println("completed STSCreate");
		}
		catch(Exception e){
			System.out.println(e.toString());
			log.error("STSAPIs:executeSTSCreate:IO Exception",e);
		}
	}	
	
	public static void executeSTSGet(String url, HttpClient httpclient, String base64UserPass){
		try{
	        //System.out.println("***********Starting STSGet***********************");
	        log.info("****Starting STSGet*****");
            //url = "https://qa.p.uvvu.com:7001/rest/1/0/SecurityToken/SecurityTokenExchange?tokentype=urn:dece:type:tokentype:usernamepassword";
	        log.info("STSGet URL:" + url);
	        GetMethod httpget = CreateRequest.getGetRequest(url, httpclient);
	        //httpget.addRequestHeader("Authorization", "Basic "+ base64UserPass);
	        httpclient.executeMethod(httpget);  
            //get the final assertion	        
	        CreateRequest.printResponse(httpget);
	        System.out.println("rawXML="+ httpget.getResponseBodyAsString());
	        if(httpget.getStatusCode() == 200){
	        	
	        	String finalAssertion = SAMLDeflateAndEncode.getDeflateAndEncodedString(httpget.getResponseBodyAsString());
	        	log.info("final assertion:" + finalAssertion);
	        	STSAPIs.setFinalAssertion(finalAssertion);
	        	httpget.releaseConnection();
	        	log.info(STSAPIs.getfinalAssertion());
	        	log.info("****completed STSGet*****");
	        	//System.out.println("completed STSGet");
	        }
	        else {
		        log.error("****" + " STSGet has some errors. Please check UV.log*****");
		        httpget.releaseConnection();
	        }
	        
		}
		catch(IOException e){
			//System.out.println(e.toString());
			log.error("STSAPIs:executeSTSGet:IO Exception",e);
		}
	}	
	
	public static String getAssertion(){
		return assertion;
	}
	
	public static void setAssertion(String assertion){
		STSAPIs.assertion = assertion;
	}
	
	public static String getfinalAssertion(){
		return finalAssertion;
	}
	
	public static void setFinalAssertion(String finalAssertion){
		STSAPIs.finalAssertion = finalAssertion;
	}
}
