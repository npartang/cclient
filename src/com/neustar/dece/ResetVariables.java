package com.neustar.dece;

public class ResetVariables {

	public static void resetVariables(){
		//to be called to reset all the variables after every scenario
		APIs.setAccountOID("");
		APIs.setSamlAssertion("");
		STSAPIs.setAssertion("");
		STSAPIs.setFinalAssertion("");
		
	}
}
