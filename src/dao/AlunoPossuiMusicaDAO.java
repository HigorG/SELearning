package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.AlunoPossuiMusica;

public class AlunoPossuiMusicaDAO {
	public void armazenaDados(EntityManager em, AlunoPossuiMusica a){
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<AlunoPossuiMusica> getListAlunoPossuiMusicas(EntityManager em) {
		TypedQuery<AlunoPossuiMusica> query = em.createQuery("SELECT apm FROM AlunoPossuiMusica apm", AlunoPossuiMusica.class);

		List<AlunoPossuiMusica> itens = query.getResultList();
		em.close();
		
		return itens;
	}
}
