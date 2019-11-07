package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {
	@Id @GeneratedValue
	private long id;
	
	private String nome;
	private String descricao;
	private String imgNome;
	private int preco;
	
	public Item(String nome, String descricao, String imgNome, int preco) {
		this.nome = nome;
		this.descricao = descricao;
		this.imgNome = imgNome;
		this.preco = preco;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getImgNome() {
		return imgNome;
	}
	
	public void setImgNome(String imgNome) {
		this.imgNome = imgNome;
	}
	
	public int getPreco() {
		return preco;
	}
	
	public void setPreco(int preco) {
		this.preco = preco;
	}
}
