package pruebas_concurrencia_gui;

import java.io.*;
import java.net.*;

public class ServidorMultihilo {
	private static final int SERVERPORT = 9999;
	public static void main (String args[]) {
		ServerSocket s = null;
		Socket cliente = null;
		
		try {
			s = new ServerSocket(SERVERPORT);
			System.out.println("Listening on port "+SERVERPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				cliente = s.accept();
				new ServerThreadManager(cliente).run();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
