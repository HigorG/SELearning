package component.partida;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;

import component.pergunta.multiplaescolha.PerguntaMultiplaEscolhaComponent;
import component.pergunta.relacionar.PerguntaRelacionarComponent;
import component.pergunta.verdadeirofalso.PerguntaVerdadeiroFalsoComponent;
import component.timer.Timer;
import dao.PerguntaMultiplaEscolhaDAO;
import dao.PerguntaRelacionarDAO;
import dao.PerguntaVerdadeiroFalsoDAO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.PerguntaMultiplaEscolha;
import model.PerguntaRelacionar;
import model.PerguntaVerdadeiroFalso;
import util.InfoPartida;
import util.InfoSessao;

public class ConteudoQuestaoComponent extends VBox {
	public ConteudoQuestaoComponent (Stage stage, Timer timer, MediaPlayer mediaPlayer, Label lblTituloSecundario, Label status, Label lblPontuacao, ProgressBar bar, FlowPane flowPaneVidas) {
		JFXTabPane jfxTabPanePerguntas = new JFXTabPane();
		jfxTabPanePerguntas.getStyleClass().add("hiddenTabPane"); // Este comando aplica a class hiddenTabPane disponivel no css para retirar o header que contem as tabs no TabPane
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
		
		EntityManager emPerguntasMultiplaEscolha = emf.createEntityManager();
		PerguntaMultiplaEscolhaDAO perguntaMultiplaEscolhaDAO = new PerguntaMultiplaEscolhaDAO();
		ObservableList<PerguntaMultiplaEscolha> listPerguntasMultiplaEscolhaDAO = FXCollections.observableArrayList(perguntaMultiplaEscolhaDAO.getListPerguntasMultiplaEscolha(emPerguntasMultiplaEscolha));
		
		EntityManager emPerguntasVerdadeiroFalso = emf.createEntityManager();
		PerguntaVerdadeiroFalsoDAO perguntaVerdadeiroFalsoDAO = new PerguntaVerdadeiroFalsoDAO();
		ObservableList<PerguntaVerdadeiroFalso> listPerguntasVerdadeiroFalso = FXCollections.observableArrayList(perguntaVerdadeiroFalsoDAO.getListPerguntasVerdadeiroFalso(emPerguntasVerdadeiroFalso));
		
		EntityManager emPerguntasRelacionar = emf.createEntityManager();
		PerguntaRelacionarDAO perguntaRelacionarDAO = new PerguntaRelacionarDAO();
		ObservableList<PerguntaRelacionar> listPerguntasRelacionar = FXCollections.observableArrayList(perguntaRelacionarDAO.getListPerguntasRelacionar(emPerguntasRelacionar));
		
		for (PerguntaMultiplaEscolha p : listPerguntasMultiplaEscolhaDAO) {
			long id = p.getId();
			String descricao = p.getDescricao();
			String fonte = p.getFonte();
			int alternativaCorreta = p.getAlternativaCorreta();
			List<String> alternativas = p.getAlternativas();
			
			PerguntaMultiplaEscolhaComponent pergunta = new PerguntaMultiplaEscolhaComponent(stage, timer, mediaPlayer, jfxTabPanePerguntas, id, descricao, fonte, alternativaCorreta, alternativas, lblTituloSecundario, lblPontuacao, status, bar, flowPaneVidas);
			Tab tab = new Tab(String.valueOf(id));
			tab.setContent(pergunta);
			jfxTabPanePerguntas.getTabs().add(tab);
		}
		
		for (PerguntaVerdadeiroFalso p : listPerguntasVerdadeiroFalso) {
			long id = p.getId();
			String descricao = p.getDescricao();
			String fonte = p.getFonte();
			int alternativaCorreta = p.getAlternativaCorreta();
			List<String> alternativas = p.getAlternativas();
			
			PerguntaVerdadeiroFalsoComponent pergunta = new PerguntaVerdadeiroFalsoComponent(stage, timer, mediaPlayer, jfxTabPanePerguntas, id, descricao, fonte, alternativaCorreta, alternativas, lblTituloSecundario, lblPontuacao, status, bar, flowPaneVidas);
			Tab tab = new Tab(String.valueOf(id));
			tab.setContent(pergunta);
			jfxTabPanePerguntas.getTabs().add(tab);
		}
		
		for (PerguntaRelacionar p : listPerguntasRelacionar) {
			long id = p.getId();
			String descricao = p.getDescricao();
			String fonte = p.getFonte();
			
			List<String> textoAlternativas = p.getTextoAlternativas();
			List<Integer> alternativas = p.getAlternativas();
			
			List<Integer> alternativasOrdemCorreta = p.getAlternativasOrdemCorreta();
			List<String> textoRespostas = p.getTextoRespostas();
			
			PerguntaRelacionarComponent pergunta = new PerguntaRelacionarComponent(stage, timer, mediaPlayer, jfxTabPanePerguntas, id, descricao, fonte, textoAlternativas,
					alternativas, alternativasOrdemCorreta, textoRespostas, lblTituloSecundario, lblPontuacao, status, bar, flowPaneVidas);
			Tab tab = new Tab(String.valueOf(id));
			tab.setContent(pergunta);
			jfxTabPanePerguntas.getTabs().add(tab);
		}
		
		this.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: solid hidden hidden hidden;" // A borda que fica em cima da pergunta
				+ "-fx-border-width: 2;"
				+ "-fx-border-color: #dde3e7;"
				+ "-fx-font-size: 17px;"
		);
		this.setPadding(new Insets(5, 0, 0, 0));
		
		// TODO Não é TODO, mas isso aqui marca que embaralha as tabs, consequentemente, as questoes tbm
		Collections.shuffle(jfxTabPanePerguntas.getTabs());
		
		this.getChildren().addAll(jfxTabPanePerguntas);
		
		/*
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			// System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
			// vboxMenuLateralEsquerdo.setPadding(new Insets(0,  (15.56 / 100) * stage.getWidth(), 0, 0));
			// vboxMenuLateralDireito.setPadding(new Insets(0,  (18.00 / 100) * stage.getWidth(), 0, 0));

			// FlowPane.setMargin(vbox2Alternativas, new Insets(0, 0, 0, stage.getWidth() - 900));
			textAreaDescricao.setPrefHeight((35.0 / 100) * stage.getHeight());
			
			// flowPaneAlternativas1.setPadding(new Insets((5.0 / 100) * stage.getHeight(), (5.0 / 100) * stage.getWidth(), (5.0 / 100) * stage.getHeight(), (5.0 / 100) * stage.getWidth()));
		
			// Aqui o botao vai ficar do tamanho do botao maior, pra manter o padrao na interface
			alternativa1.setPrefSize(alternativa2.getWidth(), alternativa2.getHeight());
		};
		
		stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
		*/
		
	    /*
	    List<String> fontes = new ArrayList<String>();
	    fontes = javafx.scene.text.Font.getFamilies();
	    for (int i = 0; i < fontes.size(); i++) {
	    	System.out.println(fontes.get(i));
	    }
	    */

	    
	}
}
