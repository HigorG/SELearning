package component.partida;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;

import component.timer.Timer;
import dao.AlunoPossuiItemDAO;
import dao.ItemDAO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AlunoPossuiItem;
import model.Item;
import util.UsuarioAtual;

public class AddonsPartidaComponent extends VBox {
	public AddonsPartidaComponent (Stage stage, Timer timer, FlowPane flowPaneVidas) {
		this.setStyle("-fx-background-color: #F2F5F7;"
				+ "-fx-border-style: solid hidden hidden hidden;" // A borda que fica em cima do painel de tempo
				+ "-fx-border-width: 2;"
				+ "-fx-border-color: #dde3e7;"
		);
		this.setPadding(new Insets(0, 10, 0, 0));
		
		
		
		HBox hboxInfoItens = new HBox();
		
		ItemDAO itemDAO = new ItemDAO();
		EntityManagerFactory emfItem = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
		EntityManager emExisteDatabaseItens = emfItem.createEntityManager();
		
		ObservableList<Item> listItens = FXCollections.observableArrayList(itemDAO.getListItens(emExisteDatabaseItens));
		
		if (!listItens.isEmpty()) {
			for (Item i : listItens) {
				AlunoPossuiItemDAO alunoPossuiItemDAO = new AlunoPossuiItemDAO();
				EntityManagerFactory emfAlunoPossuiItem = Persistence.createEntityManagerFactory("objectdb/db/Loja.odb");
				EntityManager emAlunoPossuiItes = emfAlunoPossuiItem.createEntityManager();
				
				ObservableList<AlunoPossuiItem> listAlunoPossuiItens = FXCollections.observableArrayList(alunoPossuiItemDAO.getListAlunoPossuiItens(emAlunoPossuiItes));

				boolean alunoPossuiItem = false;
				
				AlunoPossuiItem alunoPossuiItemObj = null;
				
				String qtd = "0";
				
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
				
				JFXButton btnItem = new JFXButton(lblQtdItem.getText());
				btnItem.setGraphic(imgIcone);
				
				if (Integer.valueOf(lblQtdItem.getText()) <= 0) {
					btnItem.setDisable(true);
				}
				
				hboxInfoItens.getChildren().addAll(btnItem);
				hboxInfoItens.setPadding(new Insets(5, 5, 5, 5));
				hboxInfoItens.setAlignment(Pos.BOTTOM_CENTER);
				
				btnItem.setOnAction(e -> {
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
					
					if (localAlunoPossuiItem) {
						int qtdItem = -1;
						
						lblQtdItem.setText(String.valueOf(Integer.parseInt(qtdLocal) + qtdItem));
						btnItem.setText(lblQtdItem.getText());
						
						if (i.getImgNome().equals("pocaoVida")) {
							JFXButton vida = new JFXButton();
							vida.getStyleClass().add("vida");
							vida.setRipplerFill(Color.TRANSPARENT);
							vida.setDisableVisualFocus(true);
							
							flowPaneVidas.getChildren().add(vida);
						} else {
							if (i.getImgNome().equals("cuboGelo")) {
								btnItem.setDisable(true);
								timer.setDisable(true);
								
								double temporizador1 = 0.0;
								double temporizador2 = 10.0;
								
								Timeline timeline;
									
								timeline = new Timeline(new KeyFrame(Duration.seconds(temporizador1), evt -> timer.stop()),
									new KeyFrame(Duration.seconds(temporizador2), evt -> {
										timer.startFromCurrent();
										
										if (Integer.valueOf(lblQtdItem.getText()) <= 0) {
											btnItem.setDisable(true);
										} else {
											btnItem.setDisable(false);
										}
										
										timer.setDisable(false);
									}));
								timeline.setCycleCount(1);
								timeline.play();
							}
						}
					}
					
					int qtdItem = -1;
					
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
					
					if (Integer.valueOf(lblQtdItem.getText()) <= 0) {
						btnItem.setDisable(true);
					}
				});
			}
		}
		
		HBox hboxTimer = new HBox();
		hboxTimer.getChildren().add(timer);
		hboxTimer.setAlignment(Pos.CENTER);
		
		this.setMargin(hboxTimer, new Insets(0, 0, 0, 10));
		this.setMargin(flowPaneVidas, new Insets(10, 0, 0, 0));
		
		flowPaneVidas.setAlignment(Pos.CENTER);
		hboxInfoItens.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(hboxTimer, flowPaneVidas, hboxInfoItens);
		
		// vboxMenuLateralDireito.setMargin(timer, new Insets(0, 0, 0, 10));
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			this.setPadding(new Insets(0,  (2.00 / 100) * stage.getWidth(), 0, 0));
		};

	    stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}
}
