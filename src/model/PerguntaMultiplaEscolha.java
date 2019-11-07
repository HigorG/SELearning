package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PerguntaMultiplaEscolha {
	@Id @GeneratedValue
	private long id;
	
	private String descricao;
	private String fonte;
	private int alternativaCorreta;
	private List<String> alternativas = new ArrayList<String>();
	
	public PerguntaMultiplaEscolha(String descricao, String fonte, int alternativaCorreta, List<String> alternativas){
		this.descricao = descricao;
		this.fonte = fonte;
		this.alternativaCorreta = alternativaCorreta;
		this.alternativas = alternativas;
	}
	
	public PerguntaMultiplaEscolha(long id, String descricao, String fonte, int alternativaCorreta, List<String> alternativas){
		this.id = id;
		this.descricao = descricao;
		this.fonte = fonte;
		this.alternativaCorreta = alternativaCorreta;
		this.alternativas = alternativas;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getFonte() {
		return fonte;
	}
	
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public int getAlternativaCorreta() {
		return alternativaCorreta;
	}

	public void setAlternativaCorreta(int alternativaCorreta) {
		this.alternativaCorreta = alternativaCorreta;
	}
	
	public List<String> getAlternativas() {
		return alternativas;
	}
	
	public void setAlternativas(List<String> alternativas) {
		this.alternativas = alternativas;
	}
}
