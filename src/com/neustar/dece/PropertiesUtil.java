package com.neustar.dece;
import java.util.*;
import java.io.*;

public class PropertiesUtil {

	
	public static Properties load(FileInputStream inStream){
		
		
		Properties props = new Properties();
		try{
			props.load(inStream);
			ConfigProperties.setHostName(props.getProperty("host"));
			ConfigProperties.setPort(props.getProperty("port"));
			ConfigProperties.setKeyStoreFile(props.getProperty("keystorefile"));
			ConfigProperties.setKeyStorePassword(props.getProperty("keystorepassword"));
			ConfigProperties.setXmlPath(props.getProperty("xmlpath"));
			ConfigProperties.setbaseprovisionurl(props.getProperty("baseprovisionurl"));
			ConfigProperties.setbasequeryurl(props.getProperty("basequeryurl"));
			ConfigProperties.setbasedeviceurl(props.getProperty("basedeviceurl"));
			ConfigProperties.setaccountcreateurl(props.getProperty("accountcreateurl"));
			ConfigProperties.setStsCreateurl(props.getProperty("stscreateurl"));
			ConfigProperties.setStsGeturl(props.getProperty("stsgeturl"));
			ConfigProperties.setScenarioFile(props.getProperty("scenariofile"));
			ConfigProperties.setaccountgeturl(props.getProperty("accountgeturl"));
			ConfigProperties.setaccountupdateurl(props.getProperty("accountupdateurl"));
			ConfigProperties.setaccountdeleteurl(props.getProperty("accountdeleteurl"));
			ConfigProperties.setaccountpolicycreateurl(props.getProperty("accountpolicycreateurl"));
			ConfigProperties.setuserpolicycreateurl(props.getProperty("userpolicycreateurl"));
			ConfigProperties.setusercreateurl(props.getProperty("usercreateurl"));
			ConfigProperties.setusergeturl(props.getProperty("usergeturl"));
			ConfigProperties.setuserupdateurl(props.getProperty("userupdateurl"));
			ConfigProperties.setuserdeleteurl(props.getProperty("userdeleteurl"));
			ConfigProperties.setrightstokencreateurl(props.getProperty("rightstokencreateurl"));
			ConfigProperties.setrightstokenupdateurl(props.getProperty("rightstokenupdateurl"));
			ConfigProperties.setrightstokengeturl(props.getProperty("rightstokengeturl"));
			ConfigProperties.setstreamcreateurl(props.getProperty("streamcreateurl"));
			ConfigProperties.setresourcepropertyqueryurl(props.getProperty("resourcepropertyqueryurl"));

		}
		
		catch(IOException e){
			System.out.println(e.toString());
		}
		return props;
	}
	
	
}
