package pruebas_juguete;

import java.io.*;
import java.net.*;

public class ClienteEco {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // Cambia esto a la dirección IP del servidor
        int serverPort = 9999; // Cambia esto al puerto del servidor
        MiniSocketManager msm = new MiniSocketManager(serverAddress, serverPort);
        
        String mensaje = "hola";
        
        msm.sendMessage(mensaje);
        
        String respuesta = msm.getMessage();
        System.out.println("server answer = "+respuesta);
        
        msm.closeSocketManager();
    }
}
