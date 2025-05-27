package Entidades;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "ataque")
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
    
    @OneToMany(mappedBy = "ataque", cascade = CascadeType.ALL)
    private Set<PokemonAtaque> pokemonAtaques = new HashSet<>();

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

    @Override
    public String toString() {
        return "entidades.Ataque[ idAtaque=" + idAtaque + ", nombreAtaque=" + nombreAtaque + " ]";
    }
}
