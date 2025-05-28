package Entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pokemon_ataque")
public class PokemonAtaque implements Serializable {

    @EmbeddedId
    private PokemonAtaqueId id;

    @ManyToOne
    @MapsId("idPokemon")
    @JoinColumn(name = "id_pokemon")
    private Pokemon pokemon;

    @ManyToOne
    @MapsId("idAtaque")
    @JoinColumn(name = "id_ataque")
    private Ataque ataque;

    @Column(name = "metodoAprendizaje", length = 50)
    private String metodoAprendizaje;

    public PokemonAtaque() {}

    public PokemonAtaque(Pokemon pokemon, Ataque ataque, String metodoAprendizaje) {
        this.pokemon = pokemon;
        this.ataque = ataque;
        this.metodoAprendizaje = metodoAprendizaje;
        this.id = new PokemonAtaqueId(pokemon.getIdPokemon(), ataque.getIdAtaque());
    }

    public PokemonAtaqueId getId() {
        return id;
    }

    public void setId(PokemonAtaqueId id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Ataque getAtaque() {
        return ataque;
    }

    public void setAtaque(Ataque ataque) {
        this.ataque = ataque;
    }

    public String getMetodoAprendizaje() {
        return metodoAprendizaje;
    }

    public void setMetodoAprendizaje(String metodoAprendizaje) {
        this.metodoAprendizaje = metodoAprendizaje;
    }
}
