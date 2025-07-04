/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladoresExtras;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author yoriel
 */
public class JPanelimagen extends JLabel {
    //Esta es una clase hace que los Jpanel extiendan de JLabel, lo que les permite
    //poder agregarle imagenes comodamente en base a la ruta que le de
   private ImageIcon img;

    public JPanelimagen(JPanel panel, String path) {
        this.img = null;
        if (getClass().getResource(path) != null) {
            this.img = new ImageIcon(getClass().getResource(path));
        } else {
            System.out.println("No se encontró la imagen en la ruta: " + path);
        }
        this.setSize(panel.getWidth(), panel.getHeight());
    }

    //y esto es para tener mas control sobre la imagen
    @Override
    public void paint(Graphics g) {
        if (img != null) {
            g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        super.paint(g);
    }
}
    
    

