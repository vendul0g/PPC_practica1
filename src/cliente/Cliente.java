package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import cookieManager.*;
import httpMessages.HTTPGetMessage;


public class Cliente {
	//Variables globales
	protected static final String SERVERNAME = "serverppc.com";
	private static final int BUFSIZE = 1024;
		
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	private Scanner scanner;
	private boolean isOpen;
	private CookieManager cm;
	private int port;

	//Cosntructor
	public Cliente(int port) {
		this.port = port;
		this.isOpen = false;
		this.cm = new CookieManager();
	}
	
	//Setter
	protected void setSocket(Socket s) {
		this.s = s;
	}
	
	//Funcionalidad
	public void start() {
		String answer;
		String url;
		scanner = new Scanner(System.in);

		while (true) {//Comenzamos la lectura de entrada de usuario
        	//Leemos entrada usuario
        	System.out.print("URL: ");
        	url = scanner.nextLine();
        	
        	//Creamos los buffers de lectura/escritura del socket
        	if(!isOpen) {
        		createBuffers();
        	}
        	
        	//Recuperamos las cookies de sesiones anteriores
        	HTTPGetMessage m = new HTTPGetMessage(url, SERVERNAME+":"+port);
        	Set<Cookie> cookies = cm.getCookies();
        	m.addCookies(cookies);
        	
        	//MOstramos la petición realizada
        	printRequest(m.getMessage());
        	
        	//Envío de petición HTTP
        	sendMessage(m.getMessage());
        	
        	//Recibimos respuesta
        	answer = getMessage();
        	
        	//Procesamos la respuesta
        	if(!answer.equals("")) {
        		if( answer.split("\\r\\n\\r\\n").length <= 1) {
        			System.out.println(answer);
        			continue;
        		}
        		//Separamos las cabeceras del cuerpo;
        		String cabeceras = answer.split("\\r\\n\\r\\n")[0];
        		String cuerpo = answer.split("\\r\\n\\r\\n", 2)[1];
        		
        		//Comprobamos que hay contenido
        		if(cabeceras.isEmpty() || cuerpo.isEmpty()) {
        			System.err.println("Respuesta del servidor mal formateada");
        			return;
        		}
        		
        		//Procesamos las cookies que nos devuelve el servidor
        		proccesCookies(cabeceras.split("\r\n"));
        		
        		//Mostramos el resultado por pantalla
        		printAnswer(cabeceras, cuerpo);
        	} else {
        		/*Si el mensaje del socket nos llega sin contenido quiere decir que 
        		 * se ha cerrado debido a que se cumplió el timeout ==> cerramos nosotros
        		 * también la conexión
        		 */
        		System.err.println("Timeout Excedido. Conexión cerrada");
        		break;
        	}
		}
		//Cerramos el scanner
		closeScanner();
		//Cerramos el socket
		closeSocket();
	}
	
	private void printRequest (String m) {
		System.out.println("-------------------Request----------------------\n"
				+ m+"\n"
				+ "-------------------------------------------------------\n\n");
	}
	
	private void printAnswer(String cabeceras, String cuerpo) {
		System.out.println("----------------Header-----------------------\n"
				+ cabeceras+"\n"
				+ "--------------------------Body---------------------\n"
				+ cuerpo+"\n"
				+ "···············································\n\n");
	}
	
	private void proccesCookies(String[] cabeceras) {
		Set<Cookie> cookieSet = new TreeSet<>();
		String aux;
		
		//Recorremos las cabeceras para ver si se nos ha asignado alguna cookie
		for(String c : cabeceras) {
			if (c.split(":")[0].equals("Set-Cookie")){
				aux = c.split(": ", 2)[1];
				cookieSet.add(Cookie.parseCookie(aux));
			}
		}
		
		//Una vez tenemos el Set de cookies listo, lo añadimos al fichero correspondiente
		this.cm.setCookies(cookieSet);
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
		StringBuilder message = new StringBuilder();
		int bytesRead;
		char[] buff = new char[BUFSIZE];
		
		try {
			//Leemos del socket
			do {
				try { //Preparamos timeout
					bytesRead = istream.read(buff);
				} catch (SocketException e) {
					System.err.println("Error. Socket cerrado");
					return "";
				}
				
				if(bytesRead > 0) {
					message.append(buff, 0, bytesRead);
				}
			}while(bytesRead == BUFSIZE);

			return message.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error en la lectura del socket");
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
}
