package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ConfiguracoesJogo {
	private String ordem;
	private int tempoParaResponderAtivado;
	private int tempoMaxParaResponder;
	private int qtdVidas;
	
	@Id @GeneratedValue
	private long id;
	
	public ConfiguracoesJogo(String ordem, int tempoParaResponderAtivado, int tempoMaxParaResponder, int qtdVidas) {
		this.ordem = ordem;
		this.tempoParaResponderAtivado = tempoParaResponderAtivado;
		this.tempoMaxParaResponder = tempoMaxParaResponder;
		this.qtdVidas = qtdVidas;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getOrdem() {
		return ordem;
	}
	
	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
	
	public int getTempoParaResponderAtivado() {
		return tempoParaResponderAtivado;
	}
	
	public void setTempoParaResponderAtivado(int tempoParaResponderAtivado) {
		this.tempoParaResponderAtivado = tempoParaResponderAtivado;
	}
	
	public int getTempoMaxParaResponder() {
		return tempoMaxParaResponder;
	}
	
	public void setTempoMaxParaResponder(int tempoMaxParaResponder) {
		this.tempoMaxParaResponder = tempoMaxParaResponder;
	}
	
	public int getQtdVidas() {
		return qtdVidas;
	}
	
	public void setQtdVidas(int qtdVidas) {
		this.qtdVidas = qtdVidas;
	}
}
