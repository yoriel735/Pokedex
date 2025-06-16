package Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

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

    @Basic(optional = false)
    @Column(name = "numero_pokedex", nullable = false)
    private int numeroPokedex;

    @Basic(optional = false)
    @Column(name = "nombre_pokemon", nullable = false, length = 30)
    private String nombrePokemon;

    @Column(name = "alias", length = 30)
    private String alias = "Sin alias";

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPokemon")
    private TiposPokemon tipoPokemon;

    @Enumerated(EnumType.STRING)
    @Column(name = "segundo_tipo")
    private TiposPokemon segundoTipo;

    @Basic(optional = false)
    @Column(name = "nivel", nullable = false)
    private int nivel;

    @ManyToOne
    @JoinColumn(name = "idEntrenador", referencedColumnName = "idEntrenador")
    private Entrenador entrenador;

    // Asumiendo que la relación ManyToMany con Habilidad es correcta
    @OneToOne
    @JoinColumn(name = "idHabilidad") // columna en la tabla pokemon que referencia a habilidad
    private Habilidad habilidad;
    
@OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JoinTable(name = "pokemon_ataque") // 🔥 Asegura que use el nombre correcto
private List<PokemonAtaque> pokemonAtaques = new ArrayList<>();


    public List<PokemonAtaque> getPokemonAtaques() {
        return pokemonAtaques;
    }

    public void setPokemonAtaques(List<PokemonAtaque> pokemonAtaques) {
        this.pokemonAtaques = pokemonAtaques;
    }

    private Collection<Ataque> listaAtaques = new ArrayList<>();

    // Constructores
    public Pokemon() {
    }

    public Pokemon(int numeroPokedex, String nombrePokemon, TiposPokemon tipoPokemon, TiposPokemon segundoTipo, int nivel, Entrenador entrenador) {
        this.numeroPokedex = numeroPokedex;
        this.nombrePokemon = nombrePokemon;
        this.alias = alias != null ? alias : "Sin alias";
        this.tipoPokemon = tipoPokemon;
        this.segundoTipo = segundoTipo;
        this.nivel = nivel;
        this.entrenador = entrenador;
    }

    // Getters y Setters
    public Integer getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(Integer idPokemon) {
        this.idPokemon = idPokemon;
    }

    public int getNumeroPokedex() {
        return numeroPokedex;
    }

    public void setNumeroPokedex(int numeroPokedex) {
        this.numeroPokedex = numeroPokedex;
    }

    public String getNombrePokemon() {
        return nombrePokemon;
    }

    public void setNombrePokemon(String nombrePokemon) {
        this.nombrePokemon = nombrePokemon;
    }

    public String getAlias() {
        return (alias != null && !alias.trim().isEmpty()) ? alias : "Sin alias";
    }

    public void setAlias(String alias) {
        this.alias = alias != null ? alias : "Sin alias";
    }

    public TiposPokemon getTipoPokemon() {
        return tipoPokemon;
    }

    public void setTipoPokemon(TiposPokemon tipoPokemon) {
        this.tipoPokemon = tipoPokemon;
    }

    public TiposPokemon getSegundoTipo() {
        return segundoTipo;
    }

    public void setSegundoTipo(TiposPokemon segundoTipo) {
        this.segundoTipo = segundoTipo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
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

    // Métodos para añadir/quitar Ataques y Habilidades
    public void addAtaque(Ataque ataque) {
        this.listaAtaques.add(ataque);
    }

    public void removeAtaque(Ataque ataque) {
        this.listaAtaques.remove(ataque);
    }

    public Habilidad getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    public void asignarAEntrenador(Entrenador entrenador) {
        if (entrenador != null) {
            this.entrenador = entrenador; // ✅ Asignamos el entrenador al Pokémon
            entrenador.getListaPokemons().add(this); // ✅ Añadimos el Pokémon a la colección del entrenador
        } else {
            System.out.println(" Error: Intentando asignar un Pokémon a un entrenador nulo.");
        }
    }

    // hashCode, equals y toString
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPokemon != null ? idPokemon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pokemon)) {
            return false;
        }
        Pokemon other = (Pokemon) object;
        return (this.idPokemon != null || other.idPokemon == null) && (this.idPokemon == null || this.idPokemon.equals(other.idPokemon));
    }

}
