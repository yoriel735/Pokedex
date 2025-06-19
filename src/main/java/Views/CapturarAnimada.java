package Views;

import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author yoriel
 */
//Todo lo necesario para que se muestren los gifs
public class CapturarAnimada extends javax.swing.JDialog {

    /**
     * Creates new form CapturarAnimada 
     */
  public CapturarAnimada(JFrame parent, String rutaGif, int tiempoMs, Runnable despuesDeGif) {
        super(parent, true);
        setUndecorated(true);
        setSize(parent.getWidth(), parent.getHeight());
        setLocationRelativeTo(parent);

        try {
            URL gifURL = getClass().getResource(rutaGif);
            if (gifURL == null) {
                System.err.println("No se encontro el GIF en " + rutaGif);
                return;
            }

            ImageIcon gifIcon = new ImageIcon(gifURL);
            JLabel gifLabel = new JLabel(gifIcon);
            gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(gifLabel, BorderLayout.CENTER);

            Timer timer = new Timer(tiempoMs, e -> {
                ((Timer) e.getSource()).stop();
                dispose(); //Cerrar la ventana emergente al terminar el GIF
                despuesDeGif.run(); //Ejecutar accion despues del GIF
            });

            timer.setRepeats(false);
            timer.start();
            setVisible(true);

        } catch (Exception e) {
            System.out.println("Error al mostrar el GIF: " + e.getMessage());
        }
    }


   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 911, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
