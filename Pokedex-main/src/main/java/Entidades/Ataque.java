package Entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ataques")
@NamedQueries({
    @NamedQuery(name = "Ataque.findAll", query = "SELECT a FROM Ataque a"),
    @NamedQuery(name = "Ataque.findByIdAtaque", query = "SELECT a FROM Ataque a WHERE a.idAtaque = :idAtaque"),
    @NamedQuery(name = "Ataque.findByNombreAtaque", query = "SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombreAtaque")
})
public class Ataque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAtaque")
    private Integer idAtaque;

    @Basic(optional = false)
    @Column(name = "nombreAtaque", length = 50)
    private String nombreAtaque;

    @Column(name = "tipoDeAtaque", length = 20)
    private String tipo;

    @Column(name = "categoriaAtaque", length = 20)
    private String categoria;

    @Column(name = "efecto", length = 255)
    private String efecto;

    @Column(name = "potencia")
    private Integer potencia;

    @Column(name = "PP")
    private Integer pp;

@OneToMany(mappedBy = "ataque", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private Set<PokemonAtaque> pokemonAtaques = new HashSet<>();

    public Ataque() {
    }

    public Ataque(Integer idAtaque) {
        this.idAtaque = idAtaque;
    }

    public Ataque(Integer idAtaque, String nombreAtaque) {
        this.idAtaque = idAtaque;
        this.nombreAtaque = nombreAtaque;
    }

    public Integer getIdAtaque() {
        return idAtaque;
    }

    public void setIdAtaque(Integer idAtaque) {
        this.idAtaque = idAtaque;
    }

    public String getNombreAtaque() {
        return nombreAtaque;
    }

    public void setNombreAtaque(String nombreAtaque) {
        this.nombreAtaque = nombreAtaque;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAtaque != null ? idAtaque.hashCode() : 0);
        return hash;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    public Integer getPotencia() {
        return potencia;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }


    public Set<PokemonAtaque> getPokemonAtaques() {
        return pokemonAtaques;
    }

    public void setPokemonAtaques(Set<PokemonAtaque> pokemonAtaques) {
        this.pokemonAtaques = pokemonAtaques;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ataque)) {
            return false;
        }
        Ataque other = (Ataque) object;
        if ((this.idAtaque == null && other.idAtaque != null) || (this.idAtaque != null && !this.idAtaque.equals(other.idAtaque))) {
            return false;
        }
        return true;
    }
}
