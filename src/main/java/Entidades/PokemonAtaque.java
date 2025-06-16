package Entidades;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "`pokemon_ataque`")
@TableGenerator(name = "PokemonAtaqueGen", table = "pokemon_ataque")
public class PokemonAtaque implements Serializable {

    @EmbeddedId
    private PokemonAtaqueId id;

    @ManyToOne(fetch = FetchType.EAGER) // 🔥 Cambiado a EAGER para forzar la carga
    @MapsId("idPokemon")
    @JoinColumn(name = "id_pokemon") // ✅ Cambiado al nombre correcto
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.EAGER) // 🔥 Cambiado a EAGER para forzar la carga
    @MapsId("idAtaque")
    @JoinColumn(name = "id_ataque") // ✅ Cambiado al nombre correcto
    private Ataque ataque;

    @Column(name = "metodoAprendizaje", length = 50)
    private String metodoAprendizaje;

    public PokemonAtaque() {
    }

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
