package cookieManager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CookieManager {
	//constantes
	private static String STORED_COOKIES_FILE = "src/resources/stored_cookies_file.txt";
	
	//Atributos
	private File f;
	private Map<Cookie, LocalDateTime> cookieEntry;
	private DateTimeFormatter formatter;
	
	//Contructor
	public CookieManager() {
		this.f = new File(STORED_COOKIES_FILE);
		if(!f.exists()) {
			System.err.println("Fichero de salida de cookies no existe");
		}
		this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.cookieEntry = new TreeMap<>();
		parseCookieFileToMap();
	}
	
	//Getters & setters
	public Set<Cookie> getCookies(){
		return Collections.unmodifiableSet(this.cookieEntry.keySet());
	}
	
	public void setCookies(Set<Cookie> cookieSet) {
		boolean sustituido;
		Set<Cookie> cookieEntrySet = this.cookieEntry.keySet();
		
		//Actualizamos las cookies
		for(Cookie c: cookieSet) {
			sustituido = false;
			//Comprobamos si el nombre de la cookie coincide en nuestra entrada
			for(Cookie d: cookieEntrySet) {
				if(c.getNombre().equals(d.getNombre())) {
					//Si los nombres coinciden se sustituye
					this.cookieEntry.remove(d);
					this.cookieEntry.put(c, LocalDateTime.now());
					sustituido = true;
					break;
				}
			}
			if(!sustituido) {
				this.cookieEntry.put(c, LocalDateTime.now());
			}
		}
		
		//Actualizamos el fichero
		updateFile();
	}
	
	//Funcionalidad
	private void updateFile() {
		try(PrintWriter writer = new PrintWriter(new FileWriter(this.f.getPath()))){
			//Recorremos las cookies almacenadas
			for(Cookie c: this.cookieEntry.keySet()) {
				writer.println(c.toString()+","+formatter.format(this.cookieEntry.get(c)));
			}
		}catch(IOException e) {
			e.printStackTrace();
			System.err.println("Error al actualizar fichero de cookies");
		}
	}
	
	private void parseCookieFileToMap() {
		BufferedReader reader = null;
		String line;
		if(this.f.length() > 0) {
			try {
				reader = new BufferedReader(new FileReader(f.getPath()));
	
				//Comenzamos con la lectura del fichero
				while( (line= reader.readLine()) != null) {
					parseFileCookie(line);
				}
				
				//Cerramos el lector
				reader.close();
			} catch (IOException e) {
				System.err.println("Error en la lectura del fichero de almacenamiento de cookies");
				return;
			}
		}
	}
	
	public void parseFileCookie(String line) {
		Cookie c;
		LocalDateTime t;
		
		//Cogemos el momento en el que se guardó
		try {
			t = LocalDateTime.parse(line.split(",")[1], formatter);
		} catch(Exception e) {
			System.err.println("Error al parsear la fecha de guardado de cookie");
			return;
		}
		
		//Parseamos la cookie
		c = Cookie.parseCookie(line.substring(0, line.indexOf(",")));
		//Si se ha cumplido la fecha no la añadimos
		boolean time = LocalDateTime.now().isAfter(t.plusSeconds(c.getMaxAge()));
		if(c != null && time) {
			return;
		}
		
		//Añadimos la cookie
		this.cookieEntry.put(c, t);
	}
}
