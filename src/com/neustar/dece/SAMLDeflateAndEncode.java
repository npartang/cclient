package com.neustar.dece;
import java.util.zip.Deflater;
//import org.w3c.tools.codec.Base64Encoder;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
//import org.apache.commons.codec.binary.Base64;
import org.opensaml.xml.util.Base64;
import java.util.Arrays;


public class SAMLDeflateAndEncode {

	public static String getDeflateAndEncodedString(String rawXML){
		int startAssert = rawXML.indexOf("<saml2:Assertion ");
		int endAssert   = rawXML.indexOf("</saml2:Assertion>");
		System.out.println(startAssert + " " + endAssert);
		String xmlBody = rawXML.substring(startAssert, endAssert+18).trim();
		String base64encodeStr = "";
		//deflation
		byte [] xmlBytes = new byte[xmlBody.trim().length()];
		Deflater compresser = new Deflater(Deflater.DEFLATED, true);
		compresser.setInput(xmlBody.trim().getBytes());
		compresser.finish();
		int compressedLength = compresser.deflate(xmlBytes);
		//Base64 encode
		byte [] subArray = Arrays.copyOfRange(xmlBytes, 0, compressedLength);
		base64encodeStr = Base64.encodeBytes(subArray,Base64.DONT_BREAK_LINES);
		return base64encodeStr;
	}
	
	public static String getBase64UserPass (String uName, String pass){
		String authn = uName+ ":"+pass;		
		String base64encodeStr = Base64.encodeBytes(authn.getBytes());
		return base64encodeStr;
	}
}
