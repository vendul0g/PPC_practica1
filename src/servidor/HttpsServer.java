package servidor;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class HttpsServer {
	//Constantes
    private static final String SERVERCERT = "src/certs/serverppc.p12";
    private static final String PASSPHRASE = "alvaro";
    private static final String CACERT = "src/certs/cappc.p12";
    public static final int HTTPS_PORT = 4433;

    //Atributos
    private int port;
    private KeyStore serverKeystore ;
    private KeyManagerFactory keyManagerFactory;
    private TrustManagerFactory trustManagerFactory;
    private SSLContext sslContext;
    private SSLServerSocket sss;
    
    //Constructor
    public HttpsServer(int port) {
    	System.out.println("Listening on port "+port);
    	this.port = port;
    	
    	//Cargar claves del servidor
    	loadServerCert();
    	loadCACert();
    	loadContext();
    }
    
    //Funcionalidad
    public void lauchHttpsServer() {
        Socket s;
        try {
            while (sss != null) {
                s = sss.accept();
                ServerThreadManager st = new ServerThreadManager(s);
                st.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadServerCert() {
    	// Cargar el almacén de claves del servidor
        try{
        	serverKeystore = KeyStore.getInstance("PKCS12");
        	serverKeystore.load(new FileInputStream(SERVERCERT), PASSPHRASE.toCharArray());

        	//configurar administrador de claves del servidor 
        	keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        	keyManagerFactory.init(serverKeystore, PASSPHRASE.toCharArray());
        }catch(Exception e) {
        	System.err.println("Error cargando el certificado del servidor");
        }
    }
    
    public void loadCACert() {
    	// Cargar el almacén de confianza (CA)
        try{
        	KeyStore trustStore = KeyStore.getInstance("PKCS12");
        	trustStore.load(new FileInputStream(CACERT), PASSPHRASE.toCharArray());
        	
        	// Configurar el administrador de confianza
        	trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        	trustManagerFactory.init(trustStore);
        }catch(Exception e) {
        	System.err.println("Error cargando el certificado de la CA");
        }
    }

    public void loadContext() {
    	SSLServerSocketFactory factory;
        try {
        	sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
			
			factory = sslContext.getServerSocketFactory();
			sss = (SSLServerSocket) factory.createServerSocket(port);
			sss.setNeedClientAuth(true);
		} catch (Exception e) {
			System.err.println("Error creando socket servidor");
		}
    }
    
    //Main
    public static void main(String[] args) {
		//Servidor HTTPS
        HttpsServer server = new HttpsServer(HttpsServer.HTTPS_PORT);
        server.lauchHttpsServer();
    }
}
