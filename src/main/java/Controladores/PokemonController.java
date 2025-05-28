package Controladores;

import Entidades.Pokemon;

import javax.persistence.*;
import java.util.List;

public class PokemonController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    public static void crearPokemon(Pokemon pokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pokemon);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void editarPokemon(Pokemon pokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pokemon);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminarPokemon(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pokemon pokemon = em.find(Pokemon.class, id);
            if (pokemon != null) {
                em.remove(pokemon);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Pokemon buscarPokemonPorId(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Pokemon.class, id);
        } finally {
            em.close();
        }
    }

    public List<Pokemon> obtenerPokemonPorEntrenadorId(Integer idEntrenador) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Pokemon> query = em.createQuery(
            "SELECT p FROM Pokemon p WHERE p.entrenador.idEntrenador = :id", Pokemon.class);
        query.setParameter("id", idEntrenador);
        return query.getResultList();
    } finally {
        em.close();
    }
}
    public List<Pokemon> obtenerTodosLosPokemon() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Pokemon> query = em.createQuery("SELECT p FROM Pokemon p", Pokemon.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Pokemon> listarPokemonPorEntrenador(Integer idEntrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Pokemon> query = em.createQuery(
                    "SELECT p FROM Pokemon p WHERE p.entrenador.idEntrenador = :idEntrenador", Pokemon.class);
            query.setParameter("idEntrenador", idEntrenador);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
