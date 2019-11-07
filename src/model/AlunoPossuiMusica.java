package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AlunoPossuiMusica {
	@Id @GeneratedValue
	private long id;
	
	private long alunoId;
	private long musicaId;
	
	public AlunoPossuiMusica(long alunoId, long musicaId) {
		this.alunoId = alunoId;
		this.musicaId = musicaId;
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
	
	public long getMusicaId() {
		return musicaId;
	}
	
	public void setMusicaId(long musicaId) {
		this.musicaId = musicaId;
	}
}
