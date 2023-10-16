package httpMessages;

public class HTTPResponse505 extends HTTPResponseError{
	//Variables globales
	private static final int CODE = 505;
	private static final String PHRASE = "Version Not Supported";
	
	//Constructor
	public HTTPResponse505() {
		super(CODE, PHRASE);
	}
}
