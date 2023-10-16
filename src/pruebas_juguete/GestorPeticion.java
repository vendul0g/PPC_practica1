package pruebas_juguete;

import java.io.*;
import java.net.*;

public class GestorPeticion extends Thread {
    Socket s;

    public GestorPeticion(Socket s) {
        this.s = s;
    }

    public void run() {
    	MiniSocketManager msm = new MiniSocketManager(s);
    	
    	String texto = msm.getMessage();
    	msm.sendMessage(texto);
    	msm.closeSocketManager();
    }
}
