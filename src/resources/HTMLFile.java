package resources;


import historyMap.HistoryMap;

public class HTMLFile {
	//Atributos 
	private String url;
	private HistoryMap history;
	private String fileString;
	
	//Constructor
	public HTMLFile(String url, HistoryMap h) {
		this.url = url;
		this.history = h;
		this.fileString = createHTMLFile();
	}
	
	//funcionalidad
	public int length() {
		return fileString.length();
	}
	
	public String getFile() {
		return this.fileString;
	}
	
	private String createHTMLFile() {
		String f = "<!DOCTYPE html>"
				+ "<html>\n"
				+ "    <body>\n"
				+ "        <h1>Fichero solicitado: "+this.url+"</h1>\n"
				+ "        <h1>Historial:</h1>\n";
		f += history.generateHTMLTable();
		f += "    </body>\n"
				+ "</html>\n";
		return f;
	}
}
