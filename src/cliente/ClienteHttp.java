package cliente;

import java.io.IOException;
import java.net.Socket;

public class ClienteHttp extends Cliente{
	//Constantes
	private static final int SERVERPORT = 8080;
	
	//Atributos
	private Socket s;
	
	//Constructor
	public ClienteHttp() {
		super(SERVERPORT);
		try {
			this.s = new Socket(SERVERNAME, SERVERPORT);
			this.setSocket(s);
		}catch (IOException e ) {
			e.printStackTrace();
			System.err.println("Error creando el socket del cliente");
		}
	}
}
