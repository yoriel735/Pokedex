package Controladores;

import Entidades.Ataque;
import Entidades.Habilidad;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import Entidades.TiposPokemon;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

public class PokemonController {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    public void guardarPokemon(Pokemon pokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pokemon);
            em.getTransaction().commit();
            System.out.println("✅ Pokémon guardado: " + pokemon.getNombrePokemon());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
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

    public boolean eliminarPokemon(Integer id) {
        EntityManager em = emf.createEntityManager();
        boolean eliminado = false;

        try {
            em.getTransaction().begin();

            // 🔥 Eliminar ataques directamente con SQL nativo
            em.createNativeQuery("DELETE FROM pokemon_ataque WHERE id_pokemon = ?")
                    .setParameter(1, id)
                    .executeUpdate();
            System.out.println("✅ Ataques eliminados para Pokémon ID: " + id);

            // 🔥 Ahora eliminamos el Pokémon
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

    public Integer buscarIdPokemonPorNombre(String nombrePokemon) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery("SELECT p.idPokemon FROM Pokemon p WHERE p.nombrePokemon = :nombre", Integer.class);
            query.setParameter("nombre", nombrePokemon);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return -1; // Si no encuentra el Pokémon, retorna -1
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
            System.out.println("🔥 Ejecutando consulta para ID de entrenador: " + idEntrenador);

            TypedQuery<Pokemon> query = em.createQuery(
                    "SELECT p FROM Pokemon p WHERE p.entrenador.idEntrenador = :id", Pokemon.class);
            query.setParameter("id", idEntrenador);

            List<Pokemon> resultado = query.getResultList();
            System.out.println("🔥 Pokémon encontrados: " + resultado.size());

            return resultado;
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

    public List<Habilidad> obtenerTodasHabilidades() {
        EntityManager em = emf.createEntityManager();
        List<Habilidad> habilidades = new ArrayList<>();
        try {
            habilidades = em.createQuery("SELECT h FROM Habilidad h", Habilidad.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return habilidades;
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

    public void actualizarAliasPokemon(Integer idPokemon, String nuevoAlias) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Buscar el Pokémon en la base de datos
            Pokemon pokemon = em.find(Pokemon.class, idPokemon);
            if (pokemon != null) {
                pokemon.setAlias(nuevoAlias);  // Actualizar alias
                em.merge(pokemon);  // Guardar cambios
            } else {
                System.out.println("No se encontró el Pokémon con ID: " + idPokemon);
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
    public Pokemon generarPokemonAleatorio() {
        EntityManager em = emf.createEntityManager();

        try {
            // 🔹 Obtener nombre aleatorio desde la API
            String nombrePokemonAleatorio = LectorJSONPokeApi.obtenerPokemonAleatorio();

            // 🔹 Generar nivel aleatorio (1-100)
            int nivelAleatorio = new Random().nextInt(100) + 1;

            // 🔹 Generar un número de Pokédex aleatorio (pero asegurando que sea único en la BD)
            Integer numeroPokedex;
            do {
                numeroPokedex = new Random().nextInt(10000) + 1; // 🔥 Genera entre 1 y 10000
            } while (existeNumeroPokedex(numeroPokedex, em)); // ❌ Evitar números repetidos

            // 🔹 Asignar una habilidad aleatoria
            Query habilidadQuery = em.createQuery("SELECT h FROM Habilidad h ORDER BY FUNCTION('RAND')");
            habilidadQuery.setMaxResults(1);
            Habilidad habilidadAleatoria = (Habilidad) habilidadQuery.getSingleResult();

            // 🔹 Generar tipos aleatorios
            TiposPokemon tipoAleatorio = TiposPokemon.values()[new Random().nextInt(TiposPokemon.values().length)];
            TiposPokemon segundoTipoAleatorio = TiposPokemon.values()[new Random().nextInt(TiposPokemon.values().length)];

            // 🔹 Crear el objeto `Pokemon`
            Pokemon pokemonGenerado = new Pokemon();
            pokemonGenerado.setNombrePokemon(nombrePokemonAleatorio);
            pokemonGenerado.setNumeroPokedex(numeroPokedex); // ✅ Asignar número único de Pokédex
            pokemonGenerado.setNivel(nivelAleatorio);
            pokemonGenerado.setTipoPokemon(tipoAleatorio);
            pokemonGenerado.setSegundoTipo(segundoTipoAleatorio);
            pokemonGenerado.setHabilidad(habilidadAleatoria);
            pokemonGenerado.setAlias("");

            // 🔹 Asignar ataques aleatorios
            List<PokemonAtaque> pokemonAtaques = new ArrayList<>();
            for (Ataque ataque : obtenerAtaquesAleatorios()) {
                pokemonAtaques.add(new PokemonAtaque(pokemonGenerado, ataque, "Mov. TM"));
            }
            pokemonGenerado.setPokemonAtaques(pokemonAtaques); // ✅ Asignar los ataques en memoria.

            return pokemonGenerado; // ✅ Retornar Pokémon aleatorio generado

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
        Query ataquesQuery = em.createQuery("SELECT a FROM Ataque a ORDER BY FUNCTION('RAND')");
        ataquesQuery.setMaxResults(4);
        List<Ataque> ataques = ataquesQuery.getResultList();
        em.close();
        return ataques;
    }

    private boolean existeNumeroPokedex(Integer numeroPokedex, EntityManager em) {
        Query query = em.createQuery("SELECT COUNT(p) FROM Pokemon p WHERE p.numeroPokedex = :numero");
        query.setParameter("numero", numeroPokedex);
        return ((Long) query.getSingleResult()) > 0;
    }

    public Integer generarNumeroPokedexUnico() {
        EntityManager em = emf.createEntityManager();
        Integer numeroPokedex;

        try {
            do {
                numeroPokedex = new Random().nextInt(10000) + 1; // 🔥 Generamos un número aleatorio
            } while (existeNumeroPokedex(numeroPokedex, em)); // ✅ Verificamos que no exista

        } finally {
            em.close();
        }

        return numeroPokedex;

    }
}
