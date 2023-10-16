package pruebas_concurrencia_gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Viewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HTML Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Crear un JEditorPane para mostrar HTML
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);

        try {
            // Cargar una página HTML desde una URL (cambia la URL a tu contenido HTML)
            URL url = new URL("https://www.ejemplo.com/mi_pagina.html");
            editorPane.setPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Agregar el JEditorPane a la ventana
        frame.getContentPane().add(new JScrollPane(editorPane));

        frame.setVisible(true);
    }
}
