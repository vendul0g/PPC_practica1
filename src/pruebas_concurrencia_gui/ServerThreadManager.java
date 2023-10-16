package pruebas_concurrencia_gui;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.*;

public class ServerThreadManager {
	//Variables globales
	private static final String PATH = "/home/alvaro/Documents/PPC/Practica1_PPC/src/resources";
	private static final String HTTP_REQ_REGEX = "^(GET|POST|PUT|DELETE|PATCH) [^ ]+ HTTP/[0-9]\\.?[0-9]\\r\\n([A-Z].*?: .+\\r\\n)*";
	private static enum Method {GET};
	private static final int BAD_REQUEST = 400;
	private static final int NOT_FOUND = 404;
	private static final int METHOD_NOT_ALLOWED = 405;
	private static final int VERSION_NOT_SUPPORTED = 505;
	private static final String LOGFILE = PATH+"/server.log";
	
	private static String HTTP_VERSION = "HTTP/1.1";
	//Atributos
	private Socket s;
	private BufferedWriter buf;
	private Date fecha;
	
	public ServerThreadManager(Socket s) {
		this.s = s;
		fecha = new Date();
	}
	
	public void run() {
		BufferedReader sin;
		PrintWriter sout;
		try {
//			sin = new DataInputStream(s.getInputStream());
			sin = new BufferedReader(new InputStreamReader(s.getInputStream()));
			sout = new PrintWriter(s.getOutputStream());
			String aux = "";
			String req = "";
			boolean openned_connection = true;
			
			//Abrimos el fichero de logs
			File file = new File(LOGFILE);
			FileWriter w = new FileWriter(file, true);
			this.buf = new BufferedWriter(w);
			
			//Conexión persistente
//			while(openned_connection) {
				System.out.println(" - bucle persistencia");
				//Leemos la petición
				while( (aux = sin.readLine()) != null && !aux.isEmpty()) {
					//Recorre la petición
					req+=aux+"\r\n";
				}
				
				//Procesamos la respuesta
				String ans = process_request(req);
				
				//Devolvemos la respuesta al cliente
				sout.println(ans); //NOTA: si no funciona poner un bucle
//			}
				
			//Cerramos los sockets y descriptores
			buf.close();
			sin.close();
			sout.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String process_request(String req) throws IOException {
		//Comprobamos el formato de la petición TODO
		Pattern pattern = Pattern.compile(HTTP_REQ_REGEX);
		Matcher matcher = pattern.matcher(req);
		if ( !matcher.find() ) {
			return error_msg(BAD_REQUEST);
		}
		
		//Comenzamos el procesamiento
		String[] lines = req.split("\r\n");
		String[] reqline = lines[0].split(" ");
		
		//Comprobamos el método
		Method m;
		if( (m = comprobar_metodo(reqline[0])) == null) {
			return error_msg(METHOD_NOT_ALLOWED);
		}
		
		//Comprobamos la Versión
		if(!reqline[2].equals(HTTP_VERSION)) {
			return error_msg(VERSION_NOT_SUPPORTED);
		}
		
		//TODO procesar cabeceras
		
		//Comprobamos la URL
		String url="";
		if(reqline[1].equals("/")) {
			url = "/index.html";
		}
		else url = reqline[1];
		
		//Anotamos en los logs
		this.buf.write(fecha+" "+this.s.getInetAddress()+":"+this.s.getPort()+" --> "+url+"\n");
		
		byte[] byte_file;
		try {
			byte_file = Files.readAllBytes(Paths.get(PATH+url));
		}catch (IOException e) {
			return error_msg(NOT_FOUND);
		}
		//Enviamos el fichero
		String file = new String(byte_file, StandardCharsets.UTF_8);
		return ok_msg()+file;
		
//		return error_msg(BAD_REQUEST);
	}
	
	public static Method comprobar_metodo(String method) {
		for(Method m : Method.values()) {
			if (m.toString().equals(method)) {
				return m;
			}
		}
		return null;
	}
	
	//Métodos generales
	public static String error_msg (int error) {
		String f="";
		switch (error) {
		case 400:
			f = "Bad Request";
			break;
		case 401:
			f = "Unauthorized";
			break;
		case 403:
			f = "Forbidden";
			break;
		case 404:
			f = "Not Found";
			break;
		case 405: 
			f = "Method Not Allowed";
			break;
		case 500:
			f = "Internal Server Error";
			break;
		case 505:
			f = "Version Not Supported";
			break;
		}
		
		String html_error ="<!DOCTYPE html>\n"
				+ "<html lang=\"en\">\n"
				+ "<body>\n"
				+ "    <div class=\"container\">\n"
				+ "        <h1>Error "+error+". "+f+"</h1>\n"
				+ "    </div>\n"
				+ "</body>\n"
				+ "</html>\n";
		
		String error_msg = "HTTP/1.1 "+error+" "+f+"\r\n\r\n";
		return error_msg+html_error;
	}
	
	public static String ok_msg () {
		return "HTTP/1.1 200 OK\r\n"+
			"Server: server.dev\r\n"+
			"Set-Cookie: feo=true\r\n\r\n";
	}
}
