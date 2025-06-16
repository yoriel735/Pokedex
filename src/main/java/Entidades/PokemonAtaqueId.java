package Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PokemonAtaqueId implements Serializable {

    @Column(name = "id_pokemon", nullable = false) // 🔥 Asegura que EclipseLink no haga conversiones
    private Integer idPokemon;

  @Column(name = "id_ataque", nullable = false) // 🔥 Asegura que EclipseLink no haga conversiones
    private Integer idAtaque;
  
    public PokemonAtaqueId() {}

    public PokemonAtaqueId(Integer idPokemon, Integer idAtaque) {
        this.idPokemon = idPokemon;
        this.idAtaque = idAtaque;
    }

    public Integer getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(Integer idPokemon) {
        this.idPokemon = idPokemon;
    }

    public Integer getIdAtaque() {
        return idAtaque;
    }

    public void setIdAtaque(Integer idAtaque) {
        this.idAtaque = idAtaque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokemonAtaqueId)) return false;
        PokemonAtaqueId that = (PokemonAtaqueId) o;
        return Objects.equals(idPokemon, that.idPokemon) &&
               Objects.equals(idAtaque, that.idAtaque);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPokemon, idAtaque);
    }
}
