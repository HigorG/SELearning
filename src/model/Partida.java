package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Partida {
	private long alunoId;
	private int partidaId;
	private int duracaoPartida;
	
	@Id @GeneratedValue
	private long objectId;

	public Partida(long alunoId, int partidaId, int duracaoPartida) {
		this.alunoId = alunoId;
		this.partidaId = partidaId;
		this.duracaoPartida = duracaoPartida;
	}
	
	public long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(int alunoId) {
		this.alunoId = alunoId;
	}

	public int getPartidaId() {
		return partidaId;
	}

	public void setPartidaId(int partidaId) {
		this.partidaId = partidaId;
	}

	public int getDuracaoPartida() {
		return duracaoPartida;
	}

	public void setDuracaoPartida(int duracaoPartida) {
		this.duracaoPartida = duracaoPartida;
	}
}
