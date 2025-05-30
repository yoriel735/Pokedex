package Controladores;

import Entidades.Ataque;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import java.util.ArrayList;
import java.util.Arrays;

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

    public List<PokemonAtaque> obtenerAtaquesPorPokemon(Integer idPokemon) {
      EntityManager em = emf.createEntityManager();
    try {
            // Usamos JPQL con fetch para evitar lazy loading
            String jpql = "SELECT pa FROM PokemonAtaque pa JOIN FETCH pa.ataque WHERE pa.pokemon.idPokemon = :idPokemon";
            TypedQuery<PokemonAtaque> query = em.createQuery(jpql, PokemonAtaque.class);
            query.setParameter("idPokemon", idPokemon);

            List<PokemonAtaque> lista = query.getResultList();

            // Opción: Forzar acceso a los atributos para inicializar proxies
            for (PokemonAtaque pa : lista) {
                Ataque atk = pa.getAtaque();
                if (atk != null) {
                    atk.getNombreAtaque(); // acceso para inicializar
                }
            }

            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public Pokemon buscarPokemonPorIdConHabilidad(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Pokemon> query = em.createQuery(
                    "SELECT p FROM Pokemon p LEFT JOIN FETCH p.habilidad WHERE p.idPokemon = :id", Pokemon.class);
            query.setParameter("id", id);
            return query.getSingleResult();
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
