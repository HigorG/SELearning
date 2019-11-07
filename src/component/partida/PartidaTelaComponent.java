package component.partida;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;

import component.timer.Timer;
import component.timer.TimerBuilder;
import component.timer.TimerEvent;
import component.timer.TimerEvent.Type;
import dao.PartidaDAO;
import dao.PerguntaMultiplaEscolhaDAO;
import dao.PerguntaRelacionarDAO;
import dao.PerguntaVerdadeiroFalsoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.Partida;
import model.PerguntaMultiplaEscolha;
import model.PerguntaRelacionar;
import model.PerguntaVerdadeiroFalso;
import util.Configuracoes;
import util.InfoPartida;
import util.InfoSessao;
import util.UsuarioAtual;

public class PartidaTelaComponent extends BorderPane {
	private Label lblPontuacao = new Label(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getPontuacao() + InfoPartida.alunoPontuacao));
	
	public PartidaTelaComponent (Stage stage, MediaPlayer mediaPlayer) {
		EntityManagerFactory emfGameDataBase = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		EntityManager emListQuestoesMultiplaEscolha = emfGameDataBase.createEntityManager();
		EntityManager emListQuestoesVerdadeiroFalso = emfGameDataBase.createEntityManager();
		EntityManager emListQuestoesRelacionar = emfGameDataBase.createEntityManager();
		
		PerguntaMultiplaEscolhaDAO perguntaMultiplaEscolhaDAO = new PerguntaMultiplaEscolhaDAO();
		PerguntaVerdadeiroFalsoDAO perguntaVerdadeiroFalsoDAO = new PerguntaVerdadeiroFalsoDAO();
		PerguntaRelacionarDAO perguntaRelacionarDAO = new PerguntaRelacionarDAO();
		
		ObservableList<PerguntaMultiplaEscolha> listQuestoesMultiplaEscolha = FXCollections.observableArrayList(perguntaMultiplaEscolhaDAO.getListPerguntasMultiplaEscolha(emListQuestoesMultiplaEscolha));
		ObservableList<PerguntaVerdadeiroFalso> listPerguntasVerdadeiroFalso = FXCollections.observableArrayList(perguntaVerdadeiroFalsoDAO.getListPerguntasVerdadeiroFalso(emListQuestoesVerdadeiroFalso));
		ObservableList<PerguntaRelacionar> listPerguntasRelacionar = FXCollections.observableArrayList(perguntaRelacionarDAO.getListPerguntasRelacionar(emListQuestoesRelacionar));
		
		// Define a quantidade de questoes baseado na soma de perguntas de cada tipo presentes no banco
		InfoPartida.numQuestoes = listQuestoesMultiplaEscolha.size() + listPerguntasVerdadeiroFalso.size() + listPerguntasRelacionar.size();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Estatisticas.odb");
		EntityManager emListPartidas = emf.createEntityManager();
		EntityManager emCriaPartida = emf.createEntityManager();
		
		PartidaDAO partidaDAO = new PartidaDAO();
		
		List<Partida> qtdPartidas = new ArrayList<Partida>();
		qtdPartidas = partidaDAO.getListPartidas(emListPartidas);
		
		// Atualiza no banco
		
		Timer timer = TimerBuilder.create()
                .playButtonVisible(true)
                .waitingColor(Color.GRAY)
                .duration(Duration.seconds(Configuracoes.tempoMaxParaResponder))
                .prefSize(130, 130)
                .build(stage, mediaPlayer);
		timer.setId("timer");
		
		timer.setOnTimerEvent(event -> {
			switch(event.getType()) {
			    case STARTED  : break;
			    case CONTINUED: System.out.println(timer.getProgress() * 100); break;
			    case STOPPED  : timer.setPlayButtonVisible(true); break;
			    case FINISHED : break;
			    case WAITING  : break;
			    default: break;
			}
			System.out.println(event.getType());
		});
		
		if (Configuracoes.tempoParaResponderAtivado != 1) {
			timer.setDisable(true);
			timer.setVisible(false);
		} else {
			timer.start();
		}
		
		double progresso = timer.getProgress();
		int tempo = (int) progresso;
		
		InfoPartida.partidaId = qtdPartidas.size() + 1;
		
		partidaDAO.armazenaDados(emCriaPartida, new Partida(
				UsuarioAtual.getUsuarioAtualAluno().getId(),
				InfoPartida.partidaId,
				tempo
		));
		
		FlowPane flowPaneVidas = new FlowPane();
		flowPaneVidas.setPrefSize(0, 0);
		
		// Configuracoes.ordem = configuracoes.getOrdem();
		
		for (int i = 0; i < Configuracoes.qtdVidas; i++) {
			JFXButton vida = new JFXButton();
			vida.getStyleClass().add("vida");
			vida.setRipplerFill(Color.TRANSPARENT);
			vida.setDisableVisualFocus(true);
			
			flowPaneVidas.getChildren().add(vida);
		}
		
		BarraPerfilComponent barraPerfil = new BarraPerfilComponent(stage, lblPontuacao);
		OpcoesLateraisComponent opcoesLaterais = new OpcoesLateraisComponent(stage, timer, mediaPlayer);
		ConteudoPartidaComponent conteudoPartida = new ConteudoPartidaComponent(stage, timer, mediaPlayer, flowPaneVidas, lblPontuacao);
		
		// Esse setTop() vai receber futuramente a profile-bar do menu
		this.setTop(barraPerfil);
		this.setLeft(opcoesLaterais);
		this.setCenter(conteudoPartida);
		
		/*
		stage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
		        System.out.println("maximized:" + t1.booleanValue());
		        if (t1.booleanValue()) {
		        	vboxMenuLateralEsquerdo.setPadding(new Insets(0, 190, 0, 0));
		        	vboxMenuLateralDireito.setPadding(new Insets(0, 240, 0, 0));
		        } else {
		        	vboxMenuLateralEsquerdo.setPadding(new Insets(0, 140, 0, 0));
		        	vboxMenuLateralDireito.setPadding(new Insets(0, 180, 0, 0));
		        }
		    }
		});
		*/
	}
}
