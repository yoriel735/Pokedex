package Entidades;

import java.io.Serializable;
import javax.persistence.*;

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

    @Basic(optional = false)
    @Column(name = "nombreHabilidad", nullable = false, length = 50)
    private String nombreHabilidad;

    @Column(name = "descripcionHabilidad", length = 200)
    private String descripcionHabilidad;

    // Constructores
    public Habilidad() {}

    public Habilidad(String nombreHabilidad, String descripcionHabilidad) {
        this.nombreHabilidad = nombreHabilidad;
        this.descripcionHabilidad = descripcionHabilidad;
    }

    public Habilidad(Integer idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    // Getters y Setters
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

    public String getDescripcionHabilidad() {
        return descripcionHabilidad;
    }

    public void setDescripcionHabilidad(String descripcionHabilidad) {
        this.descripcionHabilidad = descripcionHabilidad;
    }

    // hashCode, equals, toString
    @Override
    public int hashCode() {
        return (idHabilidad != null ? idHabilidad.hashCode() : 0);
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
        return nombreHabilidad;
    }
}
