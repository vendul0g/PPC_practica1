package cliente;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class ClienteHttps extends Cliente{
	//Constantes
	private static final String KSTORE		= "src/certs/alvaro.p12";
	private static final String KS_PWD		= "alvaro";
	private static final String CERT_PWD	= "alvaro";
	public static final int	SERVERPORT 	= 4433;

	//Atributos
	SSLSocket s;
	
	public ClienteHttps() {
		super(SERVERPORT);
		loadSecureConnection();
		this.setSocket(s);
	}
	
	public void loadSecureConnection() {
	    SSLContext ctx;
	    SSLSocketFactory fac;

	    try {
	        // Configura el almacén de confianza con el certificado de la CA
	        System.setProperty("javax.net.ssl.trustStore", "src/certs/cappc.p12");
	        System.setProperty("javax.net.ssl.trustStorePassword", "alvaro");

	        //Cargamos el certificado del cliente
	        KeyStore ks = KeyStore.getInstance("PKCS12");
	        ks.load(new FileInputStream(KSTORE), KS_PWD.toCharArray());

	        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        kmf.init(ks, CERT_PWD.toCharArray());

	        //Creamos el contexto
	        ctx = SSLContext.getInstance("TLSv1.3");
	        ctx.init(kmf.getKeyManagers(), null, null); 
	        fac = ctx.getSocketFactory();
	        s = (SSLSocket) fac.createSocket(SERVERNAME, SERVERPORT);

	    } catch (Exception e) {
	        System.err.println("Error instanciando el cliente seguro");
	    }
	}
}
