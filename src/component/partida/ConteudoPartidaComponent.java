package component.partida;

import component.timer.Timer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class ConteudoPartidaComponent extends BorderPane {
	private Label lblTituloSecundario = new Label("");
	private Label status = new Label("");
	private ProgressBar bar = new ProgressBar(0);
	
	public ConteudoPartidaComponent (Stage stage, Timer timer, MediaPlayer mediaPlayer, FlowPane flowPaneVidas, Label lblPontuacao) {
		this.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: hidden solid solid solid;"
				+ "-fx-border-width: 1;"
				+ "-fx-border-color: #dde3e7;"
		);
		
		BarraProgressoComponent barraProgresso = new BarraProgressoComponent(stage, lblTituloSecundario, status, bar);
		AddonsPartidaComponent addonsPartida = new AddonsPartidaComponent(stage, timer, flowPaneVidas);
		ConteudoQuestaoComponent conteudoQuestao = new ConteudoQuestaoComponent(stage, timer, mediaPlayer, lblTituloSecundario, lblPontuacao, status, bar, flowPaneVidas);
		
		BorderPane.setMargin(conteudoQuestao, new Insets(0, 0, 0, 10));
		
		this.setTop(barraProgresso);
		this.setCenter(conteudoQuestao);
		this.setRight(addonsPartida);
	}
}
