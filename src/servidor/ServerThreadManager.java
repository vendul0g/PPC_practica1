package servidor;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import historyMap.HistoryMap;
import httpMessages.*;
import resources.HTMLFile;


public class ServerThreadManager extends Thread{
	//Variables globales
	private static boolean VERBOSE = true;
	private static final String LAST_RESOURCE = "last_resource=";
	private static final String HISTORY = "history=";
	private static final int BUFSIZE = 1024;
	public static final int TIMEOUT = 1000*10;
		
	//Atributos
	private Socket s;
	private BufferedReader istream;
	private BufferedWriter ostream;
	private boolean isOpen;
	private HistoryMap historyMap;
	
	//Constructor
	public ServerThreadManager(Socket s) {
		if(VERBOSE)	System.out.println("\tConectado "+ s.getInetAddress()+":"+s.getPort());
		
		this.s = s;
		//Establecemos un timeout
		try {
			this.s.setSoTimeout(TIMEOUT);
		} catch (SocketException e) {
			System.err.println("Error al establecer el timeout");
		}
		
		this.isOpen = true;
		this.historyMap = null;
	}
	
	//Funcionalidad
	public void run() {
		//Declaracion de variables
		String message;
		String answer;
		
		//Inicializamos los buffers de lectura/escritura
		createBuffers();
		
		while(isOpen) {
			//Leemos la petición
			if( (message = getMessage()).equals(""))
				break;
			//Procesamos la petición para crear una respuesta		
			answer = proccesMessage(message);
			
			//Enviamos respuesta
			sendMessage(answer);
		}
		
		if(VERBOSE) System.out.println("\tCerramos conexión\n------------------------------------------");
		//Cerramos el socket
		closeSocket();
	}
	
	private String proccesMessage(String m) {
		//Comprobamos el formato de la petición
		if(!checkFormat(m)) {
			if(VERBOSE) System.out.println("\t\tBad Request");
			return new HTTPResponse400().getMessage();
		}
		
		//Procesamos la petición
		String[] lines = m.split(HTTPMessage.RETCAR);
		String[] reqline = lines[0].split(" ");
		
		//Comprobamos el método
		if( ServerMethod.isMethod(reqline[0]) == null) {
			if(VERBOSE) System.out.println("\t\tMethod Not Allowed");
			return new HTTPResponse405().getMessage();
		}
		
		//Comprobamos la version
		if(!HTTPMessage.isVersion(reqline[2])) {
			if(VERBOSE) System.out.println("\t\tVersion not supported");
			return new HTTPResponse505().getMessage();
		}
		
		//Comprobamos el tipo de conexión
		this.isOpen = isConnectionOpen(lines);
		
		//Comprobar las cookies
		List<String> cookies = proccesCookies(lines);
		if(cookies == null) {//Petición incorrecta
			if(VERBOSE) System.out.println("\t\tBad Request (Cookies)");
			return new HTTPResponse400().getMessage();
		}
		
		//Comprobamos URL
		String url = getURL(reqline[1]);
		if(VERBOSE) System.out.println("\t\tPetición correcta (200) --> "+url);
		
		//Recuperamos el fichero
		HTMLFile file = new HTMLFile(url, historyMap);
		
		//Enviamos la respuesta
		HTTPResponse200 ok = new HTTPResponse200();
		
		ok.setCookies(cookies);
		ok.setContentLenght(file.length());
		ok.setCuerpo(file.getFile());

		return ok.getMessage();
	}
	
	private boolean isConnectionOpen(String[] lines) {
		//Recorremos las cabeceras buscando "Connection"
		for(int i=1; i<lines.length; i++) {
			if(lines[i].split(":")[0].equals("Connection")) {
				if(lines[i].split(":")[1].equals(" keep-alive")) {
					return true;
				}else {
					return false;
				}
			}
		}
		return false;
	}
	
	private List<String> proccesCookies(String[] lines){
		String historyString;
		List<String> cookies = new LinkedList<>();
		String lastResource = lines[0].split(" ")[1];
		String[] aux = new String[5];
		boolean isLastResource = false;
		boolean isHistory = false;
		
		if(lastResource.equals("/")) lastResource+="index.html";
		
		/**
		 * Ejemplo de línea:
		 * Cookie: last_resource=/index.html; history=JTdCJTJGaW5kZXguaHRtbCUzRDElN0Q=
		 */
		
		//Recorremos las cabeceras buscando las cookies del servidor
		for(int i=1; i<lines.length && !isLastResource && !isHistory; i++) {
			if(lines[i].split(":")[0].equals("Cookie")) {
				aux = lines[i].split(": ")[1].split("; ");// aux = [last_resource=/index.html,history=JTdCJTJGaW5kZXguaHRtbCUzRDElN0Q=]
				for(String s : aux) {
					if(s.split("=")[0].equals("last_resource")) {
						isLastResource = true;
					} else if (s.split("=")[0].equals("history")) {
						isHistory = true; 
						historyString = fromBase64(s.split("=")[1]);
						historyMap = new HistoryMap(historyString);
						if (historyMap == null)
							return null;
					}					
				}
			}
		}
		
		//Si no están las cookies se las añadimos
		if(!isLastResource || !isHistory) {
			//Construimos la cookie history
			historyString = "{"+lastResource+"=1}";
			//Actualizamos el mapa
			historyMap = new HistoryMap(historyString);
			
			//Formateamos la cookie
			historyString = toBase64(historyString);
			
			//Añadimos las 2 cookies a la lista de cookies que se mandan 
			cookies.add(LAST_RESOURCE+lastResource);
			cookies.add(HISTORY+historyString);
			
		}else { //Si las cookies estaban, partimos de su contenido (history)
			if(historyMap == null) {
				System.err.println("Cookies parseadas vacías");
				return Collections.emptyList();
			}
			
			//Actualizamos el mapa si el recurso ya existía
			if (historyMap.get(lastResource) != null) {
				//Actualizamos el mapa
				historyMap.put(lastResource, historyMap.get(lastResource)+1);
			}else {
			//Actualizamos el mapa para el nuevo recurso
				historyMap.put(lastResource, 1);
				
			}
			//Creamos la cookie en Base64
			historyString = toBase64(historyMap.getHistoryMap());
			
			//Añadimos las cookies a la lista
			cookies.add(LAST_RESOURCE+lastResource);
			cookies.add(HISTORY+historyString);		
			
		}
		return cookies;
	}
	
	private String toBase64(String m) {
		String urlEncoded = "", base64Encoded;
		try {
			urlEncoded = URLEncoder.encode(m, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Fallo al codificar (URLEncoding)");
		}
		base64Encoded = Base64.getEncoder().encodeToString(urlEncoded.getBytes());
		return base64Encoded;
	}
	
	private String fromBase64(String base64String) {
	    byte[] decodedBytes = Base64.getDecoder().decode(base64String);
	    String urlDecoded = null;
	    try {
	        urlDecoded = URLDecoder.decode(new String(decodedBytes, StandardCharsets.UTF_8.toString()), StandardCharsets.UTF_8.toString());
	    } catch (UnsupportedEncodingException e) {
	        System.err.println("Fallo al decodificar (URLDecoding)");
	    } catch(IllegalArgumentException e) {
	    	System.err.println("Fallo al decodificar (URLDecoding)");
	    }
	    return urlDecoded;
	}

	
	private void createBuffers() {
		try {
			istream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			ostream = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(IOException e) {
			System.err.println("Fallo al intentar crear sockets de lectura y escritura");
			return;
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
				} catch (SocketTimeoutException e) {
					if(VERBOSE) System.err.println("\t\tTimeout Excedido");
					this.isOpen = false;
					return "";
				}
				
				if(bytesRead > 0) {
					message.append(buff, 0, bytesRead);
				}
			}while(bytesRead == BUFSIZE);

			return message.toString();
		} catch (IOException e) {
//			e.printStackTrace();
			System.err.println("\t\tLectura del socket vacía");
			return "";
		}
	}
	
	private synchronized void sendMessage(String url) {
		try {
			ostream.write(url);
			ostream.newLine();
			ostream.flush();
		} catch (IOException e) {
			System.err.println("Error en la escritura del socket");	
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
	
	private boolean checkFormat(String m) {
		Pattern pattern = Pattern.compile(HTTPRequestMessage.HTTP_REQ_REGEX);
		Matcher matcher = pattern.matcher(m);
		if(!matcher.find()) {
			return false;
		}
		return true;
	}
	
	private String getURL(String url) {
		if(url.equals("/")) {
			return "/index.html";
		}
		return url;
	}
}
