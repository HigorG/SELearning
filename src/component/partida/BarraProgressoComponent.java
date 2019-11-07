package component.partida;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import util.Global;
import util.InfoPartida;

public class BarraProgressoComponent extends HBox {
	public BarraProgressoComponent (Stage stage, Label lblTituloSecundario, Label status, ProgressBar bar) {
		lblTituloSecundario.setText("Questão #" + InfoPartida.questaoAtual);
		lblTituloSecundario.setStyle("-fx-font-size: 17px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		Region region = new Region();
	    HBox.setHgrow(region, Priority.ALWAYS);
	    
	    bar.setPrefSize(200, 24);
	    
	    bar.setProgress(InfoPartida.questaoAtual);
	    
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

	    /*
	    Timeline task = new Timeline(
	        new KeyFrame(
	                Duration.ZERO,       
	                new KeyValue(bar.progressProperty(), 0)
	        ),
	        new KeyFrame(
	                Duration.seconds(2), 
	                new KeyValue(bar.progressProperty(), 1)
	        )
	    );

	    Button button = new Button("Contar");
	    button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override public void handle(ActionEvent actionEvent) {
	            task.playFromStart();
	        }
	    });
	    */
        
	    HBox layout = new HBox(10);
	    layout.getChildren().addAll(
	        status,
	        bar
	    );
	    layout.setPadding(new Insets(10));
	    layout.setAlignment(Pos.CENTER_RIGHT);

	    layout.getStylesheets().add(getClass().getResource("/resources/css/striped-progress.css").toExternalForm());
	    
	    this.setPadding(new Insets(5, 0, 5, 10));
	    this.setStyle("-fx-background-color: WHITE;"
				+ "-fx-border-style: none none solid none;"
				+ "-fx-border-width: 0 0 2 0;"
				+ "-fx-border-color: #dde3e7;"
		);
	    
		this.getChildren().addAll(lblTituloSecundario, region, layout);
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			lblTituloSecundario.setStyle("-fx-font-size: " + (3.00 / 100) * stage.getHeight() + "px;");
		};
		
	    stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}
}
