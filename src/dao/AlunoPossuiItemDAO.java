package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.AlunoPossuiItem;
import model.AlunoPossuiItem;

public class AlunoPossuiItemDAO {
	public void armazenaDados(EntityManager em, AlunoPossuiItem i){
		em.getTransaction().begin();
		em.persist(i);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<AlunoPossuiItem> getListAlunoPossuiItens(EntityManager em) {
		TypedQuery<AlunoPossuiItem> query = em.createQuery("SELECT i FROM AlunoPossuiItem i", AlunoPossuiItem.class);

		List<AlunoPossuiItem> itens = query.getResultList();
		em.close();
		
		return itens;
	}
	
	public void atualizaDados(EntityManager em, AlunoPossuiItem alunoPossuiItemSelecionado, AlunoPossuiItem alunoPossuiItemAtualizado){
		em.getTransaction().begin();
		
		TypedQuery<AlunoPossuiItem> query = em.createQuery("SELECT api FROM AlunoPossuiItem api WHERE api.getId() = " + alunoPossuiItemSelecionado.getId(), AlunoPossuiItem.class);
		for (AlunoPossuiItem api : query.getResultList()) {
			api.setQtd(api.getQtd() + alunoPossuiItemAtualizado.getQtd());
		}
		em.getTransaction().commit();
		em.close();
	}
}
