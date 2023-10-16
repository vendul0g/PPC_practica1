package pruebas_juguete;

import java.util.Scanner;

public class CorreoCliente {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Escribe tu mensaje (o pulsa Ctrl+C para salir): ");
            if (scanner.hasNextLine()) {
                String mensaje = scanner.nextLine();
                if (mensaje.equalsIgnoreCase("exit")) {
                    System.out.println("Saliendo del cliente de correo.");
                    break;
                }
                // Aquí puedes procesar el mensaje (enviarlo por correo, etc.).
                System.out.println("Mensaje enviado: " + mensaje);
            }
        }
        
        scanner.close();
    }
}

