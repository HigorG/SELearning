package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Item;

public class ItemDAO {
	public void armazenaDados(EntityManager em, Item i){
		em.getTransaction().begin();
		em.persist(i);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Item> getListItens(EntityManager em) {
		TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i", Item.class);

		List<Item> itens = query.getResultList();
		em.close();
		
		return itens;
	}
	
	public Item getItensPorAlunoId(EntityManager em, long id) {
		TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i "
				+ "WHERE i.alunoId = " + id, Item.class);

		List<Item> itens = query.getResultList();
		
		Item item = null;
		
		for (Item i : itens) {
			if (i.getId() == id) {
				item = i;
				break;
			}
		}
		em.close();
		
		return item;
	}
}
