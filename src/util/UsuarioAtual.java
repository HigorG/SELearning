package util;

import model.Aluno;
import model.Professor;

public class UsuarioAtual {
	public static Professor usuarioAtualProfessor;
	public static Aluno usuarioAtualAluno;

	public static Professor getUsuarioAtualProfessor() {
		return usuarioAtualProfessor;
	}

	public static void setUsuarioAtualProfessor(Professor usuarioAtual) {
		UsuarioAtual.usuarioAtualProfessor = usuarioAtual;
	}
	
	public static Aluno getUsuarioAtualAluno() {
		return usuarioAtualAluno;
	}

	public static void setUsuarioAtualAluno(Aluno usuarioAtual) {
		UsuarioAtual.usuarioAtualAluno = usuarioAtual;
	}
}
