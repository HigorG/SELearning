package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Musica;

public class MusicaDAO {
	public void armazenaDados(EntityManager em, Musica m){
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Musica> getListMusicas(EntityManager em) {
		TypedQuery<Musica> query = em.createQuery("SELECT m FROM Musica m", Musica.class);

		List<Musica> itens = query.getResultList();
		em.close();
		
		return itens;
	}
}
