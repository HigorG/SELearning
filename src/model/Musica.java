package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Musica {
	@Id @GeneratedValue
	private long id;
	
	private String nome;
	private String arquivoNome;
	private String imgNome;
	private int preco;
	
	public Musica(String nome, String arquivoNome, String imgNome, int preco) {
		super();
		this.nome = nome;
		this.arquivoNome = arquivoNome;
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
	
	public String getArquivoNome() {
		return arquivoNome;
	}
	
	public void setArquivoNome(String arquivoNome) {
		this.arquivoNome = arquivoNome;
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
