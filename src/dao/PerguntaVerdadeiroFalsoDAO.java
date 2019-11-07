package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.PerguntaVerdadeiroFalso;

public class PerguntaVerdadeiroFalsoDAO {
	public void armazenaDados(EntityManager em, PerguntaVerdadeiroFalso p){
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<PerguntaVerdadeiroFalso> getListPerguntasVerdadeiroFalso(EntityManager em) {
		TypedQuery<PerguntaVerdadeiroFalso> query = em.createQuery("SELECT p FROM PerguntaVerdadeiroFalso p", PerguntaVerdadeiroFalso.class);

		List<PerguntaVerdadeiroFalso> perguntas = query.getResultList();

		// Isso aqui eh o maior bug que ja vi na face da terra, aparentente
		// nao "inicializa" a lista de alternativas, nao sendo possivel ter 
		// acesso a elas em outros metodos
		// de outras classes, sinistro
		for (PerguntaVerdadeiroFalso p : perguntas) {
			p.getAlternativas().get(0);
		}
		
		em.close();
		
		return perguntas;
	}
}
