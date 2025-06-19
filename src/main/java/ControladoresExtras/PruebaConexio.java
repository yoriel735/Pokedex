/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControladoresExtras;

import Entidades.Entrenador;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author yoriel
 */

//clase de prueba para la conexion, no la borro por si en futuro es necesaria
public class PruebaConexio {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("pokemondb");
            em = emf.createEntityManager();

            TypedQuery<Entrenador> query = em.createQuery("SELECT e FROM Entrenador e", Entrenador.class);
            List<Entrenador> entrenadores = query.getResultList();

             System.out.println("Número de entrenadores: " + entrenadores.size());
            for (Entrenador e : entrenadores) {
                System.out.println("Entrenador: " + e.getNomEntrenador());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}

