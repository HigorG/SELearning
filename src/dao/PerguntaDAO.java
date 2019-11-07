package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Pergunta;

public class PerguntaDAO {
	public void armazenaDados(EntityManager em, Pergunta a){
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Pergunta> getListPerguntas(EntityManager em) {
		TypedQuery<Pergunta> query = em.createQuery("SELECT p FROM Pergunta p", Pergunta.class);

		List<Pergunta> perguntas = query.getResultList();
		em.close();
		
		return perguntas;
	}
	
	// SELECT COUNT(p.qtdErros) FROM Pergunta p WHERE partidaId = 1 AND alunoId = 3
	public List<Pergunta> getListPerguntasPorAlunoEPartida(EntityManager em, long alunoId, int partidaId) {
		TypedQuery<Pergunta> query = em.createQuery(
				"SELECT p FROM Pergunta "
				+ "p WHERE partidaId = " + partidaId
				+ " AND alunoId = " + alunoId, Pergunta.class);

		List<Pergunta> perguntas = query.getResultList();
		
		em.close();
		
		return perguntas;
	}
	
	public Pergunta getPerguntasPorId(EntityManager em, long id) {
		TypedQuery<Pergunta> query = em.createQuery("SELECT p FROM Pergunta p", Pergunta.class);

		List<Pergunta> perguntas = query.getResultList();
		
		Pergunta pergunta = null;
		
		for (Pergunta p : perguntas) {
			if (p.getPerguntaId() == id) {
				pergunta = p;
				break;
			}
		}
		em.close();
		
		return pergunta;
	}
}
