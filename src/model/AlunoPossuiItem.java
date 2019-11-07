package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AlunoPossuiItem {
	@Id @GeneratedValue
	private long id;
	
	private long alunoId;
	private long itemId;
	private int qtd;
	
	public AlunoPossuiItem(long alunoId, long itemId, int qtd) {
		this.alunoId = alunoId;
		this.itemId = itemId;
		this.qtd = qtd;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getAlunoId() {
		return alunoId;
	}
	
	public void setAlunoId(long alunoId) {
		this.alunoId = alunoId;
	}
	
	public long getItemId() {
		return itemId;
	}
	
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public int getQtd() {
		return qtd;
	}
	
	public void setQtd(int qtd) {
		this.qtd = qtd;
	}
}
