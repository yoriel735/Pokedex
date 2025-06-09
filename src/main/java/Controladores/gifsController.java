/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author yoriel
 */
public class gifsController {
     public static void mostrarGifTemporal(JLabel label, String rutaGif, int tiempoMs, Runnable despuesDeGif) {
        try {
            URL gifURL = gifsController.class.getResource(rutaGif);
            if (gifURL == null) {
                System.err.println(" No se encontró el GIF en " + rutaGif);
                return;
            }

            ImageIcon gifIcon = new ImageIcon(gifURL);
            label.setIcon(gifIcon);

            // Después de 'tiempoMs' milisegundos, ejecutamos la siguiente acción
            Timer timer = new Timer(tiempoMs, e -> {
                ((Timer) e.getSource()).stop();
                despuesDeGif.run(); // Ejecutar acción posterior al GIF
            });

            timer.setRepeats(false);
            timer.start();
        } catch (Exception e) {
            System.out.println("Error al mostrar el GIF: " + e.getMessage());
        }
    }

    
    
}
