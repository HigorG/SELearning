package component.menu.professor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToolbar;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.partida.PartidaTelaComponent;
import dao.AlunoDAO;
import dao.AlunoPossuiMusicaDAO;
import dao.MusicaDAO;
import dao.PartidaDAO;
import dao.PerguntaDAO;
import dao.PerguntaMultiplaEscolhaDAO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Aluno;
import model.AlunoPossuiMusica;
import model.Musica;
import model.Partida;
import model.Pergunta;
import model.PerguntaMultiplaEscolha;
import util.UsuarioAtual;

public class MenuProfessorComponent extends BorderPane {
	public MenuProfessorComponent (Stage stage, MediaPlayer mediaPlayer) {
		System.out.println("Ta printando aqui: " + Main.getStack().getChildren());
		
		// Toolbar que aparece em cima
		JFXToolbar toolbar = new JFXToolbar();
		toolbar.setPadding(new Insets(10, 0, 10, 10));
		toolbar.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: solid none solid none;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: #dde3e7;"
		);
		
		Label lblMenu = new Label("Estatísticas dos Alunos");
		
		System.out.println("Viem ate aqui");
		
		inicializaToolbar(toolbar, lblMenu);
		
		System.out.println("Passei daqui");
		
		JFXTabPane tabpaneConteudo = new JFXTabPane();
		tabpaneConteudo.setPrefSize(stage.getWidth(), stage.getHeight());
		tabpaneConteudo.setStyle("-fx-background-color: white;");
		tabpaneConteudo.setDisableAnimation(true);
		tabpaneConteudo.getStyleClass().add("hiddenTabPane"); // Este comando aplica a class hiddenTabPane disponivel no css para retirar o header que contem as tabs no TabPane
		
		Tab tabMenu = new Tab("Menu Principal");
		Tab tabConteudo = new Tab("Conteudo");
		Tab tabIniciarPartida = new Tab("Iniciar Partida");
		Tab tabConfiguracoes = new Tab("Configurações");
		Tab tabEstatisticas = new Tab("Estatísticas");
		Tab tabSobre = new Tab("Sobre");
		
		int numTabMenu = 0;
		int numTabConteudo = 1;
		int numTabIniciarPartida = 2;
		int numTabConfiguracoes = 3;
		int numTabEstatisticas = 4;
		int numTabSobre = 5;
		
		criaTabMenu(tabpaneConteudo, tabMenu, numTabConteudo, numTabConfiguracoes, numTabEstatisticas, numTabSobre);
		criaTabConteudo(tabpaneConteudo, tabConteudo, numTabMenu, numTabIniciarPartida);
		criaTabIniciarPartida(tabpaneConteudo, tabIniciarPartida, numTabConteudo, stage, mediaPlayer);
		criaTabConfiguracoes(tabpaneConteudo, tabConfiguracoes, numTabMenu, mediaPlayer);
		criaTabEstatisticas(tabpaneConteudo, tabEstatisticas, numTabMenu, stage);
		criaTabSobre(tabpaneConteudo, tabSobre, numTabMenu);
		
		tabpaneConteudo.getTabs().addAll(
				tabMenu, 
				tabConteudo, 
				tabIniciarPartida, 
				tabConfiguracoes,
				tabEstatisticas,
				tabSobre
		);
		
		// O painel onde fica o menu
		HBox hboxTelaMenu = new HBox();
		hboxTelaMenu.setPadding(new Insets(40, 40, 40, 40));
		
		hboxTelaMenu.getChildren().add(tabpaneConteudo);
		
		this.setTop(toolbar);
		this.setCenter(hboxTelaMenu);
	}
	
	private void inicializaToolbar(JFXToolbar toolbar, Label lblMenu) {
		lblMenu.setStyle("-fx-font-size: 20px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		ImageView logo = new ImageView("/resources/images/Logo.png");
		logo.setFitWidth(150);
		logo.setPreserveRatio(true);
		logo.setSmooth(true);
		logo.setCache(true);
		
		HBox hboxPerfil = new HBox();
		
		Label nomeUsuario = new Label("Nome");
		nomeUsuario.setStyle("-fx-font-size: 20px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		/*
		JFXButton btnPerfil = new JFXButton("");
		
		btnPerfil.setStyle("-fx-background-radius: 80;-fx-background-color: #ffffff");
		btnPerfil.setPrefSize(20, 20);
		btnPerfil.setRipplerFill(Color.valueOf("#b1b3b5"));
        SVGGlyph glyph5 = new SVGGlyph(-1,
        	"test",
        	"M225.923,354.706c-8.098,0-16.195-3.092-22.369-9.263L9.27,151.157c-12.359-12.359-12.359-32.397,0-44.751\r\n" + 
        	"		c12.354-12.354,32.388-12.354,44.748,0l171.905,171.915l171.906-171.909c12.359-12.354,32.391-12.354,44.744,0\r\n" + 
        	"		c12.365,12.354,12.365,32.392,0,44.751L248.292,345.449C242.115,351.621,234.018,354.706,225.923,354.706z",
        	Color.web("#b1b3b5"));
        glyph5.setSize(15, 7.5);
        btnPerfil.setGraphic(glyph5);
		*/
		
        nomeUsuario.setPadding(new Insets(0, 15, 0, 15));
        // btnPerfil.setPadding(new Insets(0, 0, 0, 0));
        
		// hboxPerfil.getChildren().addAll(nomeUsuario, btnPerfil);
		hboxPerfil.getChildren().addAll(nomeUsuario);
		
		toolbar.setAlignment(lblMenu, Pos.CENTER);
		toolbar.setAlignment(logo, Pos.CENTER);
		toolbar.setAlignment(hboxPerfil, Pos.CENTER);
		hboxPerfil.setAlignment(Pos.CENTER);
		
		toolbar.setLeft(lblMenu);
		toolbar.setCenter(logo);
		toolbar.setRight(hboxPerfil);
		
		nomeUsuario.setText(UsuarioAtual.getUsuarioAtualProfessor().getNome());
	}

	private void criaTabMenu(JFXTabPane tabpaneConteudo, Tab tabMenu, int numTabConteudo, int numTabConfiguracoes, int numTabEstatisticas, int numTabSobre) {
		VBox vboxOpcoesMenuPrinciapl = new VBox();
		vboxOpcoesMenuPrinciapl.setPadding(new Insets(20, 20, 20, 20));
		
		// JFXButton btnJogar = new JFXButton("Jogar");
		JFXButton btnConfiguracoes = new JFXButton("Configurações");
		JFXButton btnEstaticas = new JFXButton("Estatísticas ");
		JFXButton btnSobre = new JFXButton("Sobre");
		JFXButton btnSair = new JFXButton("Sair");
		
		// btnJogar.setStyle("-fx-font-size: 20px;");
		btnConfiguracoes.setStyle("-fx-font-size: 20px;");
		btnEstaticas.setStyle("-fx-font-size: 20px;");
		btnSobre.setStyle("-fx-font-size: 20px;");
		btnSair.setStyle("-fx-font-size: 20px;");
		
		// vboxOpcoesMenuPrinciapl.getChildren().addAll(btnJogar, btnConfiguracoes, btnEstaticas, btnSobre, btnSair);
		vboxOpcoesMenuPrinciapl.getChildren().addAll(btnConfiguracoes, btnEstaticas, btnSobre, btnSair);
		
		tabMenu.setContent(vboxOpcoesMenuPrinciapl);
		
		/*
		btnJogar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabConteudo);
		});
		*/
		
		btnConfiguracoes.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabConfiguracoes);
		});
		
		btnEstaticas.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabEstatisticas);
		});
		
		btnSobre.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabSobre);
		});
		
		btnSair.setOnAction(ActionEvent -> {
			Platform.exit();
		});
	}
	
	private void criaTabConteudo(JFXTabPane tabpaneConteudo, Tab tabMenu2, int numTabMenu, int numTabIniciarPartida) {
		
	}
	
	private void criaTabIniciarPartida(JFXTabPane tabpaneConteudo, Tab tabIniciarPartida, int numTabConteudo, Stage stage, MediaPlayer mediaPlayer) {
		
	}
	
	private void criaTabConfiguracoes(JFXTabPane tabpaneConteudo, Tab tabConfiguracoes, int numTabMenu,
			MediaPlayer mediaPlayer) {
		VBox vboxOpcoesConfiguracoes = new VBox();
		vboxOpcoesConfiguracoes.setPadding(new Insets(20, 20, 20, 20));
		
		
		
		
		final JFXSlider volumeSlider = new JFXSlider(0, 1, 1);
		volumeSlider.setOrientation(Orientation.HORIZONTAL);
		volumeSlider.setStyle("");
		volumeSlider.getStyleClass().add("sliderVolume");
		mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
		
        Label lblOpcaoVolume = new Label("Volume");
        lblOpcaoVolume.setStyle("-fx-font-size: 20px;");
        
        
        HBox hboxVolume = new HBox();
        hboxVolume.getChildren().addAll(lblOpcaoVolume, volumeSlider);
        hboxVolume.setMargin(volumeSlider, new Insets(10, 0, 0, 10));
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		
		btnVoltar.setStyle("-fx-font-size: 20px;");

		hboxVolume.setPadding(new Insets(0, 0, 10, 14));
		
		vboxOpcoesConfiguracoes.getChildren().addAll(hboxVolume, btnVoltar);
		tabConfiguracoes.setContent(vboxOpcoesConfiguracoes);
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
	}
	
	private void criaTabEstatisticas(JFXTabPane tabpaneConteudo, Tab tabEstatisticas, int numTabMenu, Stage stage) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
		EntityManager emListUsuarios = emf.createEntityManager();
		
		AlunoDAO alunoDAO = new AlunoDAO();
		ObservableList<Aluno> listAlunos = FXCollections.observableArrayList(alunoDAO.getListAlunos(emListUsuarios));
		
		FlowPane flowPaneGraficos = new FlowPane();
		flowPaneGraficos.setPadding(new Insets(20, 20, 20, 20));
		
		VBox vboxGraficoPontuacao = new VBox();
		vboxGraficoPontuacao.setPrefSize(270, 270);
		
		Label lblGraficoPontuacao = new Label("Pontuação do aluno");
		lblGraficoPontuacao.setStyle("-fx-font-size: 20px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		BarChart<String, Number> graficoBarraPontuacao = new BarChart<>(new CategoryAxis(), new NumberAxis());
		
		for (Aluno a : listAlunos) {
			String nome = a.getNome();
			int pontuacao = a.getPontuacao();
			
			XYChart.Series pontuacaoEstatistica = new XYChart.Series();
			pontuacaoEstatistica.getData().add(new XYChart.Data("Pontos", pontuacao));
			pontuacaoEstatistica.setName(nome);
			
			graficoBarraPontuacao.getData().add(pontuacaoEstatistica);
		}
		
		vboxGraficoPontuacao.getChildren().addAll(lblGraficoPontuacao, graficoBarraPontuacao);
		
		graficoBarraPontuacao.setOnMouseClicked(e -> {
			/*
			StackPane stackPaneComponents = Main.getStack();
			
			JFXDialogLayout dialogContent = new JFXDialogLayout();
			dialogContent.setPrefSize(300, 450);
    		dialogContent.setHeading(new Text("Cadastro"));
    		// dialogContent.setBody(new Text("Preencha corretamente as informações!"));
			
			dialogContent.setPrefSize(tabpaneConteudo.getWidth(), tabpaneConteudo.getHeight());
			
			BarChart<String, Number> graficoBarraPontuacaoAux = new BarChart<>(new CategoryAxis(), new NumberAxis());
			graficoBarraPontuacaoAux = graficoBarraPontuacao;
			
    		dialogContent.getChildren().add(graficoBarraPontuacaoAux);
    		
    		JFXButton fechar = new JFXButton("OK");
    		fechar.setButtonType(JFXButton.ButtonType.RAISED);
    		fechar.setStyle("-fx-background-color: #1ab3ce;");
    		fechar.setTextFill(Color.WHITE);
    		// dialogContent.setActions(fechar);
    		dialogContent.setStyle("-fx-font-size: 14.0px;");
    		
    		JFXDialog dialogCadastro;
    		dialogCadastro = new JFXDialog(stackPaneComponents, dialogContent, JFXDialog.DialogTransition.CENTER);
    		fechar.setOnAction((ActionEvent ev) -> {
    			dialogCadastro.close();
    		});
    		dialogCadastro.show();
    		
    		dialogCadastro.setOnDialogClosed(Event -> {
    			// loginTelaComponent.setEffect(null);
    		});
    		
    		BoxBlur blur = new BoxBlur(3, 3, 3);
    		// loginTelaComponent.setEffect(blur);
			*/
		});
		
		EntityManagerFactory emfEstatisticas = Persistence.createEntityManagerFactory("objectdb/db/Estatisticas.odb");
		
		PartidaDAO partidaDAO = new PartidaDAO();
		PerguntaDAO perguntaDAO = new PerguntaDAO();
		
		VBox vboxGraficoErros = new VBox();
		vboxGraficoPontuacao.setPrefSize(270, 270);
		
		Label lblGraficoErros = new Label("Quantidade de Erros por partida");
		lblGraficoErros.setStyle("-fx-font-size: 20px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		Label lblAlunoId = new Label("ID do aluno: ");
		JFXComboBox<Long> comboBoxAlunos = new JFXComboBox<Long>();
		
		HBox hboxAlunos = new HBox();
		hboxAlunos.getChildren().addAll(lblAlunoId, comboBoxAlunos);
		
		for (Aluno a : listAlunos) {
			comboBoxAlunos.getItems().addAll(a.getId());
		}
		
		BarChart<String, Number> graficoErros = new BarChart<>(new CategoryAxis(), new NumberAxis());
		graficoErros.setPrefSize(270, 270);
		
		comboBoxAlunos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Long>() {
			@Override public void changed(ObservableValue<? extends Long> selected, Long oldValue, Long newValue) {
				graficoErros.getData().clear();
				
				Aluno selecionado = null;
				
				for (int i = 0; i < listAlunos.size(); i++) {
					Aluno a = listAlunos.get(i);
					
					if (a.getId() == newValue) {
						selecionado = a;
						break;
					}
				}
				
				String nome = selecionado.getNome();
				long alunoId = selecionado.getId();
				
				EntityManager emListPartidas = emfEstatisticas.createEntityManager();
				ObservableList<Partida> listPartidasPorAluno = FXCollections.observableArrayList(partidaDAO.getPartidasPorAluno(emListPartidas, alunoId));
				
				for (Partida pa : listPartidasPorAluno) {
					int partidaId = pa.getPartidaId();
					int indice = 0;
					
					// Faco isso pra pegar o numero da partida no vetor (o indice)
					for (int i = 0; i < listPartidasPorAluno.size(); i++) {
						if (listPartidasPorAluno.get(i).getPartidaId() == partidaId) {
							indice = i;
						}
					}
						
					int qtdErros = 0;
					EntityManager emListPerguntas = emfEstatisticas.createEntityManager();
					ObservableList<Pergunta> listPerguntas = 
						FXCollections.observableArrayList(
							perguntaDAO.getListPerguntasPorAlunoEPartida(
							emListPerguntas, 
							alunoId,
							partidaId
						)
					);
					
					for (Pergunta pe : listPerguntas) {
						qtdErros += pe.getQtdErros();
					}
					
					XYChart.Series totalErros = new XYChart.Series();
					totalErros.getData().add(new XYChart.Data(nome, qtdErros));
					totalErros.setName("Partida " + (indice + 1)); // O mais um é pra aparecer tipo "Partida 1"...
					
					graficoErros.getData().add(totalErros);
				}
			}
		});
		
		vboxGraficoErros.getChildren().addAll(lblGraficoErros, hboxAlunos, graficoErros);
		
		flowPaneGraficos.getChildren().addAll(vboxGraficoPontuacao, vboxGraficoErros);
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		btnVoltar.setStyle("-fx-font-size: 20px;");
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
		
		VBox vboxConteudo = new VBox();
		vboxConteudo.getChildren().addAll(flowPaneGraficos, btnVoltar);
		vboxConteudo.setMargin(btnVoltar, new Insets(-30, 0, 0, 0));
		
		tabEstatisticas.setContent(vboxConteudo);
	}
	
	private void criaTabSobre(JFXTabPane tabpaneConteudo, Tab tabSobre, int numTabMenu) {
		Label lblSobre = new Label("S(E)Learning é um jogo estilo Quizz que tem por objetivo"
				+ "servir como uma plataforma para auxiliar no ensino de Engenharia de Software. "
				+ "A versão atual do jogo contempla o Diagrama de Casos de Uso. Os tipos de perguntas "
				+ "presentes até a momento são de múltipla escolha, verdadeiro ou falso e relacionar. "
				+ "No jogo, o jogador recebe recompensas como pontuação e moedas. As moedas podem "
				+ "ser utilizadas na loja para obter itens que auxiliam durante a partida ou músicas "
				+ "para tornar o ambiente mais confortável.");
		lblSobre.setWrapText(true);
		lblSobre.setStyle("-fx-font-size: 20px;");
		lblSobre.setPadding(new Insets(0, 0, 0, 14));
		
		VBox vboxOpcoesSobre = new VBox();
		vboxOpcoesSobre.setPadding(new Insets(20, 20, 20, 20));
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		btnVoltar.setStyle("-fx-font-size: 20px;");

		vboxOpcoesSobre.getChildren().addAll(lblSobre, btnVoltar);
		tabSobre.setContent(vboxOpcoesSobre);

		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
	}
}
