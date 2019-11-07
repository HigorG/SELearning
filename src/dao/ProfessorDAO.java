package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Professor;

public class ProfessorDAO {
	public void armazenaDados(EntityManager em, Professor p){
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Professor> getListProfessores(EntityManager em) {
		TypedQuery<Professor> query = em.createQuery("SELECT p FROM Professor p", Professor.class);

		List<Professor> professores = query.getResultList();
		em.close();
		
		return professores;
	}
}
