package Common;

public class StringUtils {
	
	public static boolean isEmty(String str) {
		if(str != null && !str.trim().isEmpty()) return false;
		return true;
	}

	public static String getMessage(String message, String str) {
		if(!isEmty(str)) {
			str += " , " + message;
		}else {
			str = message;
		}
		
		return str;
		
	}
}
