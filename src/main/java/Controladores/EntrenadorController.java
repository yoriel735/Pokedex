package Controladores;

import Entidades.Entrenador;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EntrenadorController {

    //Estas 2 lineas es como definimos antes, para crear una variable que sea "la que esta conectada" 
    //con la base de datos
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");
    private EntityManager em = emf.createEntityManager(); //esto esta mas hecho para manejar operaciones puntuales y cerrarse

    //creamos un entrenador, basicamente un insert a la tabla de entrenador
    public static void crearEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            //Insertar el entrenador manualmente si tiene un ID específico
            if (entrenador.getIdEntrenador() != null) {
                em.createNativeQuery("INSERT INTO entrenador (idEntrenador, nomEntrenador) VALUES (?, ?)")
                        .setParameter(1, entrenador.getIdEntrenador())
                        .setParameter(2, entrenador.getNomEntrenador())
                        .executeUpdate(); //esto es realmente para identificar los ficheros, ya qeu nunca añado directamente el 
                //id del entrenador a menos que sea por estos
            } else {
                em.persist(entrenador);  //Si no tiene ID, dejar que la BD asigne uno automáticamente
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {//estas 2 operaciones son por seguridad, comprueba si la transaccion sigue en pie, 
                //y si sigue y ha habido un error, vuelve al estado inicial, no "guarde un pikachu partido a la mitad"
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    //editar el nombre de un entrenador
    public void editarNombreEntrenador(Entrenador entrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entrenador);//actualiza el objeto en la BD.
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

    //Metodo para eliminar un entrenador junto a todos sus pokemon relacionados
    public void eliminarEntrenador(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            //La idea poder borrarlo todo, y los pokemon tienen ataques que dependen de estos
            //Lo primero que haremos es eliminar los ataques asociados al pokemon en cuestion
            em.createNativeQuery("DELETE FROM pokemon_ataque WHERE id_pokemon IN (SELECT idPokemon FROM pokemon WHERE idEntrenador = ?)")
                    .setParameter(1, id) //identifica el id del entrenador
                    .executeUpdate(); //Ejecuta la consulta y realiza los cambios
            System.out.println("Ataques eliminados para los Pokemon del entrenador ID: " + id);

            //Ahora si eliminamos los Pokemon del entrenador
            em.createNativeQuery("DELETE FROM pokemon WHERE idEntrenador = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("Pokemon eliminados para el entrenador ID: " + id);

            //y ya por ultimo, eliminamos al entrenador
            em.createNativeQuery("DELETE FROM entrenador WHERE idEntrenador = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("Entrenador eliminado");

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

    //agregamos un metodo de busqueda por id de entrenador
    public Entrenador buscarEntrenadorPorId(Integer id) {
        if (id == null || id <= 0) {
            System.err.println("Error: ID de entrenador inválido.");
            return null;
        }

        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Entrenador.class, id); //comprueba si existe tal id
        } finally {
            em.close();
        }
    }

    //Una lista que muestra todos los entrenadores existentens basicamente
    //para el jList
    public List<Entrenador> obtenerTodosLosEntrenadores() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Entrenador> query = em.createQuery("SELECT e FROM Entrenador e", Entrenador.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Integer obtenerIdPorNombre(String nombre) {
        try {
            //Buscamos el entrenador por su nombre
            TypedQuery<Entrenador> query = em.createNamedQuery("Entrenador.findByNomEntrenador", Entrenador.class);
            query.setParameter("nomEntrenador", nombre);
            Entrenador entrenador = query.getSingleResult(); //y se obtiene su resultado
            if (entrenador != null) {
                return entrenador.getIdEntrenador();
            } else {
                return null;//si no se encuentra por alguna razon, mostramos el siguiente mensaje
            }
        } catch (Exception e) {
            System.err.println("No se encontró entrenador con nombre: " + nombre);
            return null;
        }
    }

    public List<Pokemon> obtenerPokemonPorEntrenadorId(Integer idEntrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            //Similar a antes, pero ahora sacamos el pokemon en base a su id Entrenador
            //y como cada pokemon esta asignado a un entrenador, no hay problemas
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
            TypedQuery<Entrenador> query = em.createQuery(
                "SELECT e FROM Entrenador e WHERE e.nomEntrenador = :nombre", Entrenador.class);
            query.setParameter("nombre", nombre);
            List<Entrenador> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

//este es el metodo para poder crear un entrenador con sus pokemon y que posteriormente se pueda guardar en fichero   
//Si el entrenador es nuevo, se guarda en la base de datos y la BD asigna un ID.
//Si el entrenador ya existe, se actualiza.
//Luego, cada pokemon de la lista se vincula con el entrenador y se guarda en la BD.
    public void crearEntrenadorConPokemon(Entrenador entrenador, List<Pokemon> listaPokemon) {
    EntityManager em = emf.createEntityManager();
    
    try {
        em.getTransaction().begin();

        //añadir/actualizar el entrenador asegurando que tiene ID
        if (entrenador.getIdEntrenador() == null) {
            em.persist(entrenador);
            em.flush();
            em.refresh(entrenador);
        } else {
            entrenador = em.merge(entrenador);
        }

        for (Pokemon p : listaPokemon) {
            p.setEntrenador(entrenador);

            //Verificar si el pokemon ya existe en la BD
            TypedQuery<Pokemon> query = em.createQuery("SELECT p FROM Pokemon p WHERE p.numeroPokedex = :numeroPokedex", Pokemon.class);
            query.setParameter("numeroPokedex", p.getNumeroPokedex());
            List<Pokemon> resultados = query.getResultList();

            if (!resultados.isEmpty()) {
                //Si ya existe, actualizarlo
                Pokemon pokemonExistente = resultados.get(0);
                pokemonExistente.setAlias(p.getAlias());
                pokemonExistente.setNivel(p.getNivel());
                pokemonExistente.setNombrePokemon(p.getNombrePokemon());
                pokemonExistente.setSegundoTipo(p.getSegundoTipo());
                pokemonExistente.setTipoPokemon(p.getTipoPokemon());
                pokemonExistente.setHabilidad(p.getHabilidad());
                em.merge(pokemonExistente);
            } else {
                //Si no existe, insertarlo
                em.persist(p);
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
    Busca al entrenador en la base de datos por su ID.
    Asigna el Pokemon capturado al entrenador y lo guarda en la base de datos. 
     Añade el Pokémon a la coleccion del entrenador para que se refleje en la Pokedex. 
    Confirma la transaccion para que los cambios se guarden correctamente.
     */
    public void agregarPokemonACaptura(Integer idEntrenador, Pokemon pokemonCapturado) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            //Verificar si ya existe un Pokemon con el mismo numero de Pokedex
            Query query = em.createQuery("SELECT COUNT(p) FROM Pokemon p WHERE p.numeroPokedex = :numero");
            query.setParameter("numero", pokemonCapturado.getNumeroPokedex());
            Long count = (Long) query.getSingleResult();

            if (count > 0) {
                pokemonCapturado.setNumeroPokedex(new PokemonController().generarNumeroPokedexUnico()); // ✅ Generar uno nuevo
            }

            //Buscar al entrenador en la base de datos
            Entrenador entrenador = em.find(Entrenador.class, idEntrenador);
            if (entrenador == null) {
                em.getTransaction().rollback();
                return;
            }

            //Asignar el Pokemon al entrenador antes de persistirlo
            pokemonCapturado.setEntrenador(entrenador);
            em.persist(pokemonCapturado);
            em.merge(entrenador);

            em.getTransaction().commit();

            System.out.println("Pokemon " + pokemonCapturado.getNombrePokemon() + " asignado correctamente.");

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
