package httpMessages;

public class HTTPResponse400 extends HTTPResponseError{
	//Variables globales
	private static final int CODE = 400;
	private static final String PHRASE = "Bad Request";
	
	//Constructor
	public HTTPResponse400() {
		super(CODE, PHRASE);
	}
}
