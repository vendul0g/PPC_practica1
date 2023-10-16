package httpMessages;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import dev.ServerThreadManager;


public class HTTPResponse200 extends HTTPResponseMessage{
	//variables globales
	private static final int CODE = 200;
	private static final String PHRASE = "OK";
	private static final String SERVER = "Server: JavaServer/2.0";
	private static final String CONTENT_TYPE = "Content-type: text/html";
	private static final String DATE = "Date: ";
	private static final String SET_COOKIE = "Set-Cookie: ";
	private static final String MAX_AGE = "max-age=3600";
	private static final String CONTENT_LENGTH = "Content-length: ";
	private static final String CONNECTION = "Connection: keep-alive";
	private static final String KEEP_ALIVE = "Keep-Alive: timeout="+ServerThreadManager.TIMEOUT/1000;
	
	//Atributos
	private LocalDateTime fecha;
	private List<String>cookies;
	private int contentLength;
	private String cuerpo;
	
	//Constructor
	public HTTPResponse200() {
		super(CODE, PHRASE);
		this.fecha = LocalDateTime.now();
		this.cookies = new LinkedList<String>();
		this.contentLength = 0;
		this.cuerpo = "";
	}
	
	//Getters & Setters
	public void setCookies(List<String> cookies) {
		this.cookies.addAll(cookies);
	}
	
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	public void setContentLenght(int contentLenght) {
		this.contentLength = contentLenght;
	}
	
	
	//Funcionalidad
	public String getMessage() {
		String response =  VERSION+SPACE+CODE+SPACE+PHRASE+RETCAR+
				SERVER+RETCAR+
				DATE+this.fecha+RETCAR+
				CONNECTION+RETCAR+
				KEEP_ALIVE+RETCAR;
		if (!cookies.isEmpty()) {
			for(String c : cookies) {
				response += SET_COOKIE+c+"; "+MAX_AGE+RETCAR;
			}
		}
		response += CONTENT_TYPE+RETCAR;			
		if (contentLength > 0) {
			response += CONTENT_LENGTH+contentLength+RETCAR;
		}
		response +=	RETCAR+
				cuerpo+
				RETCAR;
		
		return response;
	}

}
