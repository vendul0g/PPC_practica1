//package dev;
//
//import java.net.*;
//import java.io.*;
//import javax.swing.*;
//
//import httpMessages.HTTPGetMessage;
//import socketManager.SocketManager;
//
//import java.awt.*;
//import java.awt.event.*;
//
//public class ClienteGUIDev extends JFrame implements ActionListener{
//	//Variables globales
//	private static final String SERVERNAME = "localhost";
//	private static final int SERVERPORT = 9999;
//	private static final long serialVersionUID = 1L;
//
//	//Atributos
//	private Socket s;
//	private SocketManager sm;
//	private JTextField barra;
//	private JButton enter;
//	private JEditorPane editorPane;
//	private JPanel center;
//	private JTextArea txtCabeceras;
//	
//	//Constructor
//	public ClienteGUIDev() {
//		//Inicializa elementos de red
//		try {
//			s = new Socket (SERVERNAME, SERVERPORT);
//		} catch(IOException e) {
//			System.err.println("No se puede conectar con el servidor "+SERVERNAME+SERVERPORT+"\n¿Inactivo?");
//		}
//		sm = new SocketManager(s);
//		
//		// Configura el JFrame
//        setTitle("!Google");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(900, 1200);
//
//        // Crea la barra superior
//        JPanel topPanel = new JPanel();
//        barra = new JTextField(40);
//        barra.setText("/");
//        enter = new JButton("Search");
//        this.getRootPane().setDefaultButton(enter);
//        enter.addActionListener(this);
//        topPanel.add(barra);
//        topPanel.add(enter);
//
//        // Crea el componente para mostrar HTML
//        editorPane = new JEditorPane();
//        editorPane.setContentType("text/html");
//        editorPane.setEditable(false);
//
//        // Agrega los componentes al JFrame
//        center = new JPanel();
//        JScrollPane scrollCenter = new JScrollPane(editorPane); 
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(topPanel, BorderLayout.NORTH);
//        getContentPane().add(center, BorderLayout.CENTER);
//        center.setLayout(new BorderLayout());
//        
//        JPanel panelCabecera = new JPanel();
//        center.add(panelCabecera, BorderLayout.NORTH);
//        center.add(scrollCenter, BorderLayout.CENTER);
//        
//        txtCabeceras = new JTextArea();
//        txtCabeceras.setEditable(false);
//        panelCabecera.setLayout(new BoxLayout(panelCabecera, BoxLayout.Y_AXIS));
//        panelCabecera.add(txtCabeceras);
//	}
//	
//	//Funcionalidad
//	public void actionPerformed(ActionEvent e) {
//		if (e.getSource() == enter) {
//			String answer;
//
//			//Manejo de GUI
//			String url = barra.getText();
//			barra.setText("/");
//			
//			//Envío de petición
//			sm.sendMessage(new HTTPGetMessage(url, SERVERNAME+":"+SERVERPORT).getMessage());
//			
//			//Recibimos respuesta
//			answer = sm.getMessage();
//			
//			//Procesar en GUI
//			proccesAnswerGUI(answer);
//		}
//	}
//	
//	public void proccesAnswerGUI(String answer) {
//		//Borramos la pantalla
//		editorPane.setText("");
//		txtCabeceras.setText("");
//		
//		//Actualizamos
//		String cabeceras = answer.split("\\r\\n\\r\\n")[0];
//		String cuerpo = answer.split("\\r\\n\\r\\n", 2)[1];
//		txtCabeceras.setText(cabeceras);
//		editorPane.setText(cuerpo);
//	}
//	
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> {
//            ClienteGUIDev c = new ClienteGUIDev();
//            c.setVisible(true);
//        });
//	}
//}
