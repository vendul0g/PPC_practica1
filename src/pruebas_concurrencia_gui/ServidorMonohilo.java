package pruebas_concurrencia_gui;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;

public class ServidorMonohilo{
	//Variables globales
	private static String PATH = "/home/alvaro/Documents/PPC/Practica1_PPC/src/resources";
//	private static String HTTP_REQ_REGEX = "^(GET|POST|PUT|DELETE|HEAD|OPTIONS) \\S+ HTTP/1\\.[01]\\r\\n(?:[A-Za-z0-9-]+: .*?\\r\\n)*\\r\\n[\\s\\S]*$\n";
	private static enum Method {GET};
	private static int BAD_REQUEST = 400;
	private static int NOT_FOUND = 404;
	private static int METHOD_NOT_ALLOWED = 405;
	private static int VERSION_NOT_SUPPORTED = 505;
	
	private static String HTTP_VERSION = "HTTP/1.1";
	
	public static void main(String args[]) {
		ServerSocket s = null;
		Socket cliente = null;
		try {
			s = new ServerSocket(9999);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//Aceptamos peticiones contínuamente
		while(true) {
			try {
				cliente = s.accept();
				manejaPeticion(cliente);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void manejaPeticion(Socket s) throws IOException{
		DataInputStream sin;
		PrintStream sout;
		sin = new DataInputStream(s.getInputStream());
		sout = new PrintStream(s.getOutputStream());
		String aux = "";
		String req = "";
		
		//Leemos la petición
		while( (aux = sin.readLine()) != null && !aux.isEmpty()) {
			//Recorre la petición
			req+=aux+"\r\n";
		}
		
		//Procesamos la respuesta
		String ans = process_request(req);
		
		//Devolvemos la respuesta al cliente
		sout.println(ans);
		
		//Cerramos los sockets
		sin.close();
		sout.close();
		s.close();
	}
	
	public static String process_request(String req) {
		//Comprobamos el formato de la petición TODO
//		Pattern pattern = Pattern.compile(HTTP_REQ_REGEX);
//		Matcher matcher = pattern.matcher(req);
//		System.out.println(req+"\n--------------\n");
//		if ( !matcher.find() ) {
//			System.out.println("bad_request");
//			return error_msg(BAD_REQUEST);
//		}
//		System.out.println("ok");
//		System.out.println((matcher=pattern.matcher(req)).find());
		
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
