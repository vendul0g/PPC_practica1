package pruebas_iniciales;

import java.io.*;
import java.net.*;

public class ServidorEco {
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
		BufferedReader istream;
		BufferedWriter ostream;
		
		istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
		ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
		String m = istream.readLine();
		
		ostream.write(m);
		ostream.newLine();
		ostream.flush();
		
		istream.close();
		ostream.close();
		s.close();
	}
}
