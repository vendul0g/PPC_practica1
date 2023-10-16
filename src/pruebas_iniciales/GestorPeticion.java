package pruebas_iniciales;

import java.net.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;

public class GestorPeticion extends Thread{
	//Variables globales
	private static final String PATH = "/home/alvaro/Documents/PPC/Practica1_PPC/src/resources";
	private static final String LOGFILE = PATH+"/server.log";
	private static final int BUFSIZE = 1024;
		
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	
	//Constructor
	public GestorPeticion(Socket s) {
		this.s = s;
	}
	
	//Funcionalidad
	public void run() {
		//Declaracion de variables
		String message;
		String answer;
		
		//Inicializamos los buffers de lectura/escritura
		createBuffers();
		
		//Leemos la petición
		message = getMessage();

		//Procesamos la petición para crear una respuesta
		//TODO
		
		//Enviamos la respuesta
		try {
			ostream.write(message);
			ostream.newLine();
			ostream.flush();
		} catch (IOException e) {
			System.err.println("Error en la escritura del socket");
		}
					
		try {
			istream.close();
			ostream.close();
			s.close();
		} catch (IOException e) {
			System.err.println("Fallo al cerrar el socket");
		}
	}
	
	private void createBuffers() {
		try {
			istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(IOException e) {
			System.err.println("Fallo al intentar crear sockets de lectura y escritura");
			return;
		}
	}
	
	private String getMessage() {
		StringBuilder message = new StringBuilder();
		int bytesRead;
		char[] buff = new char[BUFSIZE];
		
		try {
			//Leemos del socket
			do {
				bytesRead = istream.read(buff);
				if(bytesRead > 0) {
					message.append(buff, 0, bytesRead);
				}
			}while(bytesRead == BUFSIZE);

			return message.toString();
		} catch (IOException e) {
			System.err.println("Error en la lectura del socket");
			return "";
		}
	}
}
