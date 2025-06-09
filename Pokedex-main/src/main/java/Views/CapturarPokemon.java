/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controladores.JPanelimagen;
import java.awt.Color;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondos = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout FondosLayout = new javax.swing.GroupLayout(Fondos);
        Fondos.setLayout(FondosLayout);
        FondosLayout.setHorizontalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1163, Short.MAX_VALUE)
        );
        FondosLayout.setVerticalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
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
    private javax.swing.JPanel Fondos;
    // End of variables declaration//GEN-END:variables
}
