package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pergunta {
	private long alunoId;
	private int partidaId;
	private long perguntaId;
	private String tipoPergunta;
	private int qtdErros;
	
	@Id @GeneratedValue
	private long objectId;
	
	public Pergunta(long alunoId, int partidaId, long perguntaId, String tipoPergunta, int qtdErros) {
		this.alunoId = alunoId;
		this.partidaId = partidaId;
		this.perguntaId = perguntaId;
		this.tipoPergunta = tipoPergunta;
		this.qtdErros = qtdErros;
	}

	public long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(long alunoId) {
		this.alunoId = alunoId;
	}
	
	public int getPartidaId() {
		return partidaId;
	}

	public void setPartidaId(int partidaId) {
		this.partidaId = partidaId;
	}
	
	public long getPerguntaId() {
		return perguntaId;
	}

	public void setPerguntaId(long perguntaId) {
		this.perguntaId = perguntaId;
	}

	public String getTipoPergunta() {
		return tipoPergunta;
	}

	public void setTipoPergunta(String tipoPergunta) {
		this.tipoPergunta = tipoPergunta;
	}

	public int getQtdErros() {
		return qtdErros;
	}

	public void setQtdErros(int qtdErros) {
		this.qtdErros = qtdErros;
	}
}
