package httpMessages;

public abstract class HTTPRequestMessage extends HTTPMessage {
	//Variables globales
	public static final String HTTP_REQ_REGEX = "^(GET|POST|PUT|DELETE|PATCH) [^ ]+ HTTP/[0-9]\\.?[0-9]\\r\\n([A-Z].*?: .+\\r\\n)*";
	protected final String USER_AGENT = "User-Agent: JavaBrowser/2.0";
	protected final String CONNECTION = "Connection: keep-alive";
	protected final String COOKIE = "Cookie: ";
	
	//Atributos
	protected String url;
	protected String host;
	
	//Constructor
	public HTTPRequestMessage(String url, String servername) {
		this.url = url;
		this.host = servername;
	}
	
	//Funcionalidad
	public abstract String getMessage();
}
