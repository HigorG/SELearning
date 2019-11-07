package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.PerguntaMultiplaEscolha;

public class PerguntaMultiplaEscolhaDAO {
	public void armazenaDados(EntityManager em, PerguntaMultiplaEscolha p){
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<PerguntaMultiplaEscolha> getListPerguntasMultiplaEscolha(EntityManager em) {
		TypedQuery<PerguntaMultiplaEscolha> query = em.createQuery("SELECT p FROM PerguntaMultiplaEscolha p", PerguntaMultiplaEscolha.class);

		List<PerguntaMultiplaEscolha> perguntas = query.getResultList();

		// Isso aqui eh o maior bug que ja vi na face da terra, aparentente
		// nao "inicializa" a lista de alternativas, nao sendo possivel ter 
		// acesso a elas em outros metodos
		// de outras classes, sinistro
		for (PerguntaMultiplaEscolha p : perguntas) {
			p.getAlternativas().get(0);
		}
		
		em.close();
		
		return perguntas;
	}
}
