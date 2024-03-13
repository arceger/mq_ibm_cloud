package com.over.web5.xml;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v1.SecretsManager;
import com.ibm.cloud.secrets_manager_sdk.secrets_manager.v1.model.*;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;


public class SecretManagerUtil {
	protected static SecretsManager sm;
    protected static IamAuthenticator iamAuthenticator;
    
    
    public static User getCredentials(String userUUID) {
    	iamAuthenticator = new IamAuthenticator.Builder()
	              .apikey(System.getenv("apikey"))
	              .build();
	        sm = new SecretsManager("My Secrets-Manager service", iamAuthenticator);
	        sm.setServiceUrl(System.getenv("secretmanagerurl"));

	  
	        GetSecretOptions getSecretOptions = new GetSecretOptions.Builder()
	        		  .secretType("username_password")
	        		  .id(userUUID)
	        		  .build();
	        
	        Response<GetSecret> getResp = sm.getSecret(getSecretOptions).execute();
	        
	       
	      
	        String secretPayload = (String) getResp.getResult().getResources().get(0).secretData().get("password");
	        String username = (String) getResp.getResult().getResources().get(0).secretData().get("username");
	        
	        return new User(username, secretPayload);

    	
    }

}
