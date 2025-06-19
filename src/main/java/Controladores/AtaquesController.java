
package Controladores;

import Entidades.Ataque;
import Entidades.Pokemon;
import Entidades.PokemonAtaque;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;

/**
 *
 * @author yoriel
 */
public class AtaquesController {
    //Definimos una variable para trabajar con la base de datos, cada operacion con la base de datos
    //partira de esta linea de codigo
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pokemondb");

    //Hacemos un select de los ataques del pokemon, en base a su id
    public List<PokemonAtaque> obtenerAtaquesPorPokemon(int idPokemon) {
        EntityManager em = emf.createEntityManager(); //con esta parte es donde conectamos con la base de datos
        try {
            Query query = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon");
            query.setParameter("idPokemon", idPokemon);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //Este metodo es para guardar un ataque en un pokemon
   //la idea es comprobar si un ataque ya existe, y si este ya existia, lo reutiliza
    //en lugar de crear un duplicado
    public void guardarAtaqueYPokemon(Pokemon pokemon, Ataque nuevoAtaque) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin(); //iniciamos transaccion para evitar
            //pposibles errores

            //verificamos si el ataque ya existe
            Query query = em.createQuery("SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre AND a.tipo = :tipo AND a.categoria = :categoria");
            query.setParameter("nombre", nuevoAtaque.getNombreAtaque());
            query.setParameter("tipo", nuevoAtaque.getTipo());
            query.setParameter("categoria", nuevoAtaque.getCategoria());
            List<Ataque> ataquesExistentes = query.getResultList();

            if (!ataquesExistentes.isEmpty()) {
                nuevoAtaque = ataquesExistentes.get(0); //y si si existe, usamos 
                //el ya existente
            } else {
                em.persist(nuevoAtaque); //con esto garantizamos que se guarde el nuevo ataque en la BD
                em.flush();//con esto garantizamos que los datos se envien siempre
                //se fuerza
            }

            //Verificar si el Pokemon ya tiene el ataque
            Query queryAtaqueExistente = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon AND pa.ataque.idAtaque = :idAtaque");
            queryAtaqueExistente.setParameter("idPokemon", pokemon.getIdPokemon());
            queryAtaqueExistente.setParameter("idAtaque", nuevoAtaque.getIdAtaque());
            List<PokemonAtaque> ataquesDelPokemon = queryAtaqueExistente.getResultList();

            if (ataquesDelPokemon.isEmpty()) {
                PokemonAtaque nuevoPokemonAtaque = new PokemonAtaque(pokemon, nuevoAtaque, "Mov. TM");
                em.persist(nuevoPokemonAtaque);
            }

            em.getTransaction().commit(); //se guarda todos los resultados en la BD
        } finally {
            em.close();
        }
    }

    //Un metood que verifica si este ataque ya existia anteriormente
    public boolean ataqueExiste(Ataque ataque) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre AND a.tipo = :tipo AND a.categoria = :categoria");
            query.setParameter("nombre", ataque.getNombreAtaque());
            query.setParameter("tipo", ataque.getTipo());
            query.setParameter("categoria", ataque.getCategoria());
            return !query.getResultList().isEmpty(); //y devuelve true si el ataque
            //ya existe en la bd
        } finally {
            em.close();
        }
    }

    public Ataque buscarAtaquePorNombre(String nombreAtaque) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Ataque> query = em.createQuery("SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre", Ataque.class);
        query.setParameter("nombre", nombreAtaque);
        List<Ataque> resultados = query.getResultList();
        if(resultados.isEmpty()){
            return null;
        } else {
            return resultados.get(0);
        }
    } finally {
        em.close();
    }
}
    //y este metodo es para guardar un nuevo ataque sin registrarlo a un pokemon
 // Guarda un nuevo ataque asegurando que los campos obligatorios tengan valores por defecto
    public void guardarNuevoAtaque(Ataque nuevoAtaque) {
        EntityManager em = emf.createEntityManager();
        try {
            // Verificamos que los valores obligatorios no sean nulos ni vacíos
            if (nuevoAtaque.getNombreAtaque() == null || nuevoAtaque.getNombreAtaque().isEmpty()) {
                nuevoAtaque.setNombreAtaque("Ataque desconocido");
            }
            if (nuevoAtaque.getTipo() == null || nuevoAtaque.getTipo().isEmpty()) {
                nuevoAtaque.setTipo("Normal");
            }
            if (nuevoAtaque.getCategoria() == null || nuevoAtaque.getCategoria().isEmpty()) {
                nuevoAtaque.setCategoria("Especial");
            }
            if (nuevoAtaque.getEfecto() == null || nuevoAtaque.getEfecto().isEmpty()) {
                nuevoAtaque.setEfecto("Sin efecto");
            }
            if (nuevoAtaque.getPotencia() == null) {
                nuevoAtaque.setPotencia(0);
            }
            if (nuevoAtaque.getPp() == null) {
                nuevoAtaque.setPp(10);
            }

            em.getTransaction().begin();
            em.persist(nuevoAtaque);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //Metodo para eliminar ataques
   public void eliminarAtaque(int idPokemon, int idAtaque) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        //Primero buscamos la relacion exacta entre el pokemon y los ataques
        Query queryRelacion = em.createQuery("SELECT pa FROM PokemonAtaque pa WHERE pa.pokemon.idPokemon = :idPokemon AND pa.ataque.idAtaque = :idAtaque");
        queryRelacion.setParameter("idPokemon", idPokemon);
        queryRelacion.setParameter("idAtaque", idAtaque);
        List<PokemonAtaque> relaciones = queryRelacion.getResultList();

        //y si existe esa relacion, la eliminamos
        if (!relaciones.isEmpty()) {
            for (PokemonAtaque pa : relaciones) {
                em.remove(pa);
            }
           em.getTransaction().commit(); //Confirmar cambios en la BD
            JOptionPane.showMessageDialog(null, "Ataque eliminado correctamente del Pokémon.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Este Pokémon no tiene asignado este ataque.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al eliminar el ataque: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        em.close(); //Cerrar conexion con la BD
    }
}
  public Ataque obtenerAtaquePorNombre(String nombreAtaque) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ataque> query = em.createQuery(
                "SELECT a FROM Ataque a WHERE a.nombreAtaque = :nombre", Ataque.class);
            query.setParameter("nombre", nombreAtaque);
            List<Ataque> resultados = query.getResultList();
            if (!resultados.isEmpty()) {
                return resultados.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
}