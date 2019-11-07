package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.PerguntaRelacionar;

public class PerguntaRelacionarDAO {
	public void armazenaDados(EntityManager em, PerguntaRelacionar p){
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<PerguntaRelacionar> getListPerguntasRelacionar(EntityManager em) {
		TypedQuery<PerguntaRelacionar> query = em.createQuery("SELECT p FROM PerguntaRelacionar p", PerguntaRelacionar.class);

		List<PerguntaRelacionar> perguntas = query.getResultList();

		// Isso aqui eh o maior bug que ja vi na face da terra, aparentente
		// nao "inicializa" a lista de alternativas, nao sendo possivel ter 
		// acesso a elas em outros metodos
		// de outras classes, sinistro
		for (PerguntaRelacionar p : perguntas) {
			p.getAlternativas().get(0);
			p.getAlternativasOrdemCorreta().get(0);
			p.getTextoAlternativas().get(0);
			p.getTextoRespostas().get(0);
		}
		
		em.close();
		
		return perguntas;
	}
}
