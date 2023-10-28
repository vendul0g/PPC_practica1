package servidor;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;

public class HTTPSServer {
    static public final boolean DEBUG = true;

    static public final String SV_STORE = "src/certs/serverppc.p12";
    static public final String SV_PWD = "alvaro";
    static public final String CA_STORE = "src/certs/cappc.p12";
    static public final String CA_PWD = "alvaro";

    static public void instanceEchoServerCert(int port) {
        SSLContext ctx;
        SSLServerSocketFactory fac;
        SSLServerSocket s;
        Socket s1;
        KeyStore ks;
        KeyManagerFactory kmf;
        TrustManagerFactory tmf;

        try {
            // Cargar el almacén de claves del servidor
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(SV_STORE), SV_PWD.toCharArray());

            // Configurar el administrador de claves del servidor
            kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, SV_PWD.toCharArray());

            // Cargar el almacén de confianza (CA)
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(new FileInputStream(CA_STORE), CA_PWD.toCharArray());

            // Configurar el administrador de confianza
            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);

            ctx = SSLContext.getInstance("TLSv1.2");
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            fac = ctx.getServerSocketFactory();
            s = (SSLServerSocket) fac.createServerSocket(port);
//            s.setNeedClientAuth(true);

            while (s != null) {
                System.out.println("WAITING");
                s1 = s.accept();
                serverEcho(s1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static protected void serverEcho(Socket s) {
        BufferedWriter os;
        BufferedReader is;
        String echo;

        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            System.out.println("RECEIVING");
            echo = is.readLine();
            System.out.println("RECEIVED <" + echo + ">");
            String test = echo + "\n";
            System.out.println("ANSWERING");
            os.write(test, 0, test.length());
            os.flush();

            s.close();
            System.out.println("FINISHED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HTTPSServer.instanceEchoServerCert(4433);
    }
}
