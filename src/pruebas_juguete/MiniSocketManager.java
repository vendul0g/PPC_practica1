package pruebas_juguete;

import java.net.*;
import java.io.*;

public class MiniSocketManager {
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	
	//Constructores
	public MiniSocketManager(String serverAddress, int serverPort) {
		try {
			this.s = new Socket(serverAddress, serverPort);
			this.istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch(IOException e) {
			System.err.println("Fallo al intentar crear SocketReader");
		}
	}
	
	public MiniSocketManager(Socket s) {
		try {
			this.s = s;
			this.istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch(IOException e) {
			System.err.println("Fallo al intentar crear SocketReader");
		}
	}
	
	//Getter
	public String getIdentity() {
		return s.getInetAddress().toString()+":"+s.getPort();
	}
	
	//Funcionalidad
	public String getMessage() {
		String message="";
		String aux="";
		try {
			while((aux=istream.readLine()) != null /*&& !"".equals(aux)*/) {
				message +=aux+"\r\n";
			}
			return message;
		}catch(IOException e) {
			System.err.println("Error de lectura en el socket");
			return "";
		}
	}
	
	public void sendMessage(String m) {
		try {
			ostream.write(m+"\n");
			ostream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeSocketManager() {
		try {
			this.s.close();
			this.istream.close();
			this.ostream.close();
		} catch (IOException e) {
			System.err.println("Error cerrando los sockets I/O");
		}
	}
}
