package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.ConfiguracoesJogo;

public class ConfiguracoesJogoDAO {
	public void armazenaDados(EntityManager em, ConfiguracoesJogo c){
		em.getTransaction().begin();
		em.persist(c);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<ConfiguracoesJogo> getListConfiguracoesJogo(EntityManager em) {
		TypedQuery<ConfiguracoesJogo> query = em.createQuery("SELECT c FROM ConfiguracoesJogo c", ConfiguracoesJogo.class);

		List<ConfiguracoesJogo> configuracoesJogo = query.getResultList();
		
		em.close();
		
		return configuracoesJogo;
	}
}
