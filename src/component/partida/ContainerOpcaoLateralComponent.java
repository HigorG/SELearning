package component.partida;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ContainerOpcaoLateralComponent extends HBox {
	int focused = 0;
	
	public ContainerOpcaoLateralComponent(Stage stage, JFXButton button, SVGGlyph glyph, Label lblOpcao) {
		StackPane stack = Main.getStack();
		
		button.setDisableVisualFocus(true);
		button.setStyle("-fx-background-radius: 40; -fx-background-color: transparent");
		button.setPrefSize(40, 40);
		button.setRipplerFill(Color.valueOf("#b0b0b0"));
		button.setGraphic(glyph);
		
		this.setStyle("-fx-background-color: #F2F5F7;"
				+ "-fx-border-style: none none none none;"
				+ "-fx-border-width: 0;"
				+ "-fx-border-color: #dde3e7;"
		);
		// this.setPadding(new Insets(5, 5, 5, 5));
		
		HBox containerOpcao = new HBox();
		containerOpcao.setStyle("-fx-background-color: transparent; -fx-background-radius: 50;");
		containerOpcao.setAlignment(Pos.CENTER_LEFT);
		containerOpcao.setPrefSize(140, 40);

		lblOpcao.setStyle("-fx-text-fill: TRANSPARENT");
		
		FadeTransition fadeIn = new FadeTransition(
			Duration.millis(100)
		);
		
		fadeIn.setNode(lblOpcao);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.setCycleCount(1);
		fadeIn.setAutoReverse(false);
		
		FadeTransition fadeIn2 = new FadeTransition(
			Duration.millis(100)
		);
		
		fadeIn2.setNode(containerOpcao);
		fadeIn2.setFromValue(0.0);
		fadeIn2.setToValue(1.0);
		fadeIn2.setCycleCount(1);
		fadeIn2.setAutoReverse(false);
        
        containerOpcao.getChildren().addAll(button, lblOpcao);
        this.getChildren().add(containerOpcao);
		
		containerOpcao.setOnMouseEntered(Event -> {
			if (focused == 0) {
				glyph.setFill(Color.valueOf("#ffffff"));
				button.setRipplerFill(Color.valueOf("#ffffff"));
				lblOpcao.setStyle("-fx-text-fill: WHITE");
				containerOpcao.setStyle("-fx-background-color: #b0b0b0; -fx-background-radius: 50;");
				fadeIn.playFromStart();
				fadeIn2.playFromStart();
				focused = 1;
			}
		});
		
		containerOpcao.setOnMouseExited(Event -> {
			focused = 0;
			
			glyph.setFill(Color.valueOf("#b0b0b0"));
			button.setRipplerFill(Color.valueOf("#b0b0b0"));
			lblOpcao.setStyle("-fx-text-fill: TRANSPARENT");
			containerOpcao.setStyle("-fx-background-color: TRANSPARENT; -fx-background-radius: 50;");
			fadeIn.playFromStart();
			fadeIn2.playFromStart();
		});
	}
}
