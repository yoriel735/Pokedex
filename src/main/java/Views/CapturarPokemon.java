/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controladores.JPanelimagen;
import Controladores.LectorJSONPokeApi;
import Entidades.Pokemon;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author yoriel
 */
public class CapturarPokemon extends javax.swing.JFrame {

    /**
     * Creates new form CapturarPokemon
     */
    public CapturarPokemon() {
        initComponents();
        imagenFondoCaptura();
        imagenCuadroCombate();
        mostrarImagenPokemonAleatorio();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cuando cierre la captura, liberará recursos y se cerrará correctamente
        
     ajustarComponentes();
}

private void ajustarComponentes() {
    
   
    // Colocar el cuadro de texto debajo de la imagen del Pokémon
    Texto.setBounds(100, 200, 500, 50); // Ajusta la posición y tamaño de Texto
    CuadroCombate.setBounds(100, 260, 500, 100); // Ajusta la posición y tamaño de CuadroCombate
    
    // Asegurarte de que los botones no se solapen
    jButton1.setBounds(100, 400, 100, 50); // Ajusta la posición y tamaño de jButton1
    jButton2.setBounds(250, 400, 100, 50); // Ajusta la posición y tamaño de jButton2
    
    // Reajustar la imagen del Pokémon
    ImagenPokemonAle.setBounds(200, 50, 325, 325); // Ajusta la posición y tamaño de ImagenPokemonAle
}

    private void imagenFondoCaptura() {
        String[] fondos = {
            "/Fotos/Cueva.png",
            "/Fotos/LlanuraOscura.png",
            "/Fotos/Llanura.png",
            "/Fotos/BatallaAnimada.gif",
            "/Fotos/Playa.jpg",
            "Fotos/FondoPrueba.png"
        };

        int fondoIndex = new Random().nextInt(fondos.length);
        String ruta = fondos[fondoIndex];

        JPanelimagen fondo = new JPanelimagen(Fondos, ruta);
        Fondos.add(fondo);
        Fondos.repaint();

        Fondos.setOpaque(false);
        Fondos.setBorder(null);
        Fondos.setBackground(new Color(0, 0, 0));
    }

    private void mostrarImagenPokemonAleatorio() {
        String nombrePokemonAleatorio = "";
        try {
            // Obtener nombre aleatorio
            nombrePokemonAleatorio = LectorJSONPokeApi.obtenerPokemonAleatorio();

            // Construir la URL del sprite
            String urlImagen = "https://img.pokemondb.net/sprites/home/normal/" + nombrePokemonAleatorio.toLowerCase() + ".png";

            // Descargar la imagen
            ImageIcon pokemonRandom = new ImageIcon(new URL(urlImagen));

            // Ajustar el tamaño de la imagen
            Image image = pokemonRandom.getImage().getScaledInstance(325, 325, Image.SCALE_SMOOTH);
            ImagenPokemonAle.setIcon(new ImageIcon(image));

            // Actualizar el texto del JLabel con el nombre del Pokémon
            // Colocar el texto del Pokémon en el JLabel de texto
            Texto.setText("¡" + nombrePokemonAleatorio + " ha aparecido!");

            // Configurar el texto del JLabel (Texto del Pokémon)
            Texto.setHorizontalAlignment(SwingConstants.CENTER); // Texto centrado horizontalmente
            Texto.setVerticalAlignment(SwingConstants.BOTTOM);   // Texto debajo de la imagen

            // Establecer fondo transparente y sin borde
            Texto.setOpaque(false);
            Texto.setBorder(null);
            Texto.setBackground(new Color(0, 0, 0, 0)); // Transparente

            // Forzar repintado
            Texto.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de " + nombrePokemonAleatorio, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imagenCuadroCombate() {
        CuadroCombate.setIcon(new ImageIcon(getClass().getResource("/Fotos/cuadroTexto.jpg")));

        // Asegúrate de que no tenga fondo, borde, etc.
        CuadroCombate.setOpaque(false);
        CuadroCombate.setBorder(null);
        CuadroCombate.setBackground(new Color(0, 0, 0, 0)); // Transparente, si aplica

        // Forzar repintado
        CuadroCombate.repaint();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondos = new javax.swing.JPanel();
        ImagenPokemonAle = new javax.swing.JLabel();
        CuadroCombate = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Texto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        CuadroCombate.setBackground(new java.awt.Color(204, 102, 255));

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        Texto.setText("jLabel1");

        javax.swing.GroupLayout FondosLayout = new javax.swing.GroupLayout(Fondos);
        Fondos.setLayout(FondosLayout);
        FondosLayout.setHorizontalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CuadroCombate, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondosLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(Texto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ImagenPokemonAle, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        FondosLayout.setVerticalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondosLayout.createSequentialGroup()
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ImagenPokemonAle, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Texto)
                        .addGap(140, 140, 140)))
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                    .addComponent(CuadroCombate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CuadroCombate;
    private javax.swing.JPanel Fondos;
    private javax.swing.JLabel ImagenPokemonAle;
    private javax.swing.JLabel Texto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    // End of variables declaration//GEN-END:variables
}
