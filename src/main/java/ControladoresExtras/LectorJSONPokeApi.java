/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladoresExtras;

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
    
    //leemos una api para generar el nombre de uno de los pokemon
     private static final String API_URL = "https://pokeapi.co/api/v2/pokemon/";

    public static String obtenerPokemonAleatorio() {
        try {
            Random rand = new Random();//creamos un random y lo asignamos para que 
            //de la api, lea uno de los 1025 pokemon existentes
            int idAleatorio = rand.nextInt(1025) + 1; // ID entre 1 y 1025
            
            URL url = new URL(API_URL + idAleatorio);
            //abrimos una conexion http con el url, y con el get indicamos que 
            //queremos sacar un dato
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());//leemos la respuesta de 
            //la api utilizando un scanner
            String json = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject pokemonData = new JSONObject(json);//y convertimos el resultado
            //de la api en un objeto para poder sacar solo el dato que necesitamos
            return pokemonData.getString("name"); // En este caso solo el nombre
        } catch (IOException e) {
            e.printStackTrace();
            return "Error obteniendo Pokemon";
        }
    }
}
