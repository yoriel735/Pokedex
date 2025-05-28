package Controladores;

import Entidades.Entrenador;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.TypedQuery;

public class EntrenadorController {

    

   private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    public static void crearEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entrenador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    public void editarEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(entrenador);
        em.getTransaction().commit();
        em.close();
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
}