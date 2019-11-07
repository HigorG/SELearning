package component.partida;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import util.InfoPartida;
import util.InfoSessao;
import util.UsuarioAtual;

public class BarraPerfilComponent extends BorderPane {
	public BarraPerfilComponent (Stage stage, Label lblPontuacao) {
		this.setPadding(new Insets(10, 0, 10, 10));
		this.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: solid none solid none;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: #dde3e7;"
		);
		
		Label lblTituloPrinciapal = new Label("Modelos de Caso de Uso");
		lblTituloPrinciapal.setStyle("-fx-font-size: 20px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
        Button btnBitcoins = new Button();
		btnBitcoins.getStyleClass().add("bitcoinIcon");
		
        Label lblBitcoins = new Label(String.valueOf(UsuarioAtual.getUsuarioAtualAluno().getBitcoins()));
        lblBitcoins.setStyle("-fx-font-size: 15px; -fx-font-family: \"Yu Gothic UI Semilight\";");
        
        HBox hboxPontuacao = new HBox();
        hboxPontuacao.setAlignment(Pos.CENTER);
        
        Button btnPontuacao = new Button();
		btnPontuacao.getStyleClass().add("pontuacaoIcon");
		
        lblPontuacao.setStyle("-fx-font-size: 15px; -fx-font-family: \"Yu Gothic UI Semilight\";");
        
        hboxPontuacao.getChildren().addAll(btnPontuacao, lblPontuacao, btnBitcoins, lblBitcoins);
        HBox.setMargin(btnPontuacao, new Insets(0, 7, 0, 0));
        HBox.setMargin(lblPontuacao, new Insets(0, 20, 0, 0));
        HBox.setMargin(btnBitcoins, new Insets(0, 7, 0, 0));
        HBox.setMargin(lblBitcoins, new Insets(0, 20, 0, 0));
        
		this.setLeft(lblTituloPrinciapal);
		this.setRight(hboxPontuacao);
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			this.setPadding(new Insets((1.79 / 100) * stage.getHeight(), 0, (1.79 / 100) * stage.getHeight(), (1.79 / 100) * stage.getHeight()));
			lblTituloPrinciapal.setStyle("-fx-font-size: " + (3.57 / 100) * stage.getHeight() + "px;");
		};
		
		stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}
}
