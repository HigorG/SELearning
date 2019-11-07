package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Aluno;

public class AlunoDAO {
	public void armazenaDados(EntityManager em, Aluno a){
		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Aluno> getListAlunos(EntityManager em) {
		TypedQuery<Aluno> query = em.createQuery("SELECT p FROM Aluno p", Aluno.class);

		List<Aluno> alunos = query.getResultList();
		em.close();
		
		return alunos;
	}
	
	public Aluno getAlunosPorId(EntityManager em, long id) {
		TypedQuery<Aluno> query = em.createQuery("SELECT p FROM Aluno p", Aluno.class);

		List<Aluno> alunos = query.getResultList();
		
		Aluno aluno = null;
		
		for (Aluno a : alunos) {
			if (a.getId() == id) {
				aluno = a;
				break;
			}
		}
		em.close();
		
		return aluno;
	}
	
	public void atualizaDados(EntityManager em, Aluno alunoSelecionado, Aluno alunoAtualizado){
		em.getTransaction().begin();
		
		TypedQuery<Aluno> query = em.createQuery("SELECT p FROM Aluno p WHERE p.getId() = " + alunoSelecionado.getId(), Aluno.class);
		for (Aluno p : query.getResultList()) {
			p.setPontuacao(alunoAtualizado.getPontuacao());
			p.setBitcoins(alunoAtualizado.getBitcoins());
		}
		em.getTransaction().commit();
		em.close();
	}
}
