package Controladores;

import Entidades.Entrenador;
import Entidades.Pokemon;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

public class EntrenadorController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");
    private EntityManager em = emf.createEntityManager();

    public static void crearEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // 🔥 Insertar el entrenador manualmente si tiene un ID específico
            if (entrenador.getIdEntrenador() != null) {
                em.createNativeQuery("INSERT INTO entrenador (idEntrenador, nomEntrenador) VALUES (?, ?)")
                        .setParameter(1, entrenador.getIdEntrenador())
                        .setParameter(2, entrenador.getNomEntrenador())
                        .executeUpdate();
            } else {
                em.persist(entrenador);  // ✅ Si no tiene ID, dejar que la BD asigne uno automáticamente
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
        try {
            em.getTransaction().begin();

            // 🔥 Primero eliminar los ataques asociados a los Pokémon de este entrenador
            em.createNativeQuery("DELETE FROM pokemon_ataque WHERE id_pokemon IN (SELECT idPokemon FROM pokemon WHERE idEntrenador = ?)")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Ataques eliminados para los Pokémon del entrenador ID: " + id);

            // 🔥 Luego eliminamos los Pokémon del entrenador
            em.createNativeQuery("DELETE FROM pokemon WHERE idEntrenador = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Pokémon eliminados para el entrenador ID: " + id);

            // 🔥 Finalmente eliminamos al entrenador
            em.createNativeQuery("DELETE FROM entrenador WHERE idEntrenador = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Entrenador eliminado de la BD.");

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Entrenador buscarEntrenadorPorId(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("❌ Error: ID de entrenador inválido.");
            return null;
        }

        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Entrenador.class, id);
        } finally {
            em.close();
        }
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

    public Entrenador buscarEntrenadorPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Entrenador> resultado = em.createQuery(
                    "SELECT e FROM Entrenador e WHERE e.nomEntrenador = :nombre", Entrenador.class)
                    .setParameter("nombre", nombre)
                    .getResultList();

            return resultado.isEmpty() ? null : resultado.get(0);  // 🔥 Si hay coincidencia, devuelve el entrenador
        } finally {
            em.close();
        }
    }
public void crearEntrenadorConPokemon(Entrenador entrenador, List<Pokemon> listaPokemon) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        // 1️⃣ Persistir el entrenador (si es nuevo)
        if (entrenador.getIdEntrenador() == null) {
            em.persist(entrenador);
            em.flush();
            // Usar refresh para actualizar el objeto con el ID generado
            em.refresh(entrenador);
        } else {
            entrenador = em.merge(entrenador);
        }

        // Comprobamos que el entrenador tiene un ID válido (debería tenerlo tras flush/refresh)
        if (entrenador.getIdEntrenador() == null) {
            throw new IllegalStateException("El entrenador no tiene un ID asignado tras persistirlo.");
        }

        // 2️⃣ Asignar y persistir cada Pokémon nuevo
        for (Pokemon p : listaPokemon) {
            // Asignar el entrenador a cada Pokémon
            p.setEntrenador(entrenador);

            // Dado que estamos leyendo de un CSV nuevo, p.getIdPokemon() es null → persistir directamente
            if (p.getIdPokemon() == null) {
                em.persist(p);
            } else {
                // Si se llegara a actualizar un Pokémon existente:
                em.merge(p);
            }
        }

        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        e.printStackTrace();
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
