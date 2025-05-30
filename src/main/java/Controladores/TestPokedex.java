/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author yoriel
 */


public class TestPokedex {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");
        EntityManager em = emf.createEntityManager();

        try {
            int idPokemon = 1;
            System.out.println("Valor de idPokemon recibido: " + idPokemon);

            // 🔥 Depuración: Ejecuta esta consulta primero en tu gestor MySQL para confirmar los datos
            System.out.println("Ejecutando consulta SQL manualmente:");
            System.out.println("SELECT * FROM pokemon_ataque WHERE id_pokemon = " + idPokemon);

            // 🔥 Depuración adicional antes de ejecutar la consulta
            System.out.println("Ejecutando consulta en Java...");

            String sql = "SELECT pa.id_pokemon, pa.id_ataque, a.nombreAtaque, " +
             "a.tipoDeAtaque, a.categoriaAtaque, a.efecto, a.potencia, a.PP " +
             "FROM pokemon_ataque pa " +
             "LEFT JOIN ataques a ON pa.id_ataque = a.idAtaque " +
             "WHERE pa.id_pokemon = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, idPokemon);

            List<Object[]> resultados = query.getResultList();
            System.out.println("Cantidad de ataques encontrados: " + resultados.size());

            if (resultados.isEmpty()) {
                System.out.println("⚠ No se encontraron registros en la tabla `pokemon_ataque` para id_pokemon = " + idPokemon);
                System.out.println("🔍 Confirma en MySQL si realmente hay datos ejecutando:");
                System.out.println("SELECT * FROM pokemon_ataque WHERE id_pokemon = " + idPokemon);
            } else {
                for (Object[] row : resultados) {
                    System.out.println("→ Datos del ataque: " + 
                        " Pokémon ID: " + row[0] +
                        " | Ataque ID: " + row[1] +
                        " | Nombre: " + row[2] +
                        " | Tipo: " + row[3] +
                        " | Categoría: " + row[4] +
                        " | Efecto: " + row[5] +
                        " | Potencia: " + row[6] +
                        " | PP: " + row[7]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}