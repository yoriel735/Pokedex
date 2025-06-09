package Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pokemon")
@NamedQueries({
    @NamedQuery(name = "Pokemon.findAll", query = "SELECT p FROM Pokemon p"),
    @NamedQuery(name = "Pokemon.findByIdPokemon", query = "SELECT p FROM Pokemon p WHERE p.idPokemon = :idPokemon"),
    @NamedQuery(name = "Pokemon.findByNombrePokemon", query = "SELECT p FROM Pokemon p WHERE p.nombrePokemon = :nombrePokemon")
})
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPokemon")
    private Integer idPokemon;

    @Column(name = "nombrePokemon", nullable = false, length = 50)
    private String nombrePokemon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idEntrenador", referencedColumnName = "idEntrenador")
    private Entrenador entrenador;

    // Relación ManyToMany con Ataque a través de tabla intermedia PokemonAtaque
    @ManyToMany
    @JoinTable(
        name = "pokemonataque",
        joinColumns = @JoinColumn(name = "idPokemon"),
        inverseJoinColumns = @JoinColumn(name = "idAtaque")
    )
    private Collection<Ataque> listaAtaques;

    // Relación ManyToMany con Habilidad
    @ManyToMany
    @JoinTable(
        name = "pokemonhabilidad",
        joinColumns = @JoinColumn(name = "idPokemon"),
        inverseJoinColumns = @JoinColumn(name = "idHabilidad")
    )
    private Collection<Habilidad> listaHabilidades;

    public Pokemon() {
        this.listaAtaques = new ArrayList<>();
        this.listaHabilidades = new ArrayList<>();
    }

    public Pokemon(Integer idPokemon) {
        this.idPokemon = idPokemon;
        this.listaAtaques = new ArrayList<>();
        this.listaHabilidades = new ArrayList<>();
    }

    public Pokemon(String nombrePokemon, Entrenador entrenador) {
        this.nombrePokemon = nombrePokemon;
        this.entrenador = entrenador;
        this.listaAtaques = new ArrayList<>();
        this.listaHabilidades = new ArrayList<>();
    }

    // getters y setters

    public Integer getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(Integer idPokemon) {
        this.idPokemon = idPokemon;
    }

    public String getNombrePokemon() {
        return nombrePokemon;
    }

    public void setNombrePokemon(String nombrePokemon) {
        this.nombrePokemon = nombrePokemon;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Collection<Ataque> getListaAtaques() {
        return listaAtaques;
    }

    public void setListaAtaques(Collection<Ataque> listaAtaques) {
        this.listaAtaques = listaAtaques;
    }

    public Collection<Habilidad> getListaHabilidades() {
        return listaHabilidades;
    }

    public void setListaHabilidades(Collection<Habilidad> listaHabilidades) {
        this.listaHabilidades = listaHabilidades;
    }

    // Métodos para añadir/quitar Ataques y Habilidades

    public void addAtaque(Ataque ataque) {
        this.listaAtaques.add(ataque);
    }

    public void removeAtaque(Ataque ataque) {
        this.listaAtaques.remove(ataque);
    }

    public void addHabilidad(Habilidad habilidad) {
        this.listaHabilidades.add(habilidad);
    }

    public void removeHabilidad(Habilidad habilidad) {
        this.listaHabilidades.remove(habilidad);
    }
    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL)
private Set<PokemonAtaque> pokemonAtaques = new HashSet<>();

    // hashCode, equals, toString

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPokemon != null ? idPokemon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pokemon)) {
            return false;
        }
        Pokemon other = (Pokemon) obj;
        return (this.idPokemon != null || other.idPokemon == null)
                && (this.idPokemon == null || this.idPokemon.equals(other.idPokemon));
    }

    @Override
    public String toString() {
        return "Pokemon{" + "idPokemon=" + idPokemon + ", nombrePokemon=" + nombrePokemon + '}';
    }
}
