package component.menu.aluno;

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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToolbar;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.partida.ContainerOpcaoLateralComponent;
import component.partida.PartidaTelaComponent;
import dao.AlunoDAO;
import dao.AlunoPossuiItemDAO;
import dao.AlunoPossuiMusicaDAO;
import dao.ItemDAO;
import dao.MusicaDAO;
import dao.PerguntaMultiplaEscolhaDAO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.AlunoPossuiItem;
import model.AlunoPossuiMusica;
import model.Item;
import model.Musica;
import model.PerguntaMultiplaEscolha;
import util.InfoLoja;
import util.InfoPartida;
import util.UsuarioAtual;

public class MenuAlunoComponent extends BorderPane {
	private Label lblBitcoins = new Label(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins));
	
	public MenuAlunoComponent (Stage stage, MediaPlayer mediaPlayer) {
		System.out.println("Ta printando aqui: " + Main.getStack().getChildren());

		// Toolbar que aparece em cima
		JFXToolbar toolbar = new JFXToolbar();
		toolbar.setPadding(new Insets(10, 0, 10, 10));
		toolbar.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: solid none solid none;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: #dde3e7;"
		);
		
		Label lblMenu = new Label("Menu Principal");
		
		System.out.println("Viem ate aqui");
		
		inicializaToolbar(toolbar, lblMenu, lblBitcoins);
		
		System.out.println("Passei daqui");
		
		JFXTabPane tabpaneConteudo = new JFXTabPane();
		tabpaneConteudo.setPrefSize(stage.getWidth(), stage.getHeight());
		tabpaneConteudo.setStyle("-fx-background-color: white;");
		tabpaneConteudo.setDisableAnimation(true);
		tabpaneConteudo.getStyleClass().add("hiddenTabPane"); // Este comando aplica a class hiddenTabPane disponivel no css para retirar o header que contem as tabs no TabPane
		
		Tab tabMenu = new Tab("Menu Principal");
		Tab tabConteudo = new Tab("Conteudo");
		Tab tabIniciarPartida = new Tab("Iniciar Partida");
		
		Tab tabMenuLojaHall = new Tab("Menu Loja Hall");
		Tab tabMenuLojaItens = new Tab("Menu Loja Itens");
		Tab tabMenuLojaMusicas = new Tab("Menu Loja Musicas");
		Tab tabMenuLojaTemas = new Tab("Menu Loja Temas");
		
		Tab tabConfiguracoes = new Tab("Configurações");
		Tab tabEstatisticas = new Tab("Estatísticas");
		Tab tabSobre = new Tab("Sobre");
		
		int numTabMenu = 0;
		int numTabConteudo = 1;
		int numTabIniciarPartida = 2;
		int numTabMenuLojaHall = 3;
		int numTabMenuLojaItens = 4;
		int numTabMenuLojaMusicas = 5;
		int numTabMenuLojaTemas = 6;
		int numTabConfiguracoes = 7;
		int numTabEstatisticas = 8;
		int numTabSobre = 9;
		
		criaTabMenu(tabpaneConteudo, tabMenu, numTabConteudo, numTabMenuLojaHall, numTabConfiguracoes, numTabEstatisticas, numTabSobre);
		criaTabConteudo(tabpaneConteudo, tabConteudo, numTabMenu, numTabIniciarPartida);
		criaTabIniciarPartida(tabpaneConteudo, tabIniciarPartida, numTabConteudo, stage, mediaPlayer);
		
		criaTabMenuLojaHall(tabpaneConteudo, tabMenuLojaHall, numTabMenu, numTabMenuLojaItens, numTabMenuLojaMusicas, numTabMenuLojaTemas);
		criaTabMenuLojaItens(tabpaneConteudo, tabMenuLojaItens, numTabMenuLojaHall, lblBitcoins);
		criaTabMenuLojaMusicas(tabpaneConteudo, tabMenuLojaMusicas, numTabMenuLojaHall, lblBitcoins);
		criaTabMenuLojaTemas(tabpaneConteudo, tabMenuLojaTemas, numTabMenuLojaHall);
		
		criaTabConfiguracoes(tabpaneConteudo, tabConfiguracoes, numTabMenu, mediaPlayer);
		criaTabEstatisticas(tabpaneConteudo, tabEstatisticas, numTabMenu);
		criaTabSobre(tabpaneConteudo, tabSobre, numTabMenu);
		
		tabpaneConteudo.getTabs().addAll(
				tabMenu, 
				tabConteudo,
				tabIniciarPartida, 
				
				tabMenuLojaHall,
				tabMenuLojaItens,
				tabMenuLojaMusicas,
				tabMenuLojaTemas,
				
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
	
	private void criaTabMenuLojaHall(JFXTabPane tabpaneConteudo, Tab tabMenuLojaHall, int numTabMenu,
			int numTabMenuLojaItens, int numTabMenuLojaMusicas, int numTabMenuLojaTemas) {
		VBox vboxOpcoesMenuLojaHall = new VBox();
		vboxOpcoesMenuLojaHall.setPadding(new Insets(20, 20, 20, 20));
		
		JFXButton btnLojaItens = new JFXButton("Itens");
		JFXButton btnLojaMusicas = new JFXButton("Musicas");
		// JFXButton btnLojaTemas = new JFXButton("Temas");
		JFXButton btnVoltar = new JFXButton("Voltar");
		
		btnLojaItens.setStyle("-fx-font-size: 20px;");
		btnLojaMusicas.setStyle("-fx-font-size: 20px;");
		// btnLojaTemas.setStyle("-fx-font-size: 20px;");
		btnVoltar.setStyle("-fx-font-size: 20px;");
		
		// vboxOpcoesMenuLojaHall.getChildren().addAll(btnLojaItens, btnLojaMusicas, btnLojaTemas, btnVoltar);
		vboxOpcoesMenuLojaHall.getChildren().addAll(btnLojaItens, btnLojaMusicas, btnVoltar);
		
		tabMenuLojaHall.setContent(vboxOpcoesMenuLojaHall);
		
		btnLojaItens.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaItens);
		});
		
		btnLojaMusicas.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaMusicas);
		});
		
		/*
		btnLojaTemas.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaTemas);
		});
		*/
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
	}
	
	private void criaTabMenuLojaItens(JFXTabPane tabpaneConteudo, Tab tabMenuLojaItens, int numTabMenuLojaItens, Label lblBitcoins) {
		ItemDAO itemDAO = new ItemDAO();
		EntityManagerFactory emfItem = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseItens = emfItem.createEntityManager();
		
		ObservableList<Item> listItens = FXCollections.observableArrayList(itemDAO.getListItens(emExisteDatabaseItens));
		
		FlowPane flowPaneLojaItens = new FlowPane();
		flowPaneLojaItens.setPadding(new Insets(5, 5, 5, 5));
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		btnVoltar.setStyle("-fx-font-size: 20px;");
		
		HBox hboxInfoItens = new HBox();
		
		VBox vboxItens = new VBox();
		vboxItens.getChildren().addAll(flowPaneLojaItens, hboxInfoItens, btnVoltar);
		
		tabMenuLojaItens.setContent(vboxItens);
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaItens);
		});
		
		if (!listItens.isEmpty()){
			for (Item i : listItens) {
				VBox vboxItem = new VBox();
				vboxItem.setAlignment(Pos.CENTER);
				vboxItem.setPadding(new Insets(5, 5, 5, 5));
				vboxItem.setPrefSize(200, 270);
				vboxItem.getStyleClass().add("lojaItem");
				
				Label lblNome = new Label(i.getNome());
				lblNome.setStyle("-fx-font-size: 20px;");
				
				TextArea textAreaDescricao = new TextArea(i.getDescricao());
				textAreaDescricao.setEditable(false);
				textAreaDescricao.setWrapText(true);
				textAreaDescricao.setPadding(new Insets(10, 0, 0, 0));
				textAreaDescricao.getStyleClass().add("descricaoPergunta");
				textAreaDescricao.setStyle("-fx-font-size: 15px");
				
				ImageView img = new ImageView("/resources/images/" + i.getImgNome() + ".png");
				img.setFitWidth(100);
				img.setPreserveRatio(true);
				
				HBox hboxPreco = new HBox();
				hboxPreco.setAlignment(Pos.CENTER);
				
				Button btnBitcoins = new Button();
				btnBitcoins.getStyleClass().add("bitcoinIcon");
				btnBitcoins.setAlignment(Pos.CENTER);
				
				Label lblPreco = new Label(String.valueOf(i.getPreco()));
				lblPreco.setStyle("-fx-font-size: 17px;");
				
				hboxPreco.getChildren().addAll(btnBitcoins, lblPreco);
				
				JFXButton btnComprar = new JFXButton("Comprar");
				btnComprar.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: rgba(49, 170, 188, 1);");
				
				vboxItem.getChildren().addAll(lblNome, img, textAreaDescricao, hboxPreco, btnComprar);
				flowPaneLojaItens.getChildren().add(vboxItem);
				flowPaneLojaItens.setMargin(vboxItem, new Insets(10, 10, 10, 10));
				
				// TODO (Acho que tem que dar refresh na label). 
				// Para desabilitar a compra do item caso nao tenha dinheiro suficiente
				if (UsuarioAtual.usuarioAtualAluno.getBitcoins() < i.getPreco()) {
					btnComprar.setDisable(true);
				}
				
				
				
				
				
				AlunoPossuiItemDAO alunoPossuiItemDAO = new AlunoPossuiItemDAO();
				EntityManagerFactory emfAlunoPossuiItem = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
				EntityManager emAlunoPossuiItes = emfAlunoPossuiItem.createEntityManager();
				
				ObservableList<AlunoPossuiItem> listAlunoPossuiItens = FXCollections.observableArrayList(alunoPossuiItemDAO.getListAlunoPossuiItens(emAlunoPossuiItes));

				boolean alunoPossuiItem = false;
				
				AlunoPossuiItem alunoPossuiItemObj = null;
				
				String qtd = "";
				
				if (!listAlunoPossuiItens.isEmpty()){
					for (AlunoPossuiItem api : listAlunoPossuiItens) {
						if (UsuarioAtual.getUsuarioAtualAluno().getId() == api.getAlunoId()) {
							if (api.getItemId() == i.getId()) {
								alunoPossuiItem = true;
									
								alunoPossuiItemObj = api;
								
								qtd = String.valueOf(alunoPossuiItemObj.getQtd());
							}
						}
					}
				}
				
				ImageView imgIcone = new ImageView("/resources/images/" + i.getImgNome() + ".png");
				imgIcone.setFitWidth(30);
				imgIcone.setPreserveRatio(true);
				
				Label lblQtdItem = new Label(qtd);
				
				hboxInfoItens.getChildren().addAll(imgIcone, lblQtdItem);
				hboxInfoItens.setPadding(new Insets(5, 5, 5, 5));
				
				btnComprar.setOnAction(ev -> {
					EntityManagerFactory emfAlunoPossuiItem2 = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
					EntityManager emAlunoPossuiItes2 = emfAlunoPossuiItem2.createEntityManager();
					
					ObservableList<AlunoPossuiItem> listAlunoPossuiItens2 = FXCollections.observableArrayList(alunoPossuiItemDAO.getListAlunoPossuiItens(emAlunoPossuiItes2));
					
					// Declarar novamente porque nao aceita a variavel externa ao evento de clique
					AlunoPossuiItem localAlunoPossuiItemObj = null;
					boolean localAlunoPossuiItem = false;
					String qtdLocal = "0";
					
					if (!listAlunoPossuiItens2.isEmpty()){
						for (AlunoPossuiItem api : listAlunoPossuiItens2) {
							if (UsuarioAtual.getUsuarioAtualAluno().getId() == api.getAlunoId()) {
								if (api.getItemId() == i.getId()) {
									localAlunoPossuiItem = true;
									
									localAlunoPossuiItemObj = api;
									
									qtdLocal = String.valueOf(localAlunoPossuiItemObj.getQtd());
								}
							}
						}
					}
					
					int qtdItem = 1;
					
					lblQtdItem.setText(String.valueOf(Integer.parseInt(qtdLocal) + qtdItem));
					
					EntityManagerFactory emfAluno = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
					EntityManager emAluno = emfAluno.createEntityManager();
					
					InfoLoja.alunoBitcoins -= i.getPreco();
					
					lblBitcoins.setText(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins));
					
					// Atualiza no banco o desconto de bitcoins
					AlunoDAO alunoDAO = new AlunoDAO();
					alunoDAO.atualizaDados(emAluno, UsuarioAtual.getUsuarioAtualAluno(), new Aluno(
							UsuarioAtual.getUsuarioAtualAluno().getPontuacao(),
							UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins
					));
					
					if (localAlunoPossuiItem) {
						EntityManager emAlunoPossuiItemRelacao = emfAlunoPossuiItem.createEntityManager();

						AlunoPossuiItem alunoPossuiItemNewObj = new AlunoPossuiItem(UsuarioAtual.usuarioAtualAluno.getId(), i.getId(), qtdItem);
						
						// Atualiza a quantidade do item comprado
						alunoPossuiItemDAO.atualizaDados(emAlunoPossuiItemRelacao, localAlunoPossuiItemObj, alunoPossuiItemNewObj);
					} else {
						EntityManager emAlunoPossuiItemRelacao = emfAlunoPossuiItem.createEntityManager();
						
						AlunoPossuiItem alunoPossuiItemNewObj = new AlunoPossuiItem(UsuarioAtual.usuarioAtualAluno.getId(), i.getId(), qtdItem);
						
						alunoPossuiItemDAO.armazenaDados(emAlunoPossuiItemRelacao, alunoPossuiItemNewObj);
					}
					
					int valor = UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins;
					
					// TODO Remover construcao perigosa, muito suscetivel a dar null pointers aqui
					FlowPane aux = (FlowPane) vboxItens.getChildren().get(0);
					for (int j = 0; j < aux.getChildren().size(); j++) {
						VBox aux2 = (VBox) aux.getChildren().get(j);
						
						JFXButton btnAux = (JFXButton) aux2.getChildren().get(4);
						
						HBox aux3 = (HBox) aux2.getChildren().get(3);
						Label lblAux = (Label) aux3.getChildren().get(1);
						
						int preco = Integer.valueOf(lblAux.getText());
						
						if (UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins < preco) {
							btnAux.setDisable(true);
						}
					}
				});
			}
		} else {
			
		}
	}
	
	private void criaTabMenuLojaMusicas(JFXTabPane tabpaneConteudo, Tab tabMenuLojaMusicas, int numTabMenuLojaHall, Label lblBitcoins) {
		MusicaDAO musicaDAO = new MusicaDAO();
		EntityManagerFactory emfMusica = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseMusicas = emfMusica.createEntityManager();
		
		ObservableList<Musica> listMusicas = FXCollections.observableArrayList(musicaDAO.getListMusicas(emExisteDatabaseMusicas));
		
		FlowPane flowPaneLojaMusicas = new FlowPane();
		flowPaneLojaMusicas.setPadding(new Insets(5, 5, 5, 5));
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		btnVoltar.setStyle("-fx-font-size: 20px;");
		
		HBox hboxInfoMusicas = new HBox();
		
		VBox vboxMusicas = new VBox();
		vboxMusicas.getChildren().addAll(flowPaneLojaMusicas, hboxInfoMusicas, btnVoltar);
		
		tabMenuLojaMusicas.setContent(vboxMusicas);
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaHall);
		});
		
		if (!listMusicas.isEmpty()){
			for (Musica m : listMusicas) {
				VBox vboxMusica = new VBox();
				vboxMusica.setAlignment(Pos.CENTER);
				vboxMusica.setPadding(new Insets(5, 5, 5, 5));
				vboxMusica.setPrefSize(150, 200);
				vboxMusica.getStyleClass().add("lojaItem");
				
				Label lblNome = new Label(m.getNome());
				lblNome.setStyle("-fx-font-size: 20px;");
				
				ImageView img = new ImageView("/resources/images/" + m.getImgNome() + ".png");
				img.setFitWidth(100);
				img.setPreserveRatio(true);
				
				HBox hboxPreco = new HBox();
				hboxPreco.setAlignment(Pos.CENTER);
				
				Button btnBitcoins = new Button();
				btnBitcoins.getStyleClass().add("bitcoinIcon");
				btnBitcoins.setAlignment(Pos.CENTER);
				
				Label lblPreco = new Label(String.valueOf(m.getPreco()));
				lblPreco.setStyle("-fx-font-size: 17px;");
				
				hboxPreco.getChildren().addAll(btnBitcoins, lblPreco);
				
				JFXButton btnComprar = new JFXButton("Comprar");
				btnComprar.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-background-color: rgba(49, 170, 188, 1);");
				
				vboxMusica.getChildren().addAll(lblNome, img, hboxPreco, btnComprar);
				flowPaneLojaMusicas.getChildren().add(vboxMusica);
				flowPaneLojaMusicas.setMargin(vboxMusica, new Insets(10, 10, 10, 10));
				
				// TODO (Acho que tem que dar refresh na label). 
				// Para desabilitar a compra do item caso nao tenha dinheiro suficiente
				if (UsuarioAtual.usuarioAtualAluno.getBitcoins() < m.getPreco()) {
					btnComprar.setDisable(true);
				}
				
				
				
				
				AlunoPossuiMusicaDAO alunoPossuiMusicaDAO = new AlunoPossuiMusicaDAO();
				EntityManagerFactory emfAlunoPossuiMusica = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
				EntityManager emAlunoPossuiMusicas = emfAlunoPossuiMusica.createEntityManager();
				
				ObservableList<AlunoPossuiMusica> listAlunoPossuiMusicas = FXCollections.observableArrayList(alunoPossuiMusicaDAO.getListAlunoPossuiMusicas(emAlunoPossuiMusicas));

				boolean alunoPossuiMusica = false;
				
				AlunoPossuiMusica alunoPossuiMusicaObj = null;
				
				if (!listAlunoPossuiMusicas.isEmpty()){
					for (AlunoPossuiMusica apm : listAlunoPossuiMusicas) {
						if (UsuarioAtual.getUsuarioAtualAluno().getId() == apm.getAlunoId()) {
							if (apm.getMusicaId() == m.getId()) {
								alunoPossuiMusica = true;
									
								alunoPossuiMusicaObj = apm;
							}
						}
					}
				}
				
				if (alunoPossuiMusica) {
					vboxMusica.setDisable(true);
				}
				
				btnComprar.setOnAction(ev -> {
					EntityManagerFactory emfAlunoPossuiMusica2 = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
					EntityManager emAlunoPossuiMusicas2 = emfAlunoPossuiMusica2.createEntityManager();
					
					ObservableList<AlunoPossuiMusica> listAlunoPossuiMusicas2 = FXCollections.observableArrayList(alunoPossuiMusicaDAO.getListAlunoPossuiMusicas(emAlunoPossuiMusicas2));
					
					// Declarar novamente porque nao aceita a variavel externa ao evento de clique
					AlunoPossuiMusica localAlunoPossuiMusicaObj = null;
					boolean localAlunoPossuiMusica = false;
					
					if (!listAlunoPossuiMusicas2.isEmpty()){
						for (AlunoPossuiMusica apm : listAlunoPossuiMusicas2) {
							if (UsuarioAtual.getUsuarioAtualAluno().getId() == apm.getAlunoId()) {
								if (apm.getMusicaId() == m.getId()) {
									localAlunoPossuiMusica = true;
									
									localAlunoPossuiMusicaObj = apm;
								}
							}
						}
					}
					
					EntityManagerFactory emfAluno = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
					EntityManager emAluno = emfAluno.createEntityManager();
					
					InfoLoja.alunoBitcoins -= m.getPreco();
					
					lblBitcoins.setText(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins));
					
					// Atualiza no banco o desconto de bitcoins
					AlunoDAO alunoDAO = new AlunoDAO();
					alunoDAO.atualizaDados(emAluno, UsuarioAtual.getUsuarioAtualAluno(), new Aluno(
							UsuarioAtual.getUsuarioAtualAluno().getPontuacao(),
							UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins
					));
					
					if (!localAlunoPossuiMusica) {
						EntityManager emAlunoPossuiMusicaRelacao = emfAlunoPossuiMusica.createEntityManager();
						
						AlunoPossuiMusica alunoPossuiMusicaNewObj = new AlunoPossuiMusica(UsuarioAtual.usuarioAtualAluno.getId(), m.getId());
						
						alunoPossuiMusicaDAO.armazenaDados(emAlunoPossuiMusicaRelacao, alunoPossuiMusicaNewObj);
						
						vboxMusica.setDisable(true);
					}
					
					int valor = UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins;
					
					// TODO Remover construcao perigosa, muito suscetivel a dar null pointers aqui
					FlowPane aux = (FlowPane) vboxMusicas.getChildren().get(0);
					for (int j = 0; j < aux.getChildren().size(); j++) {
						VBox aux2 = (VBox) aux.getChildren().get(j);
						
						JFXButton btnAux = (JFXButton) aux2.getChildren().get(3);
						
						HBox aux3 = (HBox) aux2.getChildren().get(2);
						Label lblAux = (Label) aux3.getChildren().get(1);
						
						int preco = Integer.valueOf(lblAux.getText());
						
						if (UsuarioAtual.getUsuarioAtualAluno().getBitcoins() + InfoLoja.alunoBitcoins < preco) {
							btnAux.setDisable(true);
						}
					}
				});
			}
		} else {
			
		}
	}
	
	private void criaTabMenuLojaTemas(JFXTabPane tabpaneConteudo, Tab tabMenuLojaTemas, int numTabMenuLojaHall) {
		// TODO Auto-generated method stub
		
		JFXButton btnVoltar = new JFXButton("Voltar");
		
		btnVoltar.setStyle("-fx-font-size: 20px;");

		FlowPane flowPaneLojaTemas = new FlowPane();
		flowPaneLojaTemas.setPadding(new Insets(5, 5, 5, 5));
				
		flowPaneLojaTemas.getChildren().addAll(btnVoltar);
		tabMenuLojaTemas.setContent(flowPaneLojaTemas);

		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaHall);
		});
	}
	
	private void inicializaToolbar(JFXToolbar toolbar, Label lblMenu, Label lblBitcoins) {
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
        
        Button btnBitcoins = new Button();
		btnBitcoins.getStyleClass().add("bitcoinIcon");
		
		lblBitcoins.setStyle("-fx-font-size: 15px; -fx-font-family: \"Yu Gothic UI Semilight\";");

        HBox hboxPontuacao = new HBox();
        hboxPontuacao.setAlignment(Pos.CENTER);
        
        Button btnPontuacao = new Button();
		btnPontuacao.getStyleClass().add("pontuacaoIcon");
		
		Label lblPontuacao = new Label(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getPontuacao()));
        lblPontuacao.setStyle("-fx-font-size: 15px; -fx-font-family: \"Yu Gothic UI Semilight\";");
        
        hboxPontuacao.getChildren().addAll(btnPontuacao, lblPontuacao, btnBitcoins, lblBitcoins);
        HBox.setMargin(btnPontuacao, new Insets(0, 7, 0, 0));
        HBox.setMargin(lblPontuacao, new Insets(0, 20, 0, 0));
        HBox.setMargin(btnBitcoins, new Insets(0, 7, 0, 0));
        HBox.setMargin(lblBitcoins, new Insets(0, 20, 0, 0));
        
		// hboxPerfil.getChildren().addAll(hboxPontuacao, nomeUsuario, btnPerfil);
		hboxPerfil.getChildren().addAll(hboxPontuacao, nomeUsuario);
		
		toolbar.setAlignment(lblMenu, Pos.CENTER);
		toolbar.setAlignment(logo, Pos.CENTER);
		toolbar.setAlignment(hboxPerfil, Pos.CENTER);
		hboxPerfil.setAlignment(Pos.CENTER);
		
		toolbar.setLeft(lblMenu);
		toolbar.setCenter(logo);
		toolbar.setRight(hboxPerfil);
		
		nomeUsuario.setText(UsuarioAtual.getUsuarioAtualAluno().getNome());
	}

	private void criaTabMenu(JFXTabPane tabpaneConteudo, Tab tabMenu1, int numTabConteudo, int numTabMenuLojaHall, int numTabConfiguracoes, int numTabEstatisticas, int numTabSobre) {
		VBox vboxOpcoesMenuPrinciapl = new VBox();
		vboxOpcoesMenuPrinciapl.setPadding(new Insets(20, 20, 20, 20));
		
		JFXButton btnJogar = new JFXButton("Jogar");
		JFXButton btnLojaHall = new JFXButton("Loja");
		JFXButton btnConfiguracoes = new JFXButton("Configurações");
		// JFXButton btnEstaticas = new JFXButton("Estatísticas ");
		JFXButton btnSobre = new JFXButton("Sobre");
		JFXButton btnSair = new JFXButton("Sair");
		
		btnJogar.setStyle("-fx-font-size: 20px;");
		btnLojaHall.setStyle("-fx-font-size: 20px;");
		btnConfiguracoes.setStyle("-fx-font-size: 20px;");
		// btnEstaticas.setStyle("-fx-font-size: 20px;");
		btnSobre.setStyle("-fx-font-size: 20px;");
		btnSair.setStyle("-fx-font-size: 20px;");
		
		// vboxOpcoesMenuPrinciapl.getChildren().addAll(btnJogar, btnLojaHall, btnConfiguracoes, btnEstaticas, btnSobre, btnSair);
		vboxOpcoesMenuPrinciapl.getChildren().addAll(btnJogar, btnLojaHall, btnConfiguracoes, btnSobre, btnSair);
		
		tabMenu1.setContent(vboxOpcoesMenuPrinciapl);
		
		btnJogar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabConteudo);
		});
		
		btnLojaHall.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenuLojaHall);
		});
		
		btnConfiguracoes.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabConfiguracoes);
		});
		
		/*
		btnEstaticas.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabEstatisticas);
		});
		*/
		
		btnSobre.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabSobre);
		});
		
		btnSair.setOnAction(ActionEvent -> {
			Platform.exit();
		});
	}
	
	private void criaTabConteudo(JFXTabPane tabpaneConteudo, Tab tabMenu2, int numTabMenu, int numTabIniciarPartida) {
		VBox vboxConteudoJogo = new VBox();
		vboxConteudoJogo.setPadding(new Insets(20, 20, 20, 20));
		
		JFXButton btnCasoDeUso = new JFXButton("Diagramas de Caso de Uso");
		JFXButton btnClasse = new JFXButton("Diagramas de Classe");
		JFXButton btnSequencia = new JFXButton("Diagramas de Sequência");
		JFXButton btnVoltarMenuInicial = new JFXButton("Voltar");
		
		btnCasoDeUso.setStyle("-fx-font-size: 20px;");
		btnClasse.setStyle("-fx-font-size: 20px;");
		btnSequencia.setStyle("-fx-font-size: 20px;");
		btnVoltarMenuInicial.setStyle("-fx-font-size: 20px;");
		
		btnClasse.setDisable(true);
		btnSequencia.setDisable(true);
		
		btnCasoDeUso.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabIniciarPartida);
		});
		
		btnVoltarMenuInicial.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
		
		vboxConteudoJogo.getChildren().addAll(btnCasoDeUso, btnClasse, btnSequencia, btnVoltarMenuInicial);
		
		tabMenu2.setContent(vboxConteudoJogo);
	}
	
	private void criaTabIniciarPartida(JFXTabPane tabpaneConteudo, Tab tabIniciarPartida, int numTabConteudo, Stage stage, MediaPlayer mediaPlayer) {
		VBox vboxOpcoesJogo = new VBox();
		vboxOpcoesJogo.setPadding(new Insets(20, 20, 20, 20));
		
		Label lblOrdemDasPerguntas = new Label("Ordem das perguntas");
		lblOrdemDasPerguntas.setStyle("-fx-font-size: 20px;");
		lblOrdemDasPerguntas.setPadding(new Insets(0, 0, 20, 0));
		
		HBox hboxRadiosOrdem = new HBox();
		
		JFXRadioButton radioSequencial = new JFXRadioButton("Sequencial");
		radioSequencial.setStyle("-fx-font-size: 20px;");
		radioSequencial.setPadding(new Insets(0, 10, 0, 0));
		radioSequencial.setSelected(true);
		JFXRadioButton radioAleatorio = new JFXRadioButton("Aleatório");
		radioAleatorio.setStyle("-fx-font-size: 20px;");
		
		hboxRadiosOrdem.getChildren().addAll(radioSequencial, radioAleatorio);
		hboxRadiosOrdem.setPadding(new Insets(0, 0, 20, 0));
		
		ToggleGroup groupOrdem = new ToggleGroup();
		groupOrdem.getToggles().addAll(radioSequencial, radioAleatorio);
		
		HBox hboxTempo = new HBox();
		
		JFXCheckBox checkBoxTempo = new JFXCheckBox("Tempo");
		checkBoxTempo.setStyle("-fx-font-size: 20px;");
		checkBoxTempo.setPadding(new Insets(0, 10, 0, 0));
		
		JFXSlider sliderTempo = new JFXSlider();
		sliderTempo.setMax(60);
		sliderTempo.setValue(30);
		
		hboxTempo.getChildren().addAll(checkBoxTempo, sliderTempo);
		hboxTempo.setPadding(new Insets(0, 0, 20, 0));
		
		JFXButton btnVoltarMenuConteudo = new JFXButton("Voltar");
		btnVoltarMenuConteudo.setStyle("-fx-font-size: 20px;");

		btnVoltarMenuConteudo.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabConteudo);
		});
		
		JFXButton btnIniciarPartida = new JFXButton("Iniciar Partida");
		btnIniciarPartida.setStyle("-fx-font-size: 20px;");
		
		btnIniciarPartida.setOnAction(ActioEvent -> {
			Main.getStack().getChildren().remove(0);
			
			PartidaTelaComponent partidaComponent = new PartidaTelaComponent(stage, mediaPlayer);
			
			Main.getStack().getChildren().add(partidaComponent);
		});
		
		vboxOpcoesJogo.setMargin(btnIniciarPartida, new Insets(0, 0, 30, 0));
		// vboxOpcoesJogo.getChildren().addAll(lblOrdemDasPerguntas, hboxRadiosOrdem, hboxTempo, btnVoltarMenuConteudo, btnIniciarPartida);
		vboxOpcoesJogo.getChildren().addAll(btnIniciarPartida, btnVoltarMenuConteudo);
		
		tabIniciarPartida.setContent(vboxOpcoesJogo);
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


		HBox hboxMusicasETemas = new HBox();
		
		JFXListView<String> listViewMusicas = new JFXListView<String>();
		
		Label lblMusica = new Label("Escolha a musica tema");
		lblMusica.setStyle("-fx-font-size: 20px;");
		
		VBox vboxMusicas = new VBox();
		vboxMusicas.getChildren().addAll(lblMusica, listViewMusicas);
		
		
		MusicaDAO musicaDAO = new MusicaDAO();
		EntityManagerFactory emfMusica = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseMusicas = emfMusica.createEntityManager();
		
		ObservableList<Musica> listMusicas = FXCollections.observableArrayList(musicaDAO.getListMusicas(emExisteDatabaseMusicas));
		
		listViewMusicas.getItems().add("default");
		
		if (!listMusicas.isEmpty()){
			for (Musica m : listMusicas) {
				AlunoPossuiMusicaDAO alunoPossuiMusicaDAO = new AlunoPossuiMusicaDAO();
				EntityManagerFactory emfAlunoPossuiMusica = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
				EntityManager emAlunoPossuiMusicas = emfAlunoPossuiMusica.createEntityManager();
				
				ObservableList<AlunoPossuiMusica> listAlunoPossuiMusicas = FXCollections.observableArrayList(alunoPossuiMusicaDAO.getListAlunoPossuiMusicas(emAlunoPossuiMusicas));

				boolean alunoPossuiMusica = false;
				
				AlunoPossuiMusica alunoPossuiMusicaObj = null;
				
				if (!listAlunoPossuiMusicas.isEmpty()){
					for (AlunoPossuiMusica apm : listAlunoPossuiMusicas) {
						if (UsuarioAtual.getUsuarioAtualAluno().getId() == apm.getAlunoId()) {
							if (apm.getMusicaId() == m.getId()) {
								alunoPossuiMusica = true;
								
								alunoPossuiMusicaObj = apm;
							}
						}
					}
				}
				
				if (alunoPossuiMusica) {
					listViewMusicas.getItems().add(m.getArquivoNome());
				}
			}
		}
		
		listViewMusicas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				MediaPlayer mp = Main.getMediaPlayer();
				
				System.out.println(mp);
				
				/*
				if (mp != null) {
					mp.stop();
					mp.dispose();
			    }
			    */
				
				Media sound = new Media(getClass().getResource("/music/" + newValue + ".mp3").toExternalForm()); 
				mp = new MediaPlayer(sound);
				mp.setOnEndOfMedia(new Runnable() {
					public void run() {
						// mp.seek(Duration.ZERO);
					}
				});
				
				mp.play();
			}
		});
		
		hboxMusicasETemas.getChildren().add(vboxMusicas);
		
		hboxMusicasETemas.setPadding(new Insets(0, 0, 10, 14));
		hboxVolume.setPadding(new Insets(0, 0, 10, 14));
		
		vboxOpcoesConfiguracoes.getChildren().addAll(hboxMusicasETemas, hboxVolume, btnVoltar);
		tabConfiguracoes.setContent(vboxOpcoesConfiguracoes);
		
		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
	}
	
	private void criaTabEstatisticas(JFXTabPane tabpaneConteudo, Tab tabEstatisticas, int numTabMenu) {
		// TODO Auto-generated method stub
		JFXButton btnVoltar = new JFXButton("Voltar");
		
		btnVoltar.setStyle("-fx-font-size: 20px;");

		VBox vboxOpcoesEstatisticas = new VBox();
		vboxOpcoesEstatisticas.setPadding(new Insets(20, 20, 20, 20));
		
		vboxOpcoesEstatisticas.getChildren().addAll(btnVoltar);
		tabEstatisticas.setContent(vboxOpcoesEstatisticas);

		btnVoltar.setOnAction(ActionEvent -> {
			tabpaneConteudo.getSelectionModel().select(numTabMenu);
		});
	}
	
	private void criaTabSobre(JFXTabPane tabpaneConteudo, Tab tabSobre, int numTabMenu) {
		// TODO Auto-generated method stub
		
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
