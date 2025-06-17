package Controladores;

import Entidades.Ataque;
import Entidades.Habilidad;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import Entidades.TiposPokemon;
import java.util.ArrayList;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

public class PokemonController {

    //misma variable de siempre
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    public void guardarPokemon(Pokemon pokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin(); //iniciamos transaccion
            em.persist(pokemon);//Guardar el nuevo Pokemon en la BD
            em.getTransaction().commit();// Confirmar los cambios
            System.out.println("Pokemon guardado: " + pokemon.getNombrePokemon());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();//Si hay error, deshacemos los cambios para evitar inconsistencias.
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void editarPokemon(Pokemon pokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pokemon); //actualizar los datos del pokemon en cuestion
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

    public boolean eliminarPokemon(Integer id) {
        EntityManager em = emf.createEntityManager();
        boolean eliminado = false;

        try {
            em.getTransaction().begin();
            //Antes de borrar los pokemon se deben borrar sus ataques asignados
            em.createNativeQuery("DELETE FROM pokemon_ataque WHERE id_pokemon = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Ataques eliminados para Pokémon ID: " + id);

            //y ahora eliminamos realmente al pokemon
            em.createNativeQuery("DELETE FROM pokemon WHERE idPokemon = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Pokémon eliminado de la BD.");

            eliminado = true;
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        return eliminado;
    }

    // -----------------------------
    public List<PokemonAtaque> obtenerAtaquesPorPokemon(Integer idPokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            //Consulta que obtiene los ataques de un pokemon específico
            String jpql = "SELECT pa FROM PokemonAtaque pa JOIN FETCH pa.ataque WHERE pa.pokemon.idPokemon = :idPokemon";
            TypedQuery<PokemonAtaque> query = em.createQuery(jpql, PokemonAtaque.class);

            //por lo que filtraremos para obtener el pokemon
            query.setParameter("idPokemon", idPokemon);

            //Se ejecuta la consulta y se devuelve la lista de ataques directamente
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace(); //Manejo de error
            return null;
        } finally {
            em.close(); //Cerrar la conexión con la BD
        }
    }

    public List<Pokemon> obtenerPokemonPorEntrenadorId(Integer idEntrenador) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Ejecutando consulta para ID de entrenador: " + idEntrenador);

            //consulta para obtener todos los pokemon con el id del entrenador en cuestion
            TypedQuery<Pokemon> query = em.createQuery(
                    "SELECT p FROM Pokemon p WHERE p.entrenador.idEntrenador = :id", Pokemon.class);
            query.setParameter("id", idEntrenador);
            //y mostrarlos en una lista
            List<Pokemon> resultado = query.getResultList();
            System.out.println("Pokémon encontrados: " + resultado.size());

            return resultado; //aqui se devuelve la lista
        } finally {
            em.close();
        }
    }


    public List<Habilidad> obtenerTodasHabilidades() {
        EntityManager em = emf.createEntityManager();
        List<Habilidad> habilidades = new ArrayList<>();
        try {
            //de aqui sacamos todas las habilidades para poder elegir una a la hora
            //de añadir un pokemon
            habilidades = em.createQuery("SELECT h FROM Habilidad h", Habilidad.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return habilidades;
    }

    //metodo para actualizar el alias de un pokemon
    public void actualizarAliasPokemon(Integer idPokemon, String nuevoAlias) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            //Buscar el pokemon en la base de datos
            Pokemon pokemon = em.find(Pokemon.class, idPokemon);
            if (pokemon != null) {
                pokemon.setAlias(nuevoAlias);  //Actualizar alias
                em.merge(pokemon);  //Guardar cambios
            } else {
                System.out.println("No se encontro el Pokemon con ID: " + idPokemon);
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

    // -------------------------------------------------------------------------------
   //Este es el metodo que se llama para generar un pokemon aleatorio al pulsar "hierba Alta" 
    public Pokemon generarPokemonAleatorio() {
        EntityManager em = emf.createEntityManager();
        try {
            //Llamamos el metodo de la api para poder obtener un nombre aleatorio
            String nombrePokemonAleatorio = LectorJSONPokeApi.obtenerPokemonAleatorio();

            //Generamos un nivel de pokemon aleatorio, entre 1 y 100
            int nivelAleatorio = new Random().nextInt(100) + 1;

            //Generar un numero de Pokedex aleatorio (pero se debe asegurar que es unico)
            Integer numeroPokedex;
            do {
                numeroPokedex = new Random().nextInt(10000) + 1; //Genera entre 1 y 10000
            } while (existeNumeroPokedex(numeroPokedex, em));//Para evitar que se repitan

            //Asignar una habilidad aleatoria
            Query habilidadQuery = em.createQuery("SELECT h FROM Habilidad h ORDER BY FUNCTION('RAND')");
            habilidadQuery.setMaxResults(1);
            //cogemos una habilidad de la base de datos, solo sacamos un resultado, y lo asignamos
            Habilidad habilidadAleatoria = (Habilidad) habilidadQuery.getSingleResult();

            //Generar tipos aleatorios
            //Transformamos el enum a "numerico" y de ahi sacamos un numero random para identificar el tipo
            TiposPokemon tipoAleatorio = TiposPokemon.values()[new Random().nextInt(TiposPokemon.values().length)];
            TiposPokemon segundoTipoAleatorio = TiposPokemon.values()[new Random().nextInt(TiposPokemon.values().length)];

            //y aqui creamos el objeto `Pokemon` con todo lo que definimos antes
            Pokemon pokemonGenerado = new Pokemon();
            pokemonGenerado.setNombrePokemon(nombrePokemonAleatorio);
            pokemonGenerado.setNumeroPokedex(numeroPokedex); // ✅ Asignar número único de Pokédex
            pokemonGenerado.setNivel(nivelAleatorio);
            pokemonGenerado.setTipoPokemon(tipoAleatorio);
            pokemonGenerado.setSegundoTipo(segundoTipoAleatorio);
            pokemonGenerado.setHabilidad(habilidadAleatoria);
            pokemonGenerado.setAlias("");

            //Asignar ataques aleatorios
            //Mismo de antes, sacamos una lista con todos los ataques registrados en la base de datos
            //y los que existen, llamamos a un metodo que genere algunos de ellos aleatoriamente
            List<PokemonAtaque> pokemonAtaques = new ArrayList<>();
            for (Ataque ataque : obtenerAtaquesAleatorios()) {
                pokemonAtaques.add(new PokemonAtaque(pokemonGenerado, ataque, "Mov. TM"));
            }
            pokemonGenerado.setPokemonAtaques(pokemonAtaques); // y los ataques al pokemon en cuestion.

            return pokemonGenerado; // y ya mostramos el pokemon aleatorio generado

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public void eliminarTablaIncorrecta() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            Query verificarTabla = em.createNativeQuery("SHOW TABLES LIKE 'pokemon_ataques'");
//            List<?> resultado = verificarTabla.getResultList();
//
//            if (!resultado.isEmpty()) {
//                System.out.println("⚠️ Eliminando tabla incorrecta `pokemon_ataques`...");
//                Query dropTable = em.createNativeQuery("DROP TABLE pokemon_ataques");
//                dropTable.executeUpdate();
//                System.out.println("✅ Tabla `pokemon_ataques` eliminada.");
//            } else {
//                System.out.println("🔍 La tabla `pokemon_ataques` no existe, no es necesario eliminarla.");
//            }
//
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            em.close();
//        }
//    }
    public List<Ataque> obtenerAtaquesAleatorios() {
        EntityManager em = emf.createEntityManager();
        //Misma logica que con lo que hicimos con la habilidad, obtenemos toda la lista 
        //y sacamos aleatorios con un rand
        Query ataquesQuery = em.createQuery("SELECT a FROM Ataque a ORDER BY FUNCTION('RAND')");
        ataquesQuery.setMaxResults(4); //limite de ataques que saldran
        List<Ataque> ataques = ataquesQuery.getResultList(); //guardar los ataques en la lista
        em.close();
        return ataques;
    }
//metodo que devuelve un true si el numero de la pokedex ya existe
    private boolean existeNumeroPokedex(Integer numeroPokedex, EntityManager em) {
        Query query = em.createQuery("SELECT COUNT(p) FROM Pokemon p WHERE p.numeroPokedex = :numero");
        query.setParameter("numero", numeroPokedex);
        return ((Long) query.getSingleResult()) > 0;
    }

    //y aqui generamos un numero de pokedex unico
    public Integer generarNumeroPokedexUnico() {
        EntityManager em = emf.createEntityManager();
        Integer numeroPokedex;

        try {
            do {
                numeroPokedex = new Random().nextInt(10000) + 1; //Generamos un número aleatorio
            } while (existeNumeroPokedex(numeroPokedex, em)); //Verificamos que no exista

        } finally {
            em.close();
        }

        return numeroPokedex;

    }
}
