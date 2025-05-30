/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Entidades.Ataque;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yoriel
 */
public class AtaquesController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    public List<PokemonAtaque> obtenerAtaquesPorPokemon(int idPokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon");
            query.setParameter("idPokemon", idPokemon);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void guardarAtaqueYPokemon(Pokemon pokemon, Ataque nuevoAtaque) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Verificar si el ataque ya existe
            Query query = em.createQuery("SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre AND a.tipo = :tipo AND a.categoria = :categoria");
            query.setParameter("nombre", nuevoAtaque.getNombreAtaque());
            query.setParameter("tipo", nuevoAtaque.getTipo());
            query.setParameter("categoria", nuevoAtaque.getCategoria());
            List<Ataque> ataquesExistentes = query.getResultList();

            if (!ataquesExistentes.isEmpty()) {
                nuevoAtaque = ataquesExistentes.get(0); // Usa el ataque existente
            } else {
                em.persist(nuevoAtaque);
                em.flush();
            }

            // Verificar si el Pokémon ya tiene el ataque
            Query queryAtaqueExistente = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon AND pa.ataque.idAtaque = :idAtaque");
            queryAtaqueExistente.setParameter("idPokemon", pokemon.getIdPokemon());
            queryAtaqueExistente.setParameter("idAtaque", nuevoAtaque.getIdAtaque());
            List<PokemonAtaque> ataquesDelPokemon = queryAtaqueExistente.getResultList();

            if (ataquesDelPokemon.isEmpty()) {
                PokemonAtaque nuevoPokemonAtaque = new PokemonAtaque(pokemon, nuevoAtaque, "Mov. TM");
                em.persist(nuevoPokemonAtaque);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public boolean ataqueExiste(Ataque ataque) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre AND a.tipo = :tipo AND a.categoria = :categoria");
            query.setParameter("nombre", ataque.getNombreAtaque());
            query.setParameter("tipo", ataque.getTipo());
            query.setParameter("categoria", ataque.getCategoria());
            return !query.getResultList().isEmpty();
        } finally {
            em.close();
        }
    }

    public void guardarNuevoAtaque(Ataque nuevoAtaque) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nuevoAtaque);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
   public void eliminarAtaque(int idPokemon, int idAtaque) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        // 🔎 Buscar la relación específica entre el Pokémon y el ataque
        Query queryRelacion = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon AND pa.ataque.idAtaque = :idAtaque");
        queryRelacion.setParameter("idPokemon", idPokemon);
        queryRelacion.setParameter("idAtaque", idAtaque);
        List<PokemonAtaque> relaciones = queryRelacion.getResultList();

        // 📌 Si existe la relación, eliminarla
        if (!relaciones.isEmpty()) {
            for (PokemonAtaque pa : relaciones) {
                em.remove(pa);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Este Pokémon no tiene asignado este ataque.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        em.flush(); // ✅ Asegurar que la BD actualice los cambios antes de continuar

        // 🔎 Comprobar si el ataque sigue asignado a otros Pokémon
        Query queryOtrosUsos = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.ataque.idAtaque = :idAtaque");
        queryOtrosUsos.setParameter("idAtaque", idAtaque);
        List<PokemonAtaque> usosRestantes = queryOtrosUsos.getResultList();

        // 📌 Si el ataque ya no está asignado a ningún Pokémon, preguntar si se quiere eliminar de `ataques`
        if (usosRestantes.isEmpty()) {
            int opcion = JOptionPane.showConfirmDialog(null, "Este ataque ya no está asignado a ningún Pokémon. ¿Quieres eliminarlo de la base de datos?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                Ataque ataque = em.find(Ataque.class, idAtaque);
                if (ataque != null) {
                    em.remove(ataque);
                }
            }
        }

        em.getTransaction().commit();
        JOptionPane.showMessageDialog(null, "Ataque eliminado correctamente del Pokémon.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al eliminar el ataque: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        em.close();
    }
   }
}