package component.pergunta.relacionar;

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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import component.timer.Timer;
import dao.AlunoDAO;
import dao.PerguntaDAO;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.Pergunta;
import util.Configuracoes;
import util.InfoPartida;
import util.UsuarioAtual;

public class PerguntaRelacionarComponent extends VBox {
	private TextArea textAreaDescricao;
	// private Label lblFonte;

	JFXListView<Integer> sourceView = new JFXListView<>();
	JFXListView<Integer> targetView1 = new JFXListView<>();
	JFXListView<Integer> targetView2 = new JFXListView<>();
	JFXListView<Integer> targetView3 = new JFXListView<>();
	JFXListView<Integer> targetView4 = new JFXListView<>();

	// Create the LoggingArea
	TextArea loggingArea = new TextArea("");
	
	JFXButton btnValidar = new JFXButton("Validar");
	
	// Set the Custom Data Format
	static final DataFormat FRUIT_LIST = new DataFormat("FruitList");
	
	public PerguntaRelacionarComponent (Stage stage, Timer timer, MediaPlayer mediaPlayer, JFXTabPane jfxTabPanePerguntas, long id, String descricao, String fonte, List<String> textoAlternativas, List<Integer> alternativas, List<Integer> alternativasOrdemCorreta, List<String> textoRespostas, Label lblTituloSecundario, Label status, Label lblPontuacao, ProgressBar bar, FlowPane flowPaneVidas) {
		textAreaDescricao = new TextArea(descricao);
		textAreaDescricao.setEditable(false);
		textAreaDescricao.setWrapText(true);
		textAreaDescricao.setPadding(new Insets(10, 0, 0, 0));
		textAreaDescricao.getStyleClass().add("descricaoPergunta");
		
		btnValidar.setDisableVisualFocus(true);
		btnValidar.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-background-color: rgba(49, 170, 188, 1);");
		btnValidar.setPrefWidth(250);
		
//		lblFonte = new Label(fonte);
		
//		HBox hboxFonte = new HBox();
//		hboxFonte.getChildren().addAll(lblFonte);
		
		Label sourceListLbl = new Label("Lista de Alternativas: ");
//		Label targetListLbl = new Label("Definições: ");
//		Label messageLbl = new Label("Select one or more fruits from a list, drag and drop them to another list");
		
		// Set the Size of the Views and the LoggingArea
		sourceView.setPrefSize(150, 50);
		targetView1.setPrefSize(50, 50);
		targetView2.setPrefSize(50, 50);
		targetView3.setPrefSize(50, 50);
		targetView4.setPrefSize(50, 50);
		targetView1.setMinSize(50, 50);
		targetView2.setMinSize(50, 50);
		targetView3.setMinSize(50, 50);
		targetView4.setMinSize(50, 50);
		targetView1.setMaxSize(50, 50);
		targetView2.setMaxSize(50, 50);
		targetView3.setMaxSize(50, 50);
		targetView4.setMaxSize(50, 50);
		
		sourceView.setStyle("-fx-font-size: 15px");
		sourceView.setOrientation(Orientation.HORIZONTAL);
		
		// Add the fruits to the Source List
		sourceView.getItems().addAll(alternativas);
		
		// Allow multiple-selection in lists
		sourceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		targetView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		targetView2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		targetView3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		targetView4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		sourceView.getStyleClass().add("customListView");
		targetView1.getStyleClass().add("roundListView");
		targetView2.getStyleClass().add("roundListView");
		targetView3.getStyleClass().add("roundListView");
		targetView4.getStyleClass().add("roundListView");
		
		List<Integer> listAlternativas = alternativas;

		FlowPane flowPaneQuestoes = new FlowPane();
		
		VBox vboxLabels = new VBox();
		vboxLabels.setPadding(new Insets(20, 0, 0, 0));
		vboxLabels.setStyle("-fx-font-size: 15px");
		VBox vboxSource = new VBox();
		HBox hboxLabelsESource = new HBox();
		
		Label lblTextoAlternativa1 = new Label(textoAlternativas.get(0));
		Label lblTextoAlternativa2 = new Label(textoAlternativas.get(1));
		Label lblTextoAlternativa3 = new Label(textoAlternativas.get(2));
		Label lblTextoAlternativa4 = new Label(textoAlternativas.get(3));
		
		vboxLabels.getChildren().addAll(lblTextoAlternativa1, lblTextoAlternativa2, lblTextoAlternativa3, lblTextoAlternativa4);
		vboxSource.getChildren().addAll(sourceListLbl, sourceView);
		vboxSource.setAlignment(Pos.CENTER);
		
		hboxLabelsESource.getChildren().addAll(vboxLabels, vboxSource);
		
		VBox vboxAlternativas = new VBox();
		
		HBox hboxAlt1 = new HBox();
		HBox hboxAlt2 = new HBox();
		HBox hboxAlt3 = new HBox();
		HBox hboxAlt4 = new HBox();
		
		Label lblResposta1 = new Label(textoRespostas.get(0));
		Label lblResposta2 = new Label(textoRespostas.get(1));
		Label lblResposta3 = new Label(textoRespostas.get(2));
		Label lblResposta4 = new Label(textoRespostas.get(3));
		
		TextArea textAreaResposta1 = new TextArea(textoRespostas.get(0));
		textAreaResposta1.setEditable(false);
		textAreaResposta1.setWrapText(true);
		textAreaResposta1.getStyleClass().add("descricaoPergunta");
		textAreaResposta1.setStyle("-fx-font-size: 15px");
		
		TextArea textAreaResposta2 = new TextArea(textoRespostas.get(1));
		textAreaResposta2.setEditable(false);
		textAreaResposta2.setWrapText(true);
		textAreaResposta2.getStyleClass().add("descricaoPergunta");
		textAreaResposta2.setStyle("-fx-font-size: 15px");
		
		TextArea textAreaResposta3 = new TextArea(textoRespostas.get(2));
		textAreaResposta3.setEditable(false);
		textAreaResposta3.setWrapText(true);
		textAreaResposta3.getStyleClass().add("descricaoPergunta");
		textAreaResposta3.setStyle("-fx-font-size: 15px");
		
		TextArea textAreaResposta4 = new TextArea(textoRespostas.get(3));
		textAreaResposta4.setEditable(false);
		textAreaResposta4.setWrapText(true);
		textAreaResposta4.getStyleClass().add("descricaoPergunta");
		textAreaResposta4.setStyle("-fx-font-size: 15px");
		
		hboxAlt1.setPrefSize(550, 60);
		hboxAlt2.setPrefSize(550, 60);
		hboxAlt3.setPrefSize(550, 60);
		hboxAlt4.setPrefSize(550, 60);
		
		hboxAlt1.getChildren().addAll(targetView1, textAreaResposta1);
		hboxAlt2.getChildren().addAll(targetView2, textAreaResposta2);
		hboxAlt3.getChildren().addAll(targetView3, textAreaResposta3);
		hboxAlt4.getChildren().addAll(targetView4, textAreaResposta4);
		
		// vboxAlternativas.getChildren().addAll(targetListLbl, hboxAlt1, hboxAlt2, hboxAlt3, hboxAlt4);
		
		// flowPaneQuestoes.getChildren().addAll(hboxLabelsESource, vboxAlternativas);
		flowPaneQuestoes.getChildren().addAll(
				hboxLabelsESource, 
				vboxAlternativas,
				hboxAlt1,
				hboxAlt2,
				hboxAlt3,
				hboxAlt4
		);
		
		btnValidar.setOnAction(e -> {
			if (!targetView1.getItems().isEmpty() && !targetView2.getItems().isEmpty() &&
				!targetView3.getItems().isEmpty() && !targetView4.getItems().isEmpty()) {
				
				boolean errou = false;
				
				if (targetView1.getItems().get(0).equals(alternativasOrdemCorreta.get(0))) {
					System.out.println("Primeiro certo");
				} else {
					errou = true;
					System.out.println("Primeiro errado");
					hboxAlt1.setStyle("-fx-border-width: 2px; -fx-border-color: red");
				}
				
				if (targetView2.getItems().get(0).equals(alternativasOrdemCorreta.get(1))) {
					System.out.println("Segundo certo");
				} else {
					errou = true;
					System.out.println("Segundo errado");
					hboxAlt2.setStyle("-fx-border-width: 2px; -fx-border-color: red");
				}
				
				if (targetView3.getItems().get(0).equals(alternativasOrdemCorreta.get(2))) {
					System.out.println("Terceiro certo");
				} else {
					errou = true;
					System.out.println("Terceiro errado");
					hboxAlt3.setStyle("-fx-border-width: 2px; -fx-border-color: red");
				}
				
				if (targetView4.getItems().get(0).equals(alternativasOrdemCorreta.get(3))) {
					System.out.println("Quarto certo");
				} else {
					errou = true;
					System.out.println("Quarto errado");
					hboxAlt4.setStyle("-fx-border-width: 2px; -fx-border-color: red");
				}
				
				StackPane stack = Main.getStack();
				if (!errou) {
					AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/AlternativaCorreta.wav").toExternalForm());
					audioClip.play();
						
					InfoPartida.alunoPontuacao += 400;
						
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
					        
//						JFXButton btnJogarNovamente = new JFXButton("Jogar Novamente");
//						btnJogarNovamente.setButtonType(JFXButton.ButtonType.RAISED);
//						btnJogarNovamente.setStyle("-fx-background-color: #1ab3ce;");
//						btnJogarNovamente.setTextFill(Color.WHITE);
							
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
								"relacionar", // O tipo da pergunta (isso que vai diferenciar quais perguntas foram respondidas)
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
								"relacionar", // O tipo da pergunta (isso que vai diferenciar quais perguntas foram respondidas)
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
				    fadeOut.setNode(btnValidar);
				    fadeOut.setToValue(0.0);
				    fadeOut.playFromStart();
				    
				    btnValidar.setDisable(true);
				    
				    if (!flowPaneVidas.getChildren().isEmpty()) {
				        final FadeTransition fadeOutVida = new FadeTransition(Duration.millis(500));
				        fadeOutVida.setNode(flowPaneVidas.getChildren().get(flowPaneVidas.getChildren().size()-1));
					    fadeOutVida.setToValue(0.0);
					    fadeOutVida.playFromStart();
					        
					    fadeOutVida.onFinishedProperty().set(ev -> {
					    	int indice = flowPaneVidas.getChildren().size() - 1;
					        	
					        flowPaneVidas.getChildren().remove(indice);
					        	
							if (flowPaneVidas.getChildren().isEmpty() || InfoPartida.questaoAtual == InfoPartida.numQuestoes) {
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
							        
//								JFXButton btnJogarNovamente = new JFXButton("Jogar Novamente");
//								btnJogarNovamente.setButtonType(JFXButton.ButtonType.RAISED);
//								btnJogarNovamente.setStyle("-fx-background-color: #1ab3ce;");
//								btnJogarNovamente.setTextFill(Color.WHITE);
									
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
							} else {
								InfoPartida.questaoAtual++;
								
								EntityManagerFactory emfEstatisticas = Persistence.createEntityManagerFactory("objectdb/db/Estatisticas.odb");
								EntityManager emCriaPergunta = emfEstatisticas.createEntityManager();
									
								PerguntaDAO perguntaDAO = new PerguntaDAO();
								perguntaDAO.armazenaDados(emCriaPergunta, new Pergunta(
										UsuarioAtual.getUsuarioAtualAluno().getId(), // O id do usuario atual
										InfoPartida.partidaId,
										id, // O id da pergunta sendo analisada
										"relacionar", // O tipo da pergunta (isso que vai diferenciar quais perguntas foram respondidas)
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
					    });
					}
				}
			}
		});
		
		// Add mouse event handlers for the source
		sourceView.setOnDragDetected(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				writelog("Event on Source: drag detected");
				dragDetectedSource(event, sourceView);
			}
		});
		
		sourceView.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Source: drag over");
				dragOverSource(event, sourceView);
			}
		});
		
		sourceView.setOnDragDropped(new EventHandler <DragEvent>(){
			public void handle(DragEvent event) {
				writelog("Event on Source: drag dropped");
				dragDroppedSource(event, sourceView);
			}
		});
		
		sourceView.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event){
				writelog("Event on Source: drag done");
				dragDoneSource(event, sourceView);
			}
		});
		
		// Add mouse event handlers for the target
		targetView1.setOnDragDetected(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				writelog("Event on Target: drag detected");
				dragDetected(event, targetView1);
			}
		});
		
		targetView1.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag over");
				dragOver(event, targetView1);
			}
		});
		
		targetView1.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag dropped");
				dragDropped(event, targetView1);
			}
		});
		
		targetView1.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag done");
				dragDone(event, targetView1);
			}
		});
		
		targetView2.setOnDragDetected(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				writelog("Event on Target: drag detected");
				dragDetected(event, targetView2);
			}
		});
		
		targetView2.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag over");
				dragOver(event, targetView2);
			}
		});
		
		targetView2.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag dropped");
				dragDropped(event, targetView2);
			}
		});
		
		targetView2.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag done");
				dragDone(event, targetView2);
			}
		});
		
		targetView3.setOnDragDetected(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				writelog("Event on Target: drag detected");
				dragDetected(event, targetView3);
			}
		});
		
		targetView3.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag over");
				dragOver(event, targetView3);
			}
		});
		
		targetView3.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag dropped");
				dragDropped(event, targetView3);
			}
		});
		
		targetView3.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag done");
				dragDone(event, targetView3);
			}
		});
		
		targetView4.setOnDragDetected(new EventHandler <MouseEvent>() {
			public void handle(MouseEvent event) {
				writelog("Event on Target: drag detected");
				dragDetected(event, targetView4);
			}
		});
		
		targetView4.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag over");
				dragOver(event, targetView4);
			}
		});
		
		targetView4.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag dropped");
				dragDropped(event, targetView4);
			}
		});
		
		targetView4.setOnDragDone(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				writelog("Event on Target: drag done");
				dragDone(event, targetView4);
			}
		});

		// root.getChildren().addAll(borderPaneQuestoes, loggingArea, btnValidar);
		
//		this.getChildren().addAll(hboxFonte, flowPaneQuestoes, btnValidar);
		this.getChildren().addAll(flowPaneQuestoes, btnValidar);
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			// System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
			// vboxMenuLateralEsquerdo.setPadding(new Insets(0,  (15.56 / 100) * stage.getWidth(), 0, 0));
			// vboxMenuLateralDireito.setPadding(new Insets(0,  (18.00 / 100) * stage.getWidth(), 0, 0));

			// FlowPane.setMargin(vbox2Alternativas, new Insets(0, 0, 0, stage.getWidth() - 900));
			textAreaDescricao.setPrefHeight((35.0 / 100) * stage.getHeight());
		};
		
	    stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
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
	
// ----------------------------------------------------------------------------------------------------
// Inicio dos metodos apenas para source
// ----------------------------------------------------------------------------------------------------
	
	private void dragDetectedSource(MouseEvent event, ListView<Integer> listView) {
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();
		
		if (selectedCount == 0) {
			event.consume();
			return;
		}
		
		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
		
		// Put the the selected items to the dragboard
		ArrayList<Integer> selectedItems = this.getSelectedFruits(listView);
		
		ClipboardContent content = new ClipboardContent();
		content.put(FRUIT_LIST, selectedItems);
		
		dragboard.setContent(content);
		event.consume();
    }
	
	private void dragOverSource(DragEvent event, ListView<Integer> listView) {
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();
		
		if (event.getGestureSource() != listView && dragboard.hasContent(FRUIT_LIST)) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		
		event.consume();
	}
	
	@SuppressWarnings("unchecked")
	private void dragDroppedSource(DragEvent event, ListView<Integer> listView) {
		boolean dragCompleted = false;
		
		// Transfer the data to the target
		Dragboard dragboard = event.getDragboard();
		
		if(dragboard.hasContent(FRUIT_LIST)) {
			ArrayList<Integer> list = (ArrayList<Integer>)dragboard.getContent(FRUIT_LIST);
			listView.getItems().addAll(list);
			
			// Data transfer is successful
			dragCompleted = true;
		}
		
		// Data transfer is not successful
		event.setDropCompleted(dragCompleted);
		event.consume();
	}
	
	private void dragDoneSource(DragEvent event, ListView<Integer> listView) {
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();
		
		if (tm == TransferMode.MOVE) {
			removeSelectedFruits(listView);
		}
		
		event.consume();
	}
	
// ----------------------------------------------------------------------------------------------------
// Fim dos metodos apenas para source
// ----------------------------------------------------------------------------------------------------
	
	private void dragDetected(MouseEvent event, ListView<Integer> listView) {
		// Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();
		
		if (selectedCount == 0) {
			event.consume();
			return;
		}
		
		// Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
		
		// Put the the selected items to the dragboard
		ArrayList<Integer> selectedItems = this.getSelectedFruits(listView);
		
		ClipboardContent content = new ClipboardContent();
		content.put(FRUIT_LIST, selectedItems);
		
		dragboard.setContent(content);
		event.consume();
    }
	
	private void dragOver(DragEvent event, ListView<Integer> listView) {
		// If drag board has an ITEM_LIST and it is not being dragged
		// over itself, we accept the MOVE transfer mode
		Dragboard dragboard = event.getDragboard();
		
		if (event.getGestureSource() != listView && dragboard.hasContent(FRUIT_LIST)) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		
		event.consume();
	}
	
	@SuppressWarnings("unchecked")
	private void dragDropped(DragEvent event, ListView<Integer> listView) {
		boolean dragCompleted = false;
		
		// Isso previne que o listview receba mais de um valor
		if (listView.getItems().size() <= 0) {
			// Transfer the data to the target
			Dragboard dragboard = event.getDragboard();
			
			if(dragboard.hasContent(FRUIT_LIST)) {
				ArrayList<Integer> list = (ArrayList<Integer>)dragboard.getContent(FRUIT_LIST);
				listView.getItems().addAll(list);
				
				// Data transfer is successful
				dragCompleted = true;
			}
			
			// Data transfer is not successful
			event.setDropCompleted(dragCompleted);
			
		}
		
		event.consume();
	}
	
	private void dragDone(DragEvent event, ListView<Integer> listView) {
		// Check how data was transfered to the target
		// If it was moved, clear the selected items
		TransferMode tm = event.getTransferMode();
		
		if (tm == TransferMode.MOVE) {
			removeSelectedFruits(listView);
		}
		
		event.consume();
	}
	
	private ArrayList<Integer> getSelectedFruits(ListView<Integer> listView) {
		// Return the list of selected Fruit in an ArratyList, so it is
		// serializable and can be stored in a Dragboard.
		ArrayList<Integer> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
		
		return list;
	}
	
	private void removeSelectedFruits(ListView<Integer> listView) {
		// Get all selected Fruits in a separate list to avoid the shared list issue
		List<Integer> selectedList = new ArrayList<>();
		
		for(Integer fruit : listView.getSelectionModel().getSelectedItems()) {
			selectedList.add(fruit);
		}
		
		// Clear the selection
		listView.getSelectionModel().clearSelection();
		
		// Remove items from the selected list
		listView.getItems().removeAll(selectedList);
	}
	
	private void writelog(String text) {
		this.loggingArea.appendText(text + "\n");
	}
}
