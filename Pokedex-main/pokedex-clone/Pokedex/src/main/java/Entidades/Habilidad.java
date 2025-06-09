package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "habilidad")
@NamedQueries({
    @NamedQuery(name = "Habilidad.findAll", query = "SELECT h FROM Habilidad h"),
    @NamedQuery(name = "Habilidad.findByIdHabilidad", query = "SELECT h FROM Habilidad h WHERE h.idHabilidad = :idHabilidad"),
    @NamedQuery(name = "Habilidad.findByNombreHabilidad", query = "SELECT h FROM Habilidad h WHERE h.nombreHabilidad = :nombreHabilidad")
})
public class Habilidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idHabilidad")
    private Integer idHabilidad;

    @Column(name = "nombreHabilidad", nullable = false, length = 50)
    private String nombreHabilidad;

    public Habilidad() {}

    public Habilidad(Integer idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    public Habilidad(String nombreHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
    }

    // getters y setters

    public Integer getIdHabilidad() {
        return idHabilidad;
    }

    public void setIdHabilidad(Integer idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    public String getNombreHabilidad() {
        return nombreHabilidad;
    }

    public void setNombreHabilidad(String nombreHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
    }

    // hashCode, equals, toString

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHabilidad != null ? idHabilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Habilidad)) {
            return false;
        }
        Habilidad other = (Habilidad) obj;
        return (this.idHabilidad != null || other.idHabilidad == null)
            && (this.idHabilidad == null || this.idHabilidad.equals(other.idHabilidad));
    }

    @Override
    public String toString() {
        return "Habilidad{" + "idHabilidad=" + idHabilidad + ", nombreHabilidad=" + nombreHabilidad + '}';
    }
}
