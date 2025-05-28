package Controladores;

import Entidades.Entrenador;
import Entidades.Pokemon;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.TypedQuery;

public class EntrenadorController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");
    private EntityManager em = emf.createEntityManager();

    public static void crearEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entrenador);
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

    public void editarEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entrenador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;  // O puedes manejar el error como quieras
        } finally {
            em.close();
        }
    }

    public void eliminarEntrenador(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Entrenador entrenador = em.find(Entrenador.class, id);
        if (entrenador != null) {
            em.remove(entrenador);
        }
        em.getTransaction().commit();
        em.close();
    }

    public Entrenador buscarEntrenadorPorId(Integer id) {
        EntityManager em = emf.createEntityManager();
        Entrenador entrenador = em.find(Entrenador.class, id);
        em.close();
        return entrenador;
    }

    public List<Entrenador> obtenerTodosLosEntrenadores() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Entrenador> query = em.createQuery("SELECT e FROM Entrenador e", Entrenador.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Entrenador> listarEntrenadores() {
        EntityManager em = emf.createEntityManager();
        List<Entrenador> lista = em.createQuery("SELECT e FROM Entrenador e", Entrenador.class).getResultList();
        em.close();
        return lista;
    }

    public Integer obtenerIdPorNombre(String nombre) {
        try {
            TypedQuery<Entrenador> query = em.createNamedQuery("Entrenador.findByNomEntrenador", Entrenador.class);
            query.setParameter("nomEntrenador", nombre);
            Entrenador entrenador = query.getSingleResult();
            if (entrenador != null) {
                return entrenador.getIdEntrenador();
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("No se encontró entrenador con nombre: " + nombre);
            return null;
        }
    }

    public List<Pokemon> obtenerPokemonPorEntrenadorId(Integer idEntrenador) {
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

    public void cerrar() {
        if (em.isOpen()) {
            em.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }

}
