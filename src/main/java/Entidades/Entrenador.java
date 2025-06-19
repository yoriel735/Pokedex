package Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "entrenador")
@NamedQueries({
    @NamedQuery(name = "Entrenador.findAll", query = "SELECT e FROM Entrenador e"),
    @NamedQuery(name = "Entrenador.findByIdEntrenador", query = "SELECT e FROM Entrenador e WHERE e.idEntrenador = :idEntrenador"),
    @NamedQuery(name = "Entrenador.findByNomEntrenador", query = "SELECT e FROM Entrenador e WHERE e.nomEntrenador = :nomEntrenador")
})
public class Entrenador implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEntrenador")
    private Integer idEntrenador;

    @Column(name = "nomEntrenador", nullable = false, length = 30)
    private String nomEntrenador;

    // Relación con la entidad Pokemon
    @OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Pokemon> listaPokemons;

    public Entrenador() {
        this.listaPokemons = new ArrayList<>();
    }

    public Entrenador(Integer idEntrenador) {
        this.idEntrenador = idEntrenador;
        this.listaPokemons = new ArrayList<>();
    }

    public Entrenador(String nomEntrenador) {
        this.nomEntrenador = nomEntrenador;
        this.listaPokemons = new ArrayList<>();
    }

    // Getters y Setters
    public Integer getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(Integer idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    public String getNomEntrenador() {
        return nomEntrenador;
    }

    public void setNomEntrenador(String nomEntrenador) {
        this.nomEntrenador = nomEntrenador;
    }

    public Collection<Pokemon> getListaPokemons() {
        return listaPokemons;
    }

    public void setListaPokemon(Collection<Pokemon> listaPokemons) {
        this.listaPokemons = listaPokemons;
        for (Pokemon p : listaPokemons) {
            p.setEntrenador(this);
        }
    }

    public void addPokemon(Pokemon p) {
        this.listaPokemons.add(p);
        p.setEntrenador(this);
    }

    public void removePokemon(Pokemon p) {
        this.listaPokemons.remove(p);
        p.setEntrenador(null);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntrenador != null ? idEntrenador.hashCode() : 0);
        return hash;
    }
    
   

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Entrenador)) {
            return false;
        }
        Entrenador other = (Entrenador) obj;
        return (this.idEntrenador != null || other.idEntrenador == null)
                && (this.idEntrenador == null || this.idEntrenador.equals(other.idEntrenador));
    }

    @Override
    public String toString() {
        return nomEntrenador;
    }
}
