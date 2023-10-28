package dev;

import java.io.*;
import java.net.*;

import servidor.ServerThreadManager;

public class ServidorMultihiloDev {
	//Variables globales
	private static final int SERVERPORT = 9999;
	
	@SuppressWarnings("resource")
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
				new ServerThreadManager(cliente).start();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}

