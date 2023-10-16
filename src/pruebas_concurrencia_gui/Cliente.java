package pruebas_concurrencia_gui;

import java.io.*;
import java.net.*;


public class Cliente {
	public static void main(String args[]) {
		Socket s;
		DataInputStream istream;
		DataOutputStream ostream;
		String servername = "localhost";
		int serverport = 9999;
		String aux;
		
		try {
			//Creamos los sockets y los stream
			s = new Socket (servername, serverport);
			istream = new DataInputStream(s.getInputStream());
			ostream = new DataOutputStream(s.getOutputStream());
			
			//Creación del mensaje de petición
			String msg = "GET / HTTP/1.1\r\n" + 
				"Host: " + servername + "\r\n" + 
				"Connection: close\r\n" +
				"User-agent: java-vendul0g/0.1\r\n" +
				"Accept-language: es\r\n" +
				"Accept: *.html\r\n" +
				"Accept: *.text\r\n" +
				"\r\n";
			
			//Envío de datos
			ostream.writeBytes(msg);
			
			//Recepción de datos
			while((aux=istream.readLine()) != null ) {
				System.out.println(aux);
			}
			
			//Cerramos los stream y socket
			istream.close();
			ostream.close();
			s.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
