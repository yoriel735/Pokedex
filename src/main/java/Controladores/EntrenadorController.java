package Controladores;

import Entidades.Entrenador;
import Entidades.Pokemon;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.Query;
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

    // ----------------------------------------------------------------------------
    /*
    Busca al entrenador en la base de datos por su ID. 🔥
    Asigna el Pokémon capturado al entrenador y lo guarda en la base de datos. 
     Añade el Pokémon a la colección del entrenador para que se refleje en la Pokédex. 
    🔥 Confirma la transacción para que los cambios se guarden correctamente.
     */
    public void agregarPokemonACaptura(Integer idEntrenador, Pokemon pokemonCapturado) {
    EntityManager em = emf.createEntityManager();

    try {
        em.getTransaction().begin();

        // 🔥 Verificar si ya existe un Pokémon con el mismo número de Pokédex
        Query query = em.createQuery("SELECT COUNT(p) FROM Pokemon p WHERE p.numeroPokedex = :numero");
        query.setParameter("numero", pokemonCapturado.getNumeroPokedex());
        Long count = (Long) query.getSingleResult();

        if (count > 0) {
            System.out.println("⚠️ Número de Pokédex duplicado, generando uno nuevo...");
            pokemonCapturado.setNumeroPokedex(new PokemonController().generarNumeroPokedexUnico()); // ✅ Generar uno nuevo
        }

        // 🔥 Buscar al entrenador en la base de datos
        Entrenador entrenador = em.find(Entrenador.class, idEntrenador);
        if (entrenador == null) {
            System.out.println("❌ Entrenador no encontrado en la BD.");
            em.getTransaction().rollback();
            return;
        }

        // 🔥 Asignar el Pokémon al entrenador antes de persistirlo
        pokemonCapturado.setEntrenador(entrenador);
        em.persist(pokemonCapturado);
        em.merge(entrenador);

        em.getTransaction().commit();

        System.out.println("✅ Pokémon " + pokemonCapturado.getNombrePokemon() + " asignado correctamente.");

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        e.printStackTrace();
    } finally {
        em.close();
    }
}
    public void cerrar() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
