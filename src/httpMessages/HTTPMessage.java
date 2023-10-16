package httpMessages;

public abstract class HTTPMessage {
	//Variables globales
	protected static final String VERSION = "HTTP/1.1";
	public static final String RETCAR = "\r\n";
	protected static final String SPACE = " ";
	
	public HTTPMessage() {
		
	}
	
	public abstract String getMessage();
	
	public static boolean isVersion(String v) {
		return v.equals(VERSION);
	}
}
