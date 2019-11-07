package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PerguntaRelacionar {
	@Id @GeneratedValue
	private long id;
	
	private String descricao;
	private String fonte;
	private List<String> textoAlternativas = new ArrayList<String>();
	private List<Integer> alternativas = new ArrayList<Integer>();
	
	private List<Integer> alternativasOrdemCorreta = new ArrayList<Integer>();
	private List<String> textoRespostas = new ArrayList<String>();

	public PerguntaRelacionar(String descricao, String fonte, List<String> textoAlternativas,
			List<Integer> alternativas, List<Integer> alternativasOrdemCorreta, List<String> textoRespostas) {
		this.descricao = descricao;
		this.fonte = fonte;
		this.textoAlternativas = textoAlternativas;
		this.alternativas = alternativas;
		this.alternativasOrdemCorreta = alternativasOrdemCorreta;
		this.textoRespostas = textoRespostas;
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
	
	public List<String> getTextoAlternativas() {
		return textoAlternativas;
	}

	public void setTextoAlternativas(List<String> textoAlternativas) {
		this.textoAlternativas = textoAlternativas;
	}
	
	public List<Integer> getAlternativas() {
		return alternativas;
	}
	
	public void setAlternativas(List<Integer> alternativas) {
		this.alternativas = alternativas;
	}
	
	public List<Integer> getAlternativasOrdemCorreta() {
		return alternativasOrdemCorreta;
	}
	
	public void setAlternativasOrdemCorreta(List<Integer> alternativasOrdemCorreta) {
		this.alternativasOrdemCorreta = alternativasOrdemCorreta;
	}

	public List<String> getTextoRespostas() {
		return textoRespostas;
	}

	public void setTextoRespostas(List<String> textoRespostas) {
		this.textoRespostas = textoRespostas;
	}
}
