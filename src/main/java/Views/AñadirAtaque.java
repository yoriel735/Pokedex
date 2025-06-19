/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import Controladores.AtaquesController;
import Entidades.Ataque;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author yoriel
 */
public class AñadirAtaque extends javax.swing.JDialog {

    private JTextField nombreField, tipoField, categoriaField, efectoField, potenciaField, ppField;
    private boolean confirmado = false;
    private Ataque nuevoAtaque;
    private final AtaquesController ataquesController;
    

    public AñadirAtaque(Frame parent) {
        super(parent, "Añadir Ataque", true);
        this.ataquesController = new AtaquesController();      
        initComponents(); // Llamamos a los componentes primero
        AñadirAtaques();  // Después de inicializar, agregamos los campos
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void AñadirAtaques() {
        //Panel para los campos de entrada (configurado con GridLayout para dos columnas)
        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));

        nombreField = new JTextField(20);
        tipoField = new JTextField(20);
        categoriaField = new JTextField(20);
        efectoField = new JTextField(20);
        potenciaField = new JTextField(20);
        ppField = new JTextField(20);

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(nombreField);
        panelCampos.add(new JLabel("Tipo:"));
        panelCampos.add(tipoField);
        panelCampos.add(new JLabel("Categoría:"));
        panelCampos.add(categoriaField);
        panelCampos.add(new JLabel("Efecto:"));
        panelCampos.add(efectoField);
        panelCampos.add(new JLabel("Potencia:"));
        panelCampos.add(potenciaField);
        panelCampos.add(new JLabel("PP:"));
        panelCampos.add(ppField);

        //Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        //Accion para el boton Guardar
        btnGuardar.addActionListener(e -> guardarAtaque());

        //Accion para el boton Cancelar
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        // Configuracion del layout principal del diálogo
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panelCampos, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        pack();
    }
    

    private void guardarAtaque() {
        try {
            //Validar que ningun campo esté vacio
            if (nombreField.getText().isEmpty() || tipoField.getText().isEmpty() || categoriaField.getText().isEmpty()
                    || efectoField.getText().isEmpty() || potenciaField.getText().isEmpty() || ppField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Validar que potencia y PP sean numeros
            int potencia, pp;
            try {
                potencia = Integer.parseInt(potenciaField.getText());
                pp = Integer.parseInt(ppField.getText());
                if (potencia < 0 || pp < 0) {
                    throw new NumberFormatException("Potencia y PP deben ser números positivos.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Potencia y PP deben ser valores numéricos positivos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //Crear el ataque
            nuevoAtaque = new Ataque();
            nuevoAtaque.setNombreAtaque(nombreField.getText());
            nuevoAtaque.setTipo(tipoField.getText());
            nuevoAtaque.setCategoria(categoriaField.getText());
            nuevoAtaque.setEfecto(efectoField.getText());
            nuevoAtaque.setPotencia(potencia);
            nuevoAtaque.setPp(pp);

            //Verificar si el ataque ya existe antes de guardarlo
            if (ataquesController.ataqueExiste(nuevoAtaque)) {
                JOptionPane.showMessageDialog(this, "Este ataque ya existe en la base de datos.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //Guardar el ataque
            ataquesController.guardarNuevoAtaque(nuevoAtaque);
            confirmado = true;
            JOptionPane.showMessageDialog(this, "Ataque guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); //se cierra la tabla al guardar ataque
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el ataque: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public boolean isConfirmado() {
        return confirmado;
    }
    
    public Ataque getAtaque() {
        return nuevoAtaque;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

