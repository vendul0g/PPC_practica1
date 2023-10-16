package httpMessages;

public abstract class HTTPResponseMessage extends HTTPMessage{
	//Atributos
	protected int code;
	protected String phrase;
	
	//Constructor
	public HTTPResponseMessage(int code, String phrase) {
		this.code = code;
		this.phrase = phrase;
	}
	
	//Funcionalidad
	public abstract String getMessage();
}
