package pruebas_concurrencia_gui;

import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ClienteGUI extends JFrame implements ActionListener{
	//Variables globales
	private static final String SERVERNAME = "localhost";
	private static final int SERVERPORT = 9999;
	
	//Atributos
	private static final long serialVersionUID = 1L;
	private JTextField barra;
	private JButton enter;
	private JEditorPane editorPane;
	private Socket s;
	private BufferedReader istream;
	private PrintWriter ostream;
	private JPanel center;
	private JTextArea txtCabeceras;
	
	public ClienteGUI() {
		//Inicializa los sockets
		try {
			s = new Socket (SERVERNAME, SERVERPORT);
			istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ostream = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e ) {
			e.printStackTrace();
		}
		
		// Configura el JFrame
        setTitle("!Google");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 1200);

        // Crea la barra superior
        JPanel topPanel = new JPanel();
        barra = new JTextField(40);
        barra.setText("/");
        enter = new JButton("Search");
        this.getRootPane().setDefaultButton(enter);
        enter.addActionListener(this);
        topPanel.add(barra);
        topPanel.add(enter);

        // Crea el componente para mostrar HTML
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);

        // Agrega los componentes al JFrame
        center = new JPanel();
        JScrollPane scrollCenter = new JScrollPane(editorPane); 
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);
        center.setLayout(new BorderLayout());
        
        JPanel panelCabecera = new JPanel();
        center.add(panelCabecera, BorderLayout.NORTH);
        center.add(scrollCenter, BorderLayout.CENTER);
        
        txtCabeceras = new JTextArea();
        txtCabeceras.setEditable(false);
        panelCabecera.setLayout(new BoxLayout(panelCabecera, BoxLayout.Y_AXIS));
        panelCabecera.add(txtCabeceras);        
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == enter) {
            String url = barra.getText();
            barra.setText("/");
            String line;
            StringBuilder htmlContent = new StringBuilder();
            //Comunicación por el socket
           	try {
				//Enviamos petición
           		ostream.print(requestMessage(url));
				
				//Recibimos respuesta
           		while((line=istream.readLine()) != null ) {
    				htmlContent.append(line+"\r\n");
    				System.out.println("sigo?");
    			}
           		System.out.println("respondido");
           		cargarRespuesta(htmlContent);
           		
			} catch (IOException e1) {
				e1.printStackTrace();
			}            	
        }
    }

    private void cargarRespuesta(StringBuilder htmlContent) throws IOException{
    	//Borramos
    	editorPane.setText("");
    	txtCabeceras.setText("");
    	
    	//Escribimos
    	String html = htmlContent.toString();
    	String cabeceras = html.split("\\r\\n\\r\\n")[0];
    	String cuerpo = html.split("\\r\\n\\r\\n",2)[1];
    	txtCabeceras.setText(cabeceras);
    	editorPane.setText(cuerpo);
    }
    
    private static String requestMessage(String url) {
    	return "GET " +url+ " HTTP/1.1\r\n" 
    			+ "Host: "+SERVERNAME+":"+SERVERPORT+"\n"
				+ "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/117.0\n"
				+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n"
				+ "Accept-Language: en-US,en;q=0.5\n"
				+ "Accept-Encoding: gzip, deflate, br\n"
				+ "Connection: keep-alive\n"
				+ "Cookie: feo=true\n"
				+ "Upgrade-Insecure-Requests: 1\n"
				+ "Sec-Fetch-Dest: document\n"
				+ "Sec-Fetch-Mode: navigate\n"
				+ "Sec-Fetch-Site: none\n"
				+ "Sec-Fetch-User: ?1\n"
				+ "\n";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClienteGUI c = new ClienteGUI();
            c.setVisible(true);
        });
    }
}
