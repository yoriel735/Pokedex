/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONObject;


/**
 *
 * @author yoriel
 */
public class LectorJSONPokeApi {
    
     private static final String API_URL = "https://pokeapi.co/api/v2/pokemon/";

    public static String obtenerPokemonAleatorio() {
        try {
            Random rand = new Random();
            int idAleatorio = rand.nextInt(1025) + 1; // ID entre 1 y 1025

            URL url = new URL(API_URL + idAleatorio);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            String json = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject pokemonData = new JSONObject(json);
            return pokemonData.getString("name"); // Extrae el nombre del Pokémon
        } catch (IOException e) {
            e.printStackTrace();
            return "Error obteniendo Pokémon";
        }
    }
}
