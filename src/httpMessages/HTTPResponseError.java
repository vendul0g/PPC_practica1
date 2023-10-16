package httpMessages;

public class HTTPResponseError extends HTTPResponseMessage{
	//Constructor
	public HTTPResponseError(int code, String phrase) {
		super(code, phrase);
	}
	
	//Funcionalidad
	public String getMessage() {
		return VERSION+SPACE+code+SPACE+phrase+RETCAR+RETCAR+
		"<!DOCTYPE html>\n"
		+ "<html lang=\"en\">\n"
		+ "<body>\n"
		+ "    <div class=\"container\">\n"
		+ "        <h1>Error "+code+". "+phrase+"</h1>\n"
		+ "    </div>\n"
		+ "</body>\n"
		+ "</html>\n";
	}
}
