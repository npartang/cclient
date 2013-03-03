/************************************************************
 * Author: Narayan Partangel
 * Description: Driver for the coordinator platform
 */


package com.neustar.dece;

import java.io.*;
import java.util.*;
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
import org.apache.log4j.PropertyConfigurator;

@SuppressWarnings("serial")

public class UVDriver  {
	static Logger log = Logger.getLogger(UVDriver.class);	
@SuppressWarnings("deprecation")
	public static void main (String args[])  {
	
		//BasicConfigurator.configure();
		PropertyConfigurator.configure("log4j.properties");

		log.info("Entering UVDriver");
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(args[0]);
		}
		catch(FileNotFoundException e){
			System.out.println(e.toString());
		}
		PropertiesUtil.load(fis);
		String hostName = ConfigProperties.getHostname();
		int port = Integer.parseInt(ConfigProperties.getPort());
		File keyFile = new File(ConfigProperties.getKeyStoreFile());
		File scenarioFile = new File(ConfigProperties.getScenarioFile());
		String keyStorePassword = ConfigProperties.getKeyStorePassword();
		String xmlPath = ConfigProperties.getXmlPath();

		HttpConnection httpConn = null;
		//test for invoking REST API		
		String xmlData = "";
        PostMethod httppost;
        GetMethod httpget;
        String url, line;
        InputStream is;
        BufferedReader br;
        ArrayList scenarios;
        HashMap apiHash = null;
        ArrayList indScenario = null;
        HttpClient httpclient = new HttpClient();
        int counter=1;
        try {        	        	        	
        	SSLProtocolSocketFactory sslFactory=new SSLProtocolSocketFactory(keyFile.toURI().toURL(),keyStorePassword,keyStorePassword,false);
        	Protocol https = new Protocol("https", sslFactory, port);
            Protocol.registerProtocol("https",new Protocol("https", sslFactory, port));            
            httpclient.getHostConfiguration().setHost(hostName, port, https);
            System.out.println(APIs.getAccountOID());
            log.info("UVDriver:main:HttpClient created");
            
            //run through the scenarios
    		is = new FileInputStream(scenarioFile);
    		br = new BufferedReader(new InputStreamReader(is));
    		scenarios = new ArrayList();
    		while ( ((line = br.readLine()) != null) )  { 
    			if(line.startsWith("#")) continue;
    			if(line.startsWith("Scenario")){
    				indScenario = new ArrayList();
    				//add the scenario name
    				indScenario.add(0, line.substring(line.indexOf(":")+1,line.length()));
    				scenarios.add(indScenario);
    				counter=1;    								
    			}
    			//add the API name as the second parameter
    			if(line.startsWith("API")){
    					apiHash = new HashMap();    
    					apiHash.put(line.substring(0, line.indexOf(":")), line.substring(line.indexOf(":")+1,line.length()));
    					indScenario.add(counter, apiHash);
    					counter++;
    			}
    			if(line.startsWith("P:")){    					
    					apiHash.put(line.substring(line.indexOf(":")+1, line.indexOf(" ")), line.substring(line.indexOf(" ")+1,line.length()));
    			}
    		}    		
    		//print all the scenarios
    		Iterator it = scenarios.iterator();
    		//System.out.println(scenarios.size());
    		while(it.hasNext()){
    			ArrayList al = (ArrayList)it.next();
    			log.info("###############Currently implementing scenario:"+al.get(0));
    			//reset all the variables which were set during previous scenarios
    			ResetVariables.resetVariables();
    			for(int i=1; i< al.size(); i++){
	    			HashMap hm = (HashMap)al.get(i);
	    			log.info("Currently implementing API:"+hm.get("API"));
	    			//execute the API
	    			if(hm.get("API").equals("STSCreate")){
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getStsCreateurl();
	    	            log.info("STSCreate URL:" + url);
	    	            try{
	    	            	if (!hm.get("STSCreateXml").equals(null))xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+ hm.get("STSCreateXml"));
	    	            }
	    	            catch (NullPointerException npe) {
	    	            	log.error("STS Create XML file not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	
	    	            	break;
	    	            }
	    	            STSAPIs.executeSTSCreate(url, xmlData, httpclient);
	    	            //APIs.executeCreate(url, xmlData, httpclient, "STSCreate");
	    			}
	    			else if (hm.get("API").equals("STSGet")){
	    	            log.info( "****Starting STSGet*****");
	    	            url = ConfigProperties.getbasequeryurl()+ ":" + port + ConfigProperties.getStsGeturl()+ "/"+ STSAPIs.getAssertion();
	    	            //url =  "https://qa.p.uvvu.com:7001/rest/1/0/SecurityToken/Assertion/" + assertion;
	    	            if( (!hm.get("Username").equals(null)) || (!hm.get("Password").equals(null)) ) {
	    	            	String base64UserPass = SAMLDeflateAndEncode.getBase64UserPass((String)(hm.get("Username")), (String)hm.get("Password"));	    	            
	    	            	System.out.println("base64userpass = " + base64UserPass);
	    	            	STSAPIs.executeSTSGet(url, httpclient, base64UserPass);
	    	            	log.info("STSGet URL:" + url);
	    	            }
	    	            //APIs.executeGet(url, httpclient, (String)hm.get("SamlAssertion"), "STSGet");
	    			}
	    			else if (hm.get("API").equals("AccountCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getaccountcreateurl();
	    	            log.info("AccountCreate URL:" + url);
	    	            try{
	    	            	if(!hm.get("AccountCreateXml").equals(null)) xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("AccountCreateXml"));
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("Account Create XML file not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            APIs.executeCreate(url, xmlData, httpclient, "AccountCreate");
	    			}
	    			else if (hm.get("API").equals("AccountGet")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getaccountgeturl()+ hm.get("AccountOID");	  
	    						System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("AccountGet URL:" + url);
	    	            APIs.executeGet(url, httpclient, (String)hm.get("SamlAssertion"), "AccountGet");
	    			}
	    			else if (hm.get("API").equals("AccountUpdate")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("AccountUpdateXml").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getaccountupdateurl()+ hm.get("AccountOID");	  
	    						System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    						xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("AccountUpdateXml"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or SAML Assertion or xml file not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("AccountUpdate URL:" + url);
	    	            APIs.executeUpdate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "AccountUpdate");
	    			}
	    			else if (hm.get("API").equals("AccountDelete")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getaccountdeleteurl()+ hm.get("AccountOID");	  
	    						System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or SAML Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("AccountDelete URL:" + url);
	    	            APIs.executeDelete(url, httpclient, (String)hm.get("SamlAssertion"), "AccountDelete");
	    			}
	    			else if (hm.get("API").equals("PolicyCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;	    	            	    	            
	    	            try{
	    	            	if( (!hm.get("PolicyCreateXml").equals(null)) || (!hm.get("AccountOID").equals(null)) || (!hm.get("PolicyID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) ) {
		    	            	xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("PolicyCreateXml"));
		    	            	
		    	            	if (!hm.containsKey("UserOID")) {
		    	            		url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getaccountpolicycreateurl()+ hm.get("PolicyID");
		    	            		url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
		    	            	}
		    	            	else{
		    	            		url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getuserpolicycreateurl()+ hm.get("PolicyID");
		    	            		url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
		    	            		url = url.replaceAll("<replaceuser>", (String)hm.get("UserOID"));
		    	            	}
		    	            	
		    	            	//replace account 
		    	            	
		    	            	log.info("PolicyCreate URL:" + url);
		    	            	APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    	            	}
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("PolicyCreate XML file or PolicyID or AccountOID or SamlAssertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            APIs.executeCreate(url, xmlData, httpclient,APIs.getSamlAssertion(), "PolicyCreate");
	    			}
	    			else if (hm.get("API").equals("FirstUserCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getusercreateurl();
	    	            log.info("UserCreate URL:" + url);
	    	            try{
	    	            	if(!hm.get("UserCreateXml").equals(null) ) {
	    	            		xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("UserCreateXml"));
	    	            		//url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    	            		url = url.replaceAll("<replaceaccount>", APIs.getAccountOID());
	    	            	}
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("User Create XML file not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }	    	            
	    	            APIs.executeCreate(url, xmlData, httpclient, "UserCreate");
	    			}
	    			else if (hm.get("API").equals("UserCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getusercreateurl();
	    	            log.info("UserCreate URL:" + url);
	    	            try{
	    	            	if( (!hm.get("UserCreateXml").equals(null)) || (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null))) {
	    	            		xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("UserCreateXml"));
	    	            		url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    	            		//url = url.replaceAll("<replaceaccount>", APIs.getAccountOID());
	    	            	}
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("User Create XML or AccountOID or Saml Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }	    	         	    	            
	    	            APIs.executeCreate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "UserCreate");
	    			}
	    			else if (hm.get("API").equals("UserGet")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("UserOID").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getusergeturl() + hm.get("UserOID");	  
	    						//System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or UserOID or Saml Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("UserGet URL:" + url);
	    	            APIs.executeGet(url, httpclient, (String)hm.get("SamlAssertion"), "UserGet");
	    			}
	    			else if (hm.get("API").equals("UserUpdate")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("UserUpdateXml").equals(null)) || (!hm.get("UserOID").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getuserupdateurl()+ hm.get("UserOID");	  
	    						//System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    						url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    						xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("UserUpdateXml"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or SAML Assertion or xml file or UserOID not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("UserUpdate URL:" + url); 
	    	            APIs.executeUpdate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "UserUpdate");
	    			}
	    			else if (hm.get("API").equals("UserDelete")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("UserOID").equals(null))) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getuserdeleteurl()+ hm.get("UserOID");	  
	    						//System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    						url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or SAML Assertion or UserOID not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("UserDelete URL:" + url);
	    	            APIs.executeDelete(url, httpclient, (String)hm.get("SamlAssertion"), "UserDelete");
	    			}
	    			else if (hm.get("API").equals("RightsTokenCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getrightstokencreateurl();
	    	            log.info("RightsTokenCreate URL:" + url);
	    	            try{
	    	            	if( (!hm.get("RightsTokenCreateXml").equals(null)) || (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null))) {
	    	            		xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("RightsTokenCreateXml"));
	    	            		url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    	            		//url = url.replaceAll("<replaceaccount>", APIs.getAccountOID());
	    	            	}
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("RightsTokenCreate Create XML or AccountOID or Saml Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }	    	         	    	            
	    	            APIs.executeCreate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "RightsTokenCreate");
	    			}
	    			else if (hm.get("API").equals("RightsTokenUpdate")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("RightsTokenUpdateUpdateXml").equals(null)) || (!hm.get("RightsTokenOID").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getrightstokenupdateurl()+ hm.get("RightsTokenOID");	  
	    						//System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    						url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    						xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("RightsTokenUpdateXml"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or SAML Assertion or xml file or RightsTokenOID not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("RightsTokenUpdate URL:" + url); 
	    	            APIs.executeUpdate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "RightsTokenUpdate");
	    			}
	    			else if (hm.get("API").equals("RightsTokenGet")){
	    				try{
	    					url = null;
	    					if( (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null)) || (!hm.get("RightsTokenOID").equals(null)) ) {
	    						url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getrightstokengeturl() + hm.get("RightsTokenOID");	  
	    						//System.out.println("saml assertion:" + hm.get("SamlAssertion") );
	    						url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    						APIs.setSamlAssertion((String)hm.get("SamlAssertion"));
	    					}
	    				}	    				
	    	            catch (NullPointerException npe) {
	    	            	log.error("Account OID or RightsTokenOID or Saml Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }
	    	            log.info("RightsTokenGet URL:" + url);
	    	            APIs.executeGet(url, httpclient, (String)hm.get("SamlAssertion"), "RightsTokenGet");
	    			}
	     			else if (hm.get("API").equals("StreamCreate")){
	    	            //url = "https://qa.p.uvvu.com:7001/rest/1/0/Provision/Account";
	    				url=null;
	    	            url = ConfigProperties.getbaseprovisionurl()+":"+port+ConfigProperties.getstreamcreateurl();
	    	            log.info("StreamCreate URL:" + url);
	    	            try{
	    	            	if( (!hm.get("StreamCreateXml").equals(null)) || (!hm.get("AccountOID").equals(null)) || (!hm.get("SamlAssertion").equals(null))) {
	    	            		xmlData = FileUtils.getFileContents(ConfigProperties.getXmlPath()+hm.get("StreamCreateXml"));
	    	            		url = url.replaceAll("<replaceaccount>", (String)hm.get("AccountOID"));
	    	            		//url = url.replaceAll("<replaceaccount>", APIs.getAccountOID());
	    	            	}
	    	            }
	    	            catch (NullPointerException npe) { 
	    	            	log.error("StreamCreate Create XML or AccountOID or Saml Assertion not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	    	            }	    	         	    	            
	    	            APIs.executeCreate(url, xmlData, httpclient, (String)hm.get("SamlAssertion"), "StreamCreate");
	    			}
	     			else if (hm.get("API").equals("ResourcePropertyQuery")){
	     				url=null;
	     				url = ConfigProperties.getbaseprovisionurl() + ":" + port+ ConfigProperties.getresourcepropertyqueryurl();
	     				log.info("ResourcePropertyQuery URL:" + url);
	     				try {
	     					if ( (!hm.get("PrimaryEmail").equals(null)) ){
	     						url = url+ (String)hm.get("PrimaryEmail");
	     					}
	     				}
	    	            catch (NullPointerException npe) { 
	    	            	log.error("ResourcePropertyQuery API - Primary Email not provided");
	    	            	log.error("###############Skipping scenario:" + al.get(0));
	    	            	break;
	     			}
	    	        APIs.executeHead(url, httpclient, "ResourcePropertyQuery");

    			}    			
    		}//for
    	  }//2nd while
		}catch (IOException e){
			log.error("UVDriver:main:IO Exception",e);
		}
        finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
        	
        	
        }
	} //end main
}//end class