/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Views;

import Controladores.EntrenadorController;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Entidades.Entrenador;

/**
 *
 * @author yoriel
 */
public class EditarEntrenador extends javax.swing.JDialog {

    /**
     * Creates new form EditarEntrenador
     */
    private JTextField txtNombre;
    private JButton botonguardar, botoncancelar;
    private String nombreOriginal;
    private Entrenador entrenador; 

    public EditarEntrenador(java.awt.Frame parent, boolean modal, Entrenador entrenador) {
        super(parent, modal);
        this.entrenador = entrenador;
        setupUI();
        cargarDatos();
        setTitle("Editar Entrenador");
        pack();
        this.setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        JLabel lblNombre = new JLabel("Nombre del Entrenador:");
        txtNombre = new JTextField(20);

        botonguardar = new JButton("Guardar");
        botoncancelar = new JButton("Cancelar");

        botonguardar.addActionListener(e -> guardarCambios());
        botoncancelar.addActionListener(e -> cancelar());

        JPanel editar = new JPanel();
        editar.add(lblNombre);
        editar.add(txtNombre);
        editar.add(botonguardar);
        editar.add(botoncancelar);

        this.setContentPane(editar);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void guardarCambios() {
     String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio.");
            return;
        }

        try {
            entrenador.setNomEntrenador(nuevoNombre);
            EntrenadorController controlador = new EntrenadorController();
            controlador.editarNombreEntrenador(entrenador);

            JOptionPane.showMessageDialog(this, "Entrenador actualizado correctamente.");
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el entrenador: " + ex.getMessage());
        }
    
}
    private void cargarDatos() {
        if (entrenador != null) {
            txtNombre.setText(entrenador.getNomEntrenador());
        }
    }
    private void cancelar() {
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
