package app.edlc.taskapi.security.authentication;

import app.edlc.taskapi.security.data.AccountCredentialsDto;

public class ValidationUtil {
	
	private ValidationUtil() {}
	
	public static boolean isStrNullOrBlank(String str) {
		if(str == null || str.isBlank())
			return true;
		
		return false;
	}
	
	public static boolean isCredentialsNullOrBlank(AccountCredentialsDto credentials) {
		if (credentials == null 
			|| credentials.getUsername() == null || credentials.getUsername().isBlank()
			|| credentials.getPassword() == null || credentials.getPassword().isBlank())
			return true;			
		
		return false;
	}	
}
