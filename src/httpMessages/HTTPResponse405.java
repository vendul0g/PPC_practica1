package httpMessages;

public class HTTPResponse405 extends HTTPResponseError{
	//Variables globales
		private static final int CODE = 405;
		private static final String PHRASE = "Method Not Allowed";
		
		//Constructor
		public HTTPResponse405() {
			super(CODE, PHRASE);
		}
}
