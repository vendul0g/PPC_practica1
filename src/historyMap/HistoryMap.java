package historyMap;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HistoryMap {
	//Atributos
	Map<String, Integer> m;
	
	//Constructor
	public HistoryMap(String s) {
		this.m = new TreeMap<>();
		parseMap(s);
	}
	
	//Getters & Setters
	public String getHistoryMap() {
		StringBuilder mapAsString = new StringBuilder("{");
	    for (String key : m.keySet()) {
	        mapAsString.append(key + "=" + m.get(key) + ",");
	    }
	    mapAsString.deleteCharAt(mapAsString.length()-1);
	    mapAsString.append("}");
	    return mapAsString.toString();
	}
	
	private void parseMap(String s){
		String[] aux = new String[2];

		//Formateamos la cadena de entrada
		s = s.substring(1, s.length()-1);
		//Creamos el mapa
		for(String a : s.split(",")) {
			aux = a.split("=",2);
			try {
				this.m.put(aux[0], Integer.parseInt(aux[1]));
			}catch(Exception e) {
				System.err.println("Fallo al parsear el mapa del historial");
			}
		}
	}
	
	public Integer get(String s) {
		return this.m.get(s);
	}
	
	//Funcionalidad
	public void put(String s, Integer i) {
		this.m.put(s, i);
	}
	
	public Set<String> keySet(){
		return this.m.keySet();
	}
	
	public String generateHTMLTable() {
        StringBuilder html = new StringBuilder();
        html.append("\t<table border='1'>\n");
        html.append("\t\t<tr><th>Recurso</th><th>Numero de peticiones</th></tr>\n");

        for (Map.Entry<String, Integer> entry : this.m.entrySet()) {
            html.append("\t\t\t<tr>");
            html.append("<td> ").append(entry.getKey()).append("</td>");
            html.append("\t<td> ").append(entry.getValue()).append("</td>");
            html.append("</tr>\n");
        }

        html.append("\t</table> \n");
        return html.toString();
    }
}
