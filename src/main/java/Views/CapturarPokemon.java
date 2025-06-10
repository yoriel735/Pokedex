/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controladores.EntrenadorController;
import Controladores.JPanelimagen;
import Controladores.LectorJSONPokeApi;
import Controladores.PokemonController;
import Controladores.gifsController;
import Entidades.Entrenador;
import Entidades.Pokemon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

/**
 *
 * @author yoriel
 */
public class CapturarPokemon extends javax.swing.JFrame {

    private Pokemon pokemonActual; // 🔹 Aquí almacenamos el Pokémon salvaje actual
    private PokemonController pokemonController;
    private MenuEntrenadores menuEntrenadores;
    private String nombreEntrenador;
    private Pokedex pokedexActiva; // 🔥 Variable para rastrear la Pokédex abierta
private static CapturarPokemon zonaCapturaActual;
    /**
     * Creates new form CapturarPokemon
     */
    public CapturarPokemon(MenuEntrenadores menuEntrenadores, String nombreEntrenador) {
        initComponents();
        this.menuEntrenadores = menuEntrenadores;
        this.nombreEntrenador = nombreEntrenador; // ✅ Guardamos el nombre correctamente
        pokemonController = new PokemonController();
        imagenFondoCaptura();
        configurarCuadroNivel();
        configurarCuadroCombate();
        mostrarPokemonAleatorio();
        configurarArrastrePokeball(Pokeball);
        ajustarImagenPokeball(Pokeball, "/Fotos/pokeball.png", 100, 100);
        configurarDropPokemon();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cuando cierre la captura, liberará recursos y se cerrará correctamente
//        mostrarDetallesPokemon();
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

    private void mostrarImagenPokemon() {
        if (pokemonActual == null) {
            JOptionPane.showMessageDialog(this, "Error: No hay Pokémon generado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            //  Obtener el nombre del Pokémon desde `pokemonActual`
            String nombrePokemon = pokemonActual.getNombrePokemon();

            // Construir la URL del sprite
            String urlImagen = "https://img.pokemondb.net/sprites/home/normal/" + nombrePokemon.toLowerCase() + ".png";

            // Descargar la imagen
            ImageIcon pokemonRandom = new ImageIcon(new URL(urlImagen));

            // Ajustar el tamaño de la imagen
            Image image = pokemonRandom.getImage().getScaledInstance(325, 325, Image.SCALE_SMOOTH);
            ImagenPokemonAle.setIcon(new ImageIcon(image));

            // Actualizar el texto en la interfaz
            Texto.setText("¡Un " + nombrePokemon + " salvaje ha aparecido!");
            Texto.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de " + pokemonActual.getNombrePokemon(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarPokemonAleatorio() {
        pokemonActual = pokemonController.generarPokemonAleatorio(); //Obtener Pokémon desde el controlador

        if (pokemonActual != null) {
            mostrarImagenPokemon();  // Ahora mostramos la imagen correctamente
            mostrarDetallesPokemon(pokemonActual);
        } else {
            JOptionPane.showMessageDialog(this, "Error al generar Pokémon salvaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarCuadroCombate() {
        // Crear el fondo dentro del panel CuadroCombates
        JPanelimagen imagenFondo = new JPanelimagen(CuadroCombates, "/Fotos/cuadroTexto.jpg");

        CuadroCombates.add(imagenFondo).repaint();

        //estos 3, es para que se quite cualquier fondo o borde del label que use
        //la ventaja del label, es que funciona como fondo, puedo poner botones encima
        CuadroCombates.setOpaque(false);
        CuadroCombates.setBorder(null);
        CuadroCombates.setBackground(new Color(0, 0, 0));

    }

    private void configurarCuadroNivel() {
        // Crear el fondo dentro del panel CuadroCombates
        JPanelimagen imagenFondo = new JPanelimagen(CuadroNivel, "/Fotos/CuadroNivel.png");

        CuadroNivel.add(imagenFondo).repaint();

        //estos 3, es para que se quite cualquier fondo o borde del label que use
        //la ventaja del label, es que funciona como fondo, puedo poner botones encima
        CuadroNivel.setOpaque(false);
        CuadroNivel.setBorder(null);
        CuadroNivel.setBackground(new Color(0, 0, 0));

    }

    private void mostrarDetallesPokemon(Pokemon p) {
        NivelPokemon.setText(": " + p.getNivel());

        NombrePokemon.setText(p.getNombrePokemon());

    }

    private void ajustarImagenPokeball(JLabel Pokeball, String rutaImagen, int ancho, int alto) {
        try {
            //Cargar la imagen
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
            Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

            //Asignar la imagen ajustada al `JLabel`
            Pokeball.setIcon(new ImageIcon(imagen));
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    private void configurarArrastrePokeball(JLabel Pokeball) {
        Pokeball.setTransferHandler(new TransferHandler("icon"));

        Pokeball.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TransferHandler handler = Pokeball.getTransferHandler();
                handler.exportAsDrag(Pokeball, e, TransferHandler.COPY);
            }
        });
    }

    private void configurarDropPokemon() {
        ImagenPokemonAle.setDropTarget(new DropTarget() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                Transferable transferable = dtde.getTransferable();
                try {
                    // 🔥 Verificar qué formato se está recibiendo
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        System.out.println("Formato recibido: " + flavor.getMimeType());
                    }

                    // 🔥 Usar `javaJVMLocalObjectFlavor` en lugar de `imageFlavor`
                    Object data = transferable.getTransferData(new DataFlavor("application/x-java-jvm-local-objectref; class=javax.swing.Icon"));

                    if (data instanceof Icon) { //Verificar que es un `Icon`
                        iniciarCapturaVisual(); // Lanzamos los GIFs de captura
                    } else {
                        mostrarCapturaFallida(); //Captura fallida si el objeto no es válido
                    }
                } catch (Exception e) {
                    mostrarCapturaFallida(); //Captura fallida por error en la detección
                    e.printStackTrace();
                }
            }
        });
    }

  private void iniciarCapturaVisual() {
    System.out.println("🔥 Iniciando captura visual...");

    new CapturarAnimada(this, "/Fotos/lanzarPokeball.gif", 1500, () -> {
        new CapturarAnimada(this, "/Fotos/PokemonCapturado.gif", 10000, () -> {
            
            // Mostrar el diálogo de confirmación con CapturarPokemon.this como padre.
            int opcion = JOptionPane.showConfirmDialog(
                    CapturarPokemon.this, 
                    "¡Pokémon capturado! ¿Quieres asignarle un alias?", 
                    "Asignar alias", 
                    JOptionPane.YES_NO_OPTION);
            System.out.println("🔥 El usuario eligió: " + (opcion == JOptionPane.YES_OPTION ? "Sí" : "No"));
            
            String alias;
            if (opcion == JOptionPane.YES_OPTION) {
                // Se usa CapturarPokemon.this como parent para que el diálogo quede en primer plano.
                alias = JOptionPane.showInputDialog(
                        CapturarPokemon.this,
                        "Introduce el alias para tu Pokémon:",
                        "Asignar alias",
                        JOptionPane.QUESTION_MESSAGE);
                if (alias == null || alias.trim().isEmpty()) {
                    alias = "Sin alias";
                }
            } else {
                alias = "Sin alias";
            }
            
            // Asignar alias y guardar el Pokémon.
            pokemonActual.setAlias(alias);
            System.out.println("✅ Alias asignado: " + alias);
            guardarPokemonEnEntrenador();

            // Una vez se tiene el alias y se guarda, cerramos la ventana de captura y abrimos la Pokédex.
            dispose();
        
        });
    });
}



    private void guardarPokemonEnEntrenador() {
        System.out.println("🔥 Método guardarPokemonEnEntrenador() ejecutado.");

        EntrenadorController ec = new EntrenadorController();
        Integer idEntrenador = ec.obtenerIdPorNombre(nombreEntrenador); // ✅ Obtener ID del entrenador

        if (idEntrenador == null) {
            System.out.println("❌ Error: El ID del entrenador es NULL.");
            return;
        }

        System.out.println("✅ ID del entrenador: " + idEntrenador);
        System.out.println("✅ Pokémon a guardar: " + pokemonActual.getNombrePokemon());

        ec.agregarPokemonACaptura(idEntrenador, pokemonActual); // ✅ Guardar el Pokémon con el entrenador
    }

    
    private void mostrarCapturaFallida() {
        JOptionPane.showMessageDialog(this, "¡La captura falló! Intenta de nuevo.", "Captura fallida", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Fondos = new javax.swing.JPanel();
        ImagenPokemonAle = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        CuadroCombates = new javax.swing.JPanel();
        Texto = new javax.swing.JLabel();
        CuadroNivel = new javax.swing.JPanel();
        NivelPokemon = new javax.swing.JLabel();
        NombrePokemon = new javax.swing.JLabel();
        Pokeball = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("jButton2");

        CuadroCombates.setBackground(new java.awt.Color(255, 51, 0));

        Texto.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        Texto.setText("Un pokemon salvaje aparecio");

        javax.swing.GroupLayout CuadroCombatesLayout = new javax.swing.GroupLayout(CuadroCombates);
        CuadroCombates.setLayout(CuadroCombatesLayout);
        CuadroCombatesLayout.setHorizontalGroup(
            CuadroCombatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CuadroCombatesLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(Texto, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(414, Short.MAX_VALUE))
        );
        CuadroCombatesLayout.setVerticalGroup(
            CuadroCombatesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CuadroCombatesLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(Texto)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        CuadroNivel.setBackground(new java.awt.Color(153, 255, 153));

        NivelPokemon.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        NivelPokemon.setText("Nivel");

        NombrePokemon.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        NombrePokemon.setForeground(new java.awt.Color(102, 102, 102));
        NombrePokemon.setText("Nombre Pokemon");

        javax.swing.GroupLayout CuadroNivelLayout = new javax.swing.GroupLayout(CuadroNivel);
        CuadroNivel.setLayout(CuadroNivelLayout);
        CuadroNivelLayout.setHorizontalGroup(
            CuadroNivelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CuadroNivelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(NombrePokemon, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(NivelPokemon)
                .addGap(66, 66, 66))
        );
        CuadroNivelLayout.setVerticalGroup(
            CuadroNivelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CuadroNivelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(CuadroNivelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NombrePokemon)
                    .addComponent(NivelPokemon))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FondosLayout = new javax.swing.GroupLayout(Fondos);
        Fondos.setLayout(FondosLayout);
        FondosLayout.setHorizontalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondosLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(CuadroNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ImagenPokemonAle, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(CuadroCombates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(Pokeball, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(16, Short.MAX_VALUE))))
        );
        FondosLayout.setVerticalGroup(
            FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(ImagenPokemonAle, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE))
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(CuadroNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(FondosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondosLayout.createSequentialGroup()
                        .addComponent(Pokeball, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CuadroCombates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private javax.swing.JPanel CuadroCombates;
    private javax.swing.JPanel CuadroNivel;
    private javax.swing.JPanel Fondos;
    private javax.swing.JLabel ImagenPokemonAle;
    private javax.swing.JLabel NivelPokemon;
    private javax.swing.JLabel NombrePokemon;
    private javax.swing.JLabel Pokeball;
    private javax.swing.JLabel Texto;
    private javax.swing.JButton jButton2;
    // End of variables declaration//GEN-END:variables
}
