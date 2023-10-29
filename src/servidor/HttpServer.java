package servidor;

import java.io.*;
import java.net.*;

public class HttpServer {
	//Constantes
	public static final int HTTP_PORT = 8080;

	//Atributos
	ServerSocket s;
	Socket cliente; 
	
	//Constructor
	public HttpServer(int port) {
		try {
			s = new ServerSocket(port);
			System.out.println("Listening on port "+port);
		} catch (IOException e) {
			System.err.println("Error al asociar el servidor con el puerto "+port);
		}
	}
	
	//Funcionalidad
	public void run() {
		while(true) {
			try {
				cliente = s.accept();
				new ServerThreadManager(cliente).start();
			}catch(IOException e) {
				System.err.println("Error en la creación del hilo");
			}
		}
	}
	
	//Main
	public static void main(String[] args) {
        //Servidor HTTP
        HttpServer httpServer = new HttpServer(HttpServer.HTTP_PORT);
		httpServer.run();
    }
}

