package cliente;

import java.util.Scanner;

public class LanzarCliente {
	public static void main (String[] args) {
		Scanner s = new Scanner(System.in);
		String mode;
		
		System.out.println("Qué tipo de conexión quieres?");
		System.out.println(" s\tsegura (HTTPS)\n i\tinsegura (HTTP)");
		System.out.print("--> ");
		
		//Procesamos solicitud
		mode = s.nextLine();
		if(mode.equalsIgnoreCase("i")) {
			Cliente c = new ClienteHttp();
			c.start();
		}else if (mode.equalsIgnoreCase("s")) {
			ClienteHttps cs = new ClienteHttps();
			cs.start();
		}else {
			System.out.println("Modo incorrecto - Saliendo...");
		}

		//Cerramos el scanner
		s.close();
	}
}