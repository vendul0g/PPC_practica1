package pruebas_iniciales;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import httpMessages.HTTPMessage;

public class ClienteEco {
	//Variables globales
		private static final String SERVERNAME = "localhost";
		private static final int SERVERPORT = 9999;
		
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	private Scanner scanner;
	
	//Constructor
	public ClienteEco() {
		try {
			this.s = new Socket(SERVERNAME, SERVERPORT);
		}catch (IOException e ) {
			System.err.println("Error creando el socket del cliente");
		}
	}
	
	public void start() {
		String answer;
		String url;

		//Creamos los buffers de lectura/escritura del socket
		createBuffers();
		
		//Leemos entrada usuario
		scanner = new Scanner(System.in);
		System.out.print("URL: ");
		url = scanner.nextLine();
		
		
		//Envio la peticion
		sendMessage(url);
		
		//Recibimos una respuesta
		answer = getMessage();
		
		//Mostramos la respuesta
		System.out.println(answer);
		//TODO Mostrar cabeceras y cuerpo por separado
		
		//Cerramos el scanner
		closeScanner();
		
		//Cerramos el socket
		closeSocket();
	}
	
	private void createBuffers() {
		try {
			istream = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
			ostream = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream()));
		}catch(IOException e) {
			System.err.println("Error al crear buffers de lectura y escritura del socket");
			return;
		}
	}
	
	private void sendMessage(String url) {
		try {
			ostream.write(url);
			ostream.newLine();
			ostream.flush();
		} catch (IOException e) {
			System.err.println("Error en la escritura del socket");	
			closeScanner();
		}
	}
	
	private String getMessage() {
		String answer = "";
		String aux = "";
		
		try {
			while((aux=istream.readLine()) != null) {
				answer += aux+HTTPMessage.RETCAR;
			}
			return answer;
			
		} catch(IOException e) {
			System.err.println("Error leyendo del socket");
			closeScanner();
			return "";
		}
	}
	
	private void closeSocket(){
		try {
			this.s.close();
			istream.close();
			ostream.close();
		} catch (IOException e3) {
			System.err.println("Error cerrando los sockets I/O");
		}
	}
	
	private void closeScanner() {
		this.scanner.close();
	}
	
	public static void main(String args[]) {
		ClienteEco c = new ClienteEco();
		c.start();
	}
}
