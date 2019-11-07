package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Aluno {
	private String nome;
	private String email;
	private String senha;
	private int anoNascimento; // Opcional
	private String telefone; // Opcional
	private String sexo; // Opcional
	private int pontuacao;
	private int bitcoins;
	
	@Id @GeneratedValue
	private long id;
	
	public Aluno(String nome, String email, String senha){
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.anoNascimento = 2019;
		this.telefone = "";
		this.sexo = "";
		this.pontuacao = 0;
		this.bitcoins = 0;
	}
	
	public Aluno(int pontuacao, int bitcoins){
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.anoNascimento = 2019;
		this.telefone = "";
		this.sexo = "";
		this.pontuacao = pontuacao;
		this.bitcoins = bitcoins;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public int getAnoNascimento() {
		return anoNascimento;
	}
	
	public void setAnoNascimento(int anoNascimento) {
		this.anoNascimento = anoNascimento;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getPontuacao() {
		return pontuacao;
	}
	
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public int getBitcoins() {
		return bitcoins;
	}

	public void setBitcoins(int bitcoins) {
		this.bitcoins = bitcoins;
	}
}
