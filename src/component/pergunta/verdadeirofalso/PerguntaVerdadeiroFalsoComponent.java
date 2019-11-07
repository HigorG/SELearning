package component.pergunta.verdadeirofalso;

import static javafx.animation.Interpolator.EASE_BOTH;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.animation.JFXNodesAnimation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import component.timer.Timer;
import component.timer.TimerEvent;
import component.timer.TimerEvent.Type;
import dao.AlunoDAO;
import dao.PartidaDAO;
import dao.PerguntaDAO;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.Partida;
import model.Pergunta;
import util.Configuracoes;
import util.Global;
import util.InfoPartida;
import util.InfoSessao;
import util.UsuarioAtual;

public class PerguntaVerdadeiroFalsoComponent extends VBox {
	private TextArea textAreaDescricao;
	private Label lblFonte;

	public PerguntaVerdadeiroFalsoComponent (Stage stage, Timer timer, MediaPlayer mediaPlayer, JFXTabPane jfxTabPanePerguntas, long id, String descricao, String fonte, int alternativaCorreta, List<String> alternativas, Label lblTituloSecundario, Label status, Label lblPontuacao, ProgressBar bar, FlowPane flowPaneVidas) {
		textAreaDescricao = new TextArea(descricao);
		textAreaDescricao.setEditable(false);
		textAreaDescricao.setWrapText(true);
		textAreaDescricao.setPadding(new Insets(10, 0, 0, 0));
		textAreaDescricao.getStyleClass().add("descricaoPergunta");
		
		lblFonte = new Label(fonte);
		
		HBox hboxFonte = new HBox();
		hboxFonte.getChildren().addAll(lblFonte);
		
		FlowPane flowPaneAlternativas = new FlowPane();
		VBox vbox1Alternativas = new VBox();
		VBox vbox2Alternativas = new VBox();
		
//		int maiorAlternativa = maiorComprimentoTextoAlternativa(alternativas);
		
		for (int j = 0; j < Math.ceil(alternativas.size() / 2); j++) {
			JFXButton alternativa = criarBotaoAlternativa(stage, timer, mediaPlayer, id, jfxTabPanePerguntas, alternativas, j, alternativaCorreta, lblTituloSecundario, status, lblPontuacao, bar, flowPaneVidas);
			
//			String auxAlternativa = alternativas.get(maiorAlternativa);
//			JFXButton alternativaAux = new JFXButton(auxAlternativa);
//			alternativa.setPrefSize(alternativaAux.getWidth(), alternativaAux.getHeight());
//			
//			System.out.println("WIDTH: " + alternativaAux.getWidth() + " | HEIGHT: " + alternativaAux.getHeight());
			
			vbox1Alternativas.getChildren().add(alternativa);
			VBox.setMargin(alternativa, new Insets(0, 0, 10, 0));
		}
			
		for (int j = (int) Math.ceil(alternativas.size() / 2); j < alternativas.size(); j++) {
			JFXButton alternativa = criarBotaoAlternativa(stage, timer, mediaPlayer, id, jfxTabPanePerguntas, alternativas, j, alternativaCorreta, lblTituloSecundario, status, lblPontuacao, bar, flowPaneVidas);
			
//			String auxAlternativa = alternativas.get(maiorAlternativa);
//			JFXButton alternativaAux = new JFXButton(auxAlternativa);
//			alternativa.setPrefSize(alternativaAux.getWidth(), alternativaAux.getHeight());
			
			vbox2Alternativas.getChildren().add(alternativa);
			VBox.setMargin(alternativa, new Insets(0, 0, 10, 0));
		}

		vbox1Alternativas.setPadding(new Insets(15, 15, 15, 15));
		vbox2Alternativas.setPadding(new Insets(15, 15, 15, 15));
		flowPaneAlternativas.getChildren().addAll(vbox1Alternativas, vbox2Alternativas);
		this.getChildren().addAll(textAreaDescricao, hboxFonte, flowPaneAlternativas);
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			// System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
			// vboxMenuLateralEsquerdo.setPadding(new Insets(0,  (15.56 / 100) * stage.getWidth(), 0, 0));
			// vboxMenuLateralDireito.setPadding(new Insets(0,  (18.00 / 100) * stage.getWidth(), 0, 0));

			// FlowPane.setMargin(vbox2Alternativas, new Insets(0, 0, 0, stage.getWidth() - 900));
			textAreaDescricao.setPrefHeight((35.0 / 100) * stage.getHeight());
			
			// flowPaneAlternativas1.setPadding(new Insets((5.0 / 100) * stage.getHeight(), (5.0 / 100) * stage.getWidth(), (5.0 / 100) * stage.getHeight(), (5.0 / 100) * stage.getWidth()));
		
			// Aqui o botao vai ficar do tamanho do botao maior, pra manter o padrao na interface
			// alternativa1.setPrefSize(alternativa2.getWidth(), alternativa2.getHeight());
		};
		
	    /*
	    List<String> fontes = new ArrayList<String>();
	    fontes = javafx.scene.text.Font.getFamilies();
	    for (int i = 0; i < fontes.size(); i++) {
	    	System.out.println(fontes.get(i));
	    }
	    */

	    stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}

	private int maiorComprimentoTextoAlternativa(List<String> alternativas) {
		int maiorAlternativa = 0;
			
		for (int i = 0; i < alternativas.size() - 1; i++) {
			String alternativaAux1 = alternativas.get(i);
			String alternativaAux2 = alternativas.get(i+1);
			
			if (alternativaAux1.length() < alternativaAux2.length()) {
				maiorAlternativa = i + 1;
			}
		}
		
		return maiorAlternativa;
	}

	private JFXButton criarBotaoAlternativa(Stage stage, Timer timer, MediaPlayer mediaPlayer, long id, JFXTabPane jfxTabPanePerguntas, List<String> alternativas, int j, int alternativaCorreta, Label lblTituloSecundario, Label status, Label lblPontuacao, ProgressBar bar, FlowPane flowPaneVidas) {
		StackPane stack = Main.getStack();
		
		JFXButton alternativa = new JFXButton(alternativas.get(j));
		alternativa.setId(String.valueOf(j));
		alternativa.setDisableVisualFocus(true);
		alternativa.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: rgba(49, 170, 188, 1);");
		alternativa.setPrefWidth(250);
		
		alternativa.setOnAction(e -> {
			if (Integer.parseInt(alternativa.getId()) + 1 == alternativaCorreta) {
				AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/AlternativaCorreta.wav").toExternalForm());
				audioClip.play();
				
				InfoPartida.alunoPontuacao += 50;
				
				lblPontuacao.setText(String.valueOf((String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getPontuacao() + InfoPartida.alunoPontuacao))));
				
				if (InfoPartida.questaoAtual == InfoPartida.numQuestoes) {
					System.out.println("Cabou");
					
					Tab tab = new Tab("Fim");
					tab.setContent(new Label("Fim da partida. Parabéns!"));
					jfxTabPanePerguntas.getTabs().add(tab);
					
					jfxTabPanePerguntas.getSelectionModel().selectLast();
					
					
					
					
					
					StackPane main = new StackPane();

					Label lblRecompensa = new Label("Obter recompensa");
					lblRecompensa.setStyle("-fx-text-fill: white");
					
			        StackPane colorPane = new StackPane();
			        colorPane.setStyle("-fx-background-radius:50; -fx-min-width:50; -fx-min-height:50;");
			        colorPane.getStyleClass().add("blue-500");
			        colorPane.getChildren().add(lblRecompensa);
			        main.getChildren().add(colorPane);

			        StackPane wizard = new StackPane();
			        wizard.getChildren().add(main);
			        
			        StackPane nextPage = new StackPane();

			        ImageView trophyImage = new ImageView("/resources/images/trophy.png");
			        trophyImage.setFitWidth(160);
			        trophyImage.setPreserveRatio(true);
			        
			        Button btnBitcoins = new Button();
					btnBitcoins.getStyleClass().add("bitcoinIcon");
					btnBitcoins.setStyle("-width: 50; -height: 65; -fx-min-height: -height;\r\n" + 
							"    -fx-min-width: -width;\r\n" + 
							"    -fx-max-height: -height;\r\n" + 
							"    -fx-max-width: -width;");
					
					// Calculo de bitcoins que o usuario ganhou
					int qtdBitcoins = InfoPartida.alunoPontuacao / 70;
					
			        Label lblBitcoins = new Label(String.valueOf(qtdBitcoins));
			        lblBitcoins.setStyle("-fx-font-size: 70px; -fx-font-family: \"Yu Gothic UI Semilight\";");
			        
			        HBox hboxBitcoins = new HBox();
			        hboxBitcoins.getChildren().addAll(btnBitcoins, lblBitcoins);
			        hboxBitcoins.setAlignment(Pos.CENTER);
			        hboxBitcoins.setMargin(btnBitcoins, new Insets(0, 30, 0, 0));
			        
			        Button btnPontuacao = new Button();
					btnPontuacao.getStyleClass().add("pontuacaoIcon");
					btnPontuacao.setStyle("-width: 45; -height: 65; -fx-min-height: -height;\r\n" + 
							"    -fx-min-width: -width;\r\n" + 
							"    -fx-max-height: -height;\r\n" + 
							"    -fx-max-width: -width;");
					
					Label lblPont = new Label(String.valueOf(InfoPartida.alunoPontuacao));
					lblPont.setStyle("-fx-font-size: 80px; -fx-font-family: \"Yu Gothic UI Semilight\";");
					
					HBox hboxPontuacao = new HBox();
			        hboxPontuacao.getChildren().addAll(btnPontuacao, lblPont);
			        hboxPontuacao.setAlignment(Pos.CENTER);
			        hboxPontuacao.setMargin(btnPontuacao, new Insets(0, 30, 0, 0));
					
					VBox recompensas = new VBox();
					recompensas.getChildren().addAll(hboxPontuacao, hboxBitcoins);
			        
//					JFXButton btnJogarNovamente = new JFXButton("Jogar Novamente");
//					btnJogarNovamente.setButtonType(JFXButton.ButtonType.RAISED);
//					btnJogarNovamente.setStyle("-fx-background-color: #1ab3ce;");
//					btnJogarNovamente.setTextFill(Color.WHITE);
					
			        JFXButton btnVoltarMenuPrincipal = new JFXButton("Voltar ao Menu Principal");
			        SVGGlyph glyphOpcaoMenuPrincipal = new SVGGlyph(-1,
							"test",
							"M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z ",
							Color.valueOf("#b0b0b0"));
			        glyphOpcaoMenuPrincipal.setSize(18, 18);
			        btnVoltarMenuPrincipal.setButtonType(JFXButton.ButtonType.RAISED);
			        btnVoltarMenuPrincipal.setGraphic(glyphOpcaoMenuPrincipal);
			        
					HBox hboxBtnsAcoes = new HBox();
					hboxBtnsAcoes.getChildren().addAll(btnVoltarMenuPrincipal);
					hboxBtnsAcoes.setAlignment(Pos.CENTER);
					
					VBox vboxFimJogo = new VBox();
					vboxFimJogo.setAlignment(Pos.CENTER);
					vboxFimJogo.setStyle("-fx-background-color: white");
			        vboxFimJogo.setPrefWidth(1000);
			        vboxFimJogo.setPrefHeight(700);
			        
			        vboxFimJogo.getChildren().addAll(trophyImage, recompensas, hboxBtnsAcoes);
					
			        nextPage.getChildren().add(vboxFimJogo);
			        
			        StackPane.setAlignment(vboxFimJogo, Pos.CENTER);
			        StackPane.setMargin(vboxFimJogo, new Insets(-300, -200, 0, -200));

			        JFXNodesAnimation<StackPane, StackPane> animation = 
			        		new FlowPaneStackPaneJFXNodesAnimation(main, nextPage, wizard, colorPane);

			        colorPane.setOnMouseClicked((click) -> animation.animate());
			        
			        // StackPane.setMargin(main, new Insets(100));
			        wizard.setStyle("-fx-background-color:WHITE");
					
			        VBox vboxImage = new VBox();
			        vboxImage.setAlignment(Pos.CENTER);
			        vboxImage.setStyle("-fx-background-color: transparent;");
					
					ImageView winImage = new ImageView("/resources/images/winImage.png");
					winImage.setFitWidth(400);
					winImage.setPreserveRatio(true);
					
					vboxImage.getChildren().addAll(winImage);

					JFXDialogLayout dialogContent = new JFXDialogLayout();
					dialogContent.setHeading(vboxImage);
					// dialogContent.setBody(new Text("Deseja realmente abandonar a partida atual?"));
					dialogContent.setBody(wizard);
					
					dialogContent.setStyle("-fx-font-size: 14.0px;");
					
					JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);

					EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
					EntityManager em = emf.createEntityManager();
					
					// Atualiza no banco
					AlunoDAO alunoDAO = new AlunoDAO();
					alunoDAO.atualizaDados(em, UsuarioAtual.getUsuarioAtualAluno(), new Aluno(
							UsuarioAtual.getUsuarioAtualAluno().getPontuacao() +
							InfoPartida.alunoPontuacao, 
							UsuarioAtual.getUsuarioAtualAluno().getBitcoins() +
							qtdBitcoins
					));
					
					/// Atualiza a quantidade de pergunta de uma partida
					
					EntityManagerFactory emfEstatisticas = Persistence.createEntityManagerFactory("objectdb/db/Estatisticas.odb");
					EntityManager emCriaPergunta = emfEstatisticas.createEntityManager();
					
					PerguntaDAO perguntaDAO = new PerguntaDAO();
					perguntaDAO.armazenaDados(emCriaPergunta, new Pergunta(
							UsuarioAtual.getUsuarioAtualAluno().getId(), // O id do usuario atual
							InfoPartida.partidaId,
							id, // O id da pergunta sendo analisada
							"verdadeiroFalso", // O tipo da pergunta (isso que vai diferenciar quais perguntas foram respondidas)
							InfoPartida.qtdErros // Informa a quantidade de erros até acertar tal pergunta
					));
					
					InfoPartida.qtdErros = 0;
					
					// Reseta a pontuacao porque acabou a partida
					InfoPartida.alunoPontuacao = 0;
					InfoPartida.questaoAtual = 1;
					InfoPartida.qtdVidasAtual = Configuracoes.qtdVidas;
					timer.reset();

					// Atualiza na interface
					EntityManager emAtualiza = emf.createEntityManager();
					
					UsuarioAtual.setUsuarioAtualAluno(alunoDAO.getAlunosPorId(emAtualiza, UsuarioAtual.getUsuarioAtualAluno().getId()));
					
					btnVoltarMenuPrincipal.setOnAction((ActionEvent ev) -> {
						dialog.close();
						
						Main.getStack().getChildren().remove(0); // Remove o component de partida
						Main.getStack().getChildren().remove(0); // Remove o dialog
							
						MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
							
						Main.getStack().getChildren().add(menuAluno);
						
						stack.getChildren().get(0).setEffect(null);
						// this.setEffect(null);
					});
					
					dialog.show();
					
					dialog.setOverlayClose(false);
					
					dialog.setOnDialogClosed(Event -> {
						// this.setEffect(null);
					});
					
					BoxBlur blur = new BoxBlur(3, 3, 3);
					stack.getChildren().get(0).setEffect(blur);
				} else {
					InfoPartida.questaoAtual++;
					
					EntityManagerFactory emfEstatisticas = Persistence.createEntityManagerFactory("objectdb/db/Estatisticas.odb");
					EntityManager emCriaPergunta = emfEstatisticas.createEntityManager();
					
					PerguntaDAO perguntaDAO = new PerguntaDAO();
					perguntaDAO.armazenaDados(emCriaPergunta, new Pergunta(
							UsuarioAtual.getUsuarioAtualAluno().getId(), // O id do usuario atual
							InfoPartida.partidaId,
							id, // O id da pergunta sendo analisada
							"verdadeiroFalso", // O tipo da pergunta (isso que vai diferenciar quais perguntas foram respondidas)
							InfoPartida.qtdErros // Informa a quantidade de erros até acertar tal pergunta
					));
					
					InfoPartida.qtdErros = 0;
					
					lblTituloSecundario.setText("Questão #" + InfoPartida.questaoAtual);
	
					Service<Void> servico = new Service() {
			            @Override
			            protected Task createTask() {
			                return new Task() {
			                    @Override
			                    protected Void call() throws Exception {
			                        Thread.sleep(300);
			                        updateProgress(InfoPartida.questaoAtual, InfoPartida.numQuestoes);
			                        updateMessage("Questão " + InfoPartida.questaoAtual + " de " + InfoPartida.numQuestoes);
			                        return null;
			                    }
			                };
			            }
			        };
			        
			        status.textProperty().bind(servico.messageProperty());
			        bar.progressProperty().bind(servico.progressProperty());
			        servico.restart(); // precisa inicializar o Service
					
					jfxTabPanePerguntas.getSelectionModel().selectNext();
				}
			} else {
				// TODO Usuario pressiona a pergunta errada, desconta vida
				// TODO Usuario comete erro
				InfoPartida.qtdErros++;
				
				AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/AlternativaIncorreta.wav").toExternalForm());
				audioClip.play();
				
		        final FadeTransition fadeOut = new FadeTransition(Duration.millis(500));
		        fadeOut.setNode(alternativa);
		        fadeOut.setToValue(0.0);
		        fadeOut.playFromStart();
		        
		        alternativa.setDisable(true);
		        
		        if (!flowPaneVidas.getChildren().isEmpty()) {
		        	final FadeTransition fadeOutVida = new FadeTransition(Duration.millis(500));
		        	fadeOutVida.setNode(flowPaneVidas.getChildren().get(flowPaneVidas.getChildren().size()-1));
			        fadeOutVida.setToValue(0.0);
			        fadeOutVida.playFromStart();
			        
			        fadeOutVida.onFinishedProperty().set(ev -> {
			        	int indice = flowPaneVidas.getChildren().size() - 1;
			        	
			        	flowPaneVidas.getChildren().remove(indice);
			        	
						if (flowPaneVidas.getChildren().isEmpty()) {
							System.out.println("Acabaram as vidas");
							
							Tab tab = new Tab("Fim");
							tab.setContent(new Label("Fim da partida. Parabéns!"));
							jfxTabPanePerguntas.getTabs().add(tab);
							
							
							
							// TODO Refatorar esse trecho, já está sendo usado para mostrar quando o usuário ganha o jogo
							StackPane main = new StackPane();

							Label lblRecompensa = new Label("Ver Pontuacao Obtida");
							lblRecompensa.setStyle("-fx-text-fill: white");
							
					        StackPane colorPane = new StackPane();
					        colorPane.setStyle("-fx-background-radius:50; -fx-min-width:50; -fx-min-height:50;");
					        colorPane.getStyleClass().add("blue-500");
					        colorPane.getChildren().add(lblRecompensa);
					        main.getChildren().add(colorPane);

					        StackPane wizard = new StackPane();
					        wizard.getChildren().add(main);
					        
					        StackPane nextPage = new StackPane();

					        ImageView medalImage = new ImageView("/resources/images/medal.png");
					        medalImage.setFitWidth(160);
					        medalImage.setPreserveRatio(true);
					        
					        Button btnPontuacao = new Button();
							btnPontuacao.getStyleClass().add("pontuacaoIcon");
							btnPontuacao.setStyle("-width: 45; -height: 65; -fx-min-height: -height;\r\n" + 
									"    -fx-min-width: -width;\r\n" + 
									"    -fx-max-height: -height;\r\n" + 
									"    -fx-max-width: -width;");
							
							Label lblPont = new Label(String.valueOf(InfoPartida.alunoPontuacao));
							lblPont.setStyle("-fx-font-size: 80px; -fx-font-family: \"Yu Gothic UI Semilight\";");
							
							HBox hboxPontuacao = new HBox();
					        hboxPontuacao.getChildren().addAll(btnPontuacao, lblPont);
					        hboxPontuacao.setAlignment(Pos.CENTER);
					        hboxPontuacao.setMargin(btnPontuacao, new Insets(0, 30, 0, 0));
							
							VBox recompensas = new VBox();
							recompensas.getChildren().addAll(hboxPontuacao);
					        
//							JFXButton btnJogarNovamente = new JFXButton("Jogar Novamente");
//							btnJogarNovamente.setButtonType(JFXButton.ButtonType.RAISED);
//							btnJogarNovamente.setStyle("-fx-background-color: #1ab3ce;");
//							btnJogarNovamente.setTextFill(Color.WHITE);
							
							JFXButton btnVoltarMenuPrincipal = new JFXButton("Voltar ao Menu Principal");
					        SVGGlyph glyphOpcaoMenuPrincipal = new SVGGlyph(-1,
									"test",
									"M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z ",
									Color.valueOf("#b0b0b0"));
					        glyphOpcaoMenuPrincipal.setSize(18, 18);
					        btnVoltarMenuPrincipal.setButtonType(JFXButton.ButtonType.RAISED);
					        btnVoltarMenuPrincipal.setGraphic(glyphOpcaoMenuPrincipal);
					        
							HBox hboxBtnsAcoes = new HBox();
							hboxBtnsAcoes.getChildren().addAll(btnVoltarMenuPrincipal);
							hboxBtnsAcoes.setAlignment(Pos.CENTER);
							
							VBox vboxFimJogo = new VBox();
							vboxFimJogo.setAlignment(Pos.CENTER);
							vboxFimJogo.setStyle("-fx-background-color: white");
					        vboxFimJogo.setPrefWidth(1000);
					        vboxFimJogo.setPrefHeight(700);
					        
					        vboxFimJogo.getChildren().addAll(medalImage, recompensas, hboxBtnsAcoes);
							
					        nextPage.getChildren().add(vboxFimJogo);
					        
					        StackPane.setAlignment(vboxFimJogo, Pos.CENTER);
					        StackPane.setMargin(vboxFimJogo, new Insets(-300, -200, 0, -200));

					        JFXNodesAnimation<StackPane, StackPane> animation = 
					        		new FlowPaneStackPaneJFXNodesAnimation(main, nextPage, wizard, colorPane);

					        colorPane.setOnMouseClicked((click) -> animation.animate());
					        
					        // StackPane.setMargin(main, new Insets(100));
					        wizard.setStyle("-fx-background-color:WHITE");
							
					        VBox vboxImage = new VBox();
					        vboxImage.setAlignment(Pos.CENTER);
					        vboxImage.setStyle("-fx-background-color: transparent;");
							
							ImageView winImage = new ImageView("/resources/images/loseImage.png");
							winImage.setFitWidth(400);
							winImage.setPreserveRatio(true);
							
							vboxImage.getChildren().addAll(winImage);

							JFXDialogLayout dialogContent = new JFXDialogLayout();
							dialogContent.setHeading(vboxImage);
							// dialogContent.setBody(new Text("Deseja realmente abandonar a partida atual?"));
							dialogContent.setBody(wizard);
							
							dialogContent.setStyle("-fx-font-size: 14.0px;");
							
							JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
							dialog.setOverlayClose(false);
							
							EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
							EntityManager em = emf.createEntityManager();
							
							Aluno aluno = new Aluno(
									UsuarioAtual.getUsuarioAtualAluno().getPontuacao() +
									InfoPartida.alunoPontuacao, 
									UsuarioAtual.getUsuarioAtualAluno().getBitcoins()
													);
							
							AlunoDAO alunoDAO = new AlunoDAO();
							
							// Reseta a pontuacao porque acabou a partida
							InfoPartida.alunoPontuacao = 0;
							InfoPartida.questaoAtual = 1;
							InfoPartida.qtdVidasAtual = Configuracoes.qtdVidas;
							timer.reset();
							
							// Atualiza no banco
							alunoDAO.atualizaDados(em, UsuarioAtual.getUsuarioAtualAluno(), aluno);
							
							// Atualiza na interface
							EntityManager emAtualiza = emf.createEntityManager();
							UsuarioAtual.setUsuarioAtualAluno(alunoDAO.getAlunosPorId(emAtualiza, UsuarioAtual.getUsuarioAtualAluno().getId()));
							
							btnVoltarMenuPrincipal.setOnAction((ActionEvent event) -> {
								int tamanho = Main.getStack().getChildren().size();
								
								Main.getStack().getChildren().remove(0); // Remove o component de partida
								Main.getStack().getChildren().remove(0); // Remove o dialog
								
								MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
									
								Main.getStack().getChildren().add(menuAluno);
									
								dialog.close();
								
								stack.getChildren().get(0).setEffect(null);
								// this.setEffect(null);
							});
							
							dialog.show();
							
							dialog.setOnDialogClosed(Event -> {
								// this.setEffect(null);
							});
							
							BoxBlur blur = new BoxBlur(3, 3, 3);
							stack.getChildren().get(0).setEffect(blur);
							
							
							
							jfxTabPanePerguntas.getSelectionModel().selectLast();
						}
			        });
				}
			}
		});
		
		return alternativa;
	}
	
	private static final class FlowPaneStackPaneJFXNodesAnimation extends JFXNodesAnimation<StackPane, StackPane> {
        private final Pane tempPage;
        private final StackPane main;
        private final StackPane nextPage;
        private final StackPane wizard;
        private final StackPane colorPane1;

        private double newX;
        private double newY;

        FlowPaneStackPaneJFXNodesAnimation(final StackPane main, final StackPane nextPage, final StackPane wizard,
                                           final StackPane colorPane1) {
            super(main, nextPage);
            this.main = main;
            this.nextPage = nextPage;
            this.wizard = wizard;
            this.colorPane1 = colorPane1;
            tempPage = new Pane();
            newX = 0;
            newY = 0;
        }

        @Override
        public void init() {
            nextPage.setOpacity(0);
            
            if (!wizard.getChildren().contains(tempPage)) {
            	wizard.getChildren().add(tempPage);
            }
            
            if (!wizard.getChildren().contains(nextPage)) {
            	wizard.getChildren().add(nextPage);
            }
            
            newX = colorPane1.localToScene(colorPane1.getBoundsInLocal()).getMinX();
            newY = colorPane1.localToScene(colorPane1.getBoundsInLocal()).getMinY();
            
            if (!tempPage.getChildren().contains(colorPane1)) {
            	tempPage.getChildren().add(colorPane1);
            }
            
            colorPane1.setTranslateX(newX);
            colorPane1.setTranslateY(newY);
            
            if (!colorPane1.getChildren().isEmpty()) {
            	colorPane1.getChildren().remove(0);
            }
            
            // Isso previne que o evento possa ser disparado novamente
            colorPane1.setDisable(true);
        }

        @Override
        public void end() {

        }

        @Override
        public Animation animateSharedNodes() {
            return new Timeline();
        }

        @Override
        public Animation animateExit() {
            final Integer endValue = 0;
            return new Timeline(
                new KeyFrame(Duration.millis(300),
                             new KeyValue(main.opacityProperty(), endValue, EASE_BOTH)),
                new KeyFrame(Duration.millis(520),
                             new KeyValue(colorPane1.translateXProperty(), endValue, EASE_BOTH),
                             new KeyValue(colorPane1.translateYProperty(), endValue, EASE_BOTH)),
                new KeyFrame(Duration.millis(200),
                             new KeyValue(colorPane1.scaleXProperty(), 1, EASE_BOTH),
                             new KeyValue(colorPane1.scaleYProperty(), 1, EASE_BOTH)),
                new KeyFrame(Duration.millis(1000),
                             new KeyValue(colorPane1.scaleXProperty(), 40, EASE_BOTH),
                             new KeyValue(colorPane1.scaleYProperty(), 40, EASE_BOTH)));
        }

        @Override
        public Animation animateEntrance() {
            return new Timeline(new KeyFrame(Duration.millis(320),
                                             new KeyValue(nextPage.opacityProperty(), 1, EASE_BOTH)));
        }
    }
}
