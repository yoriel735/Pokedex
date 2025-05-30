/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import java.util.List;

/**
 *
 * @author yoriel
 */
public class TestConsulta {
    public static void main(String[] args) {
        PokemonController pc = new PokemonController();
        List<Pokemon> pokemons = pc.obtenerTodosLosPokemon();
        for (Pokemon p : pokemons) {
            System.out.println("Pokémon: " + p.getNombrePokemon());
            List<PokemonAtaque> ataques = pc.obtenerAtaquesPorPokemon(p.getIdPokemon());
            for (PokemonAtaque pa : ataques) {
                System.out.println("  Ataque: " + pa.getAtaque().getNombreAtaque());
            }
        }
    }
}
