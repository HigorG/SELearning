package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Partida;

public class PartidaDAO {
	public void armazenaDados(EntityManager em, Partida a){
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Partida> getListPartidas(EntityManager em) {
		TypedQuery<Partida> query = em.createQuery("SELECT p FROM Partida p", Partida.class);

		List<Partida> partidas = query.getResultList();
		
		em.close();
		
		return partidas;
	}
	
	public Partida getPartidasPorId(EntityManager em, long id) {
		TypedQuery<Partida> query = em.createQuery("SELECT p FROM Partida p", Partida.class);

		List<Partida> partidas = query.getResultList();
		
		Partida partida = null;
		
		for (Partida p : partidas) {
			if (p.getPartidaId() == id) {
				partida = p;
				break;
			}
		}
		em.close();
		
		return partida;
	}
	
	public List<Partida> getPartidasPorAluno(EntityManager em, long id) {
		TypedQuery<Partida> query = em.createQuery("SELECT p FROM Partida p WHERE p.alunoId = " + id, Partida.class);

		List<Partida> partidas = query.getResultList();

		em.close();
		
		return partidas;
	}
}
