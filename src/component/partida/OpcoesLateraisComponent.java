package component.partida;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import component.timer.Timer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class OpcoesLateraisComponent extends VBox {
	int focused = 0;
	private final Duration FADE_DURATION = Duration.seconds(2.0);
	
	public OpcoesLateraisComponent (Stage stage, Timer timer, MediaPlayer mediaPlayer) {
		StackPane stack = Main.getStack();
		
		final JFXSlider volumeSlider = new JFXSlider(0, 1, 1);
		volumeSlider.setOrientation(Orientation.HORIZONTAL);
		volumeSlider.setStyle("");
		volumeSlider.getStyleClass().add("sliderVolume");

		mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());

        JFXButton btnOpcaoHome = new JFXButton();
        SVGGlyph glyphOpcaoHome = new SVGGlyph(-1,
				"test",
				"M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z ",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoHome.setSize(18, 18);
        Label lblOpcaoHome = new Label("Home");
        
        JFXButton btnOpcaoVolume = new JFXButton();
        final SVGGlyph glyphOpcaoVolume = new SVGGlyph(-1,
				"test",
				"M46.547,64.825c0,1.638-0.947,3.128-2.429,3.823c-0.573,0.27-1.187,0.401-1.798,0.401c-0.965,0-1.922-0.332-2.695-0.972" + 
				"		l-23.097-19.14H4.226C1.892,48.938,0,47.045,0,44.712V27.879c0-2.334,1.892-4.226,4.226-4.226h12.303l23.097-19.14" + 
				"		c1.263-1.046,3.012-1.269,4.493-0.569c1.48,0.695,2.429,2.185,2.429,3.823L46.547,64.825L46.547,64.825z M62.783,58.223" + 
				"		c-0.102,0.008-0.202,0.012-0.303,0.012c-1.117,0-2.193-0.441-2.988-1.237l-0.566-0.568c-1.48-1.479-1.654-3.821-0.406-5.504" + 
				"		c3.162-4.265,4.834-9.322,4.834-14.628c0-5.706-1.897-11.058-5.485-15.478c-1.366-1.68-1.24-4.12,0.29-5.65l0.564-0.565" + 
				"		c0.846-0.844,1.975-1.304,3.2-1.231c1.192,0.06,2.306,0.621,3.06,1.545c4.978,6.09,7.607,13.484,7.607,21.38" + 
				"		c0,7.354-2.324,14.354-6.725,20.241C65.131,57.521,64.007,58.135,62.783,58.223z",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoVolume.setSize(13, 18);
        Label lblOpcaoVolume = new Label("Volume");
        
        JFXButton btnOpcaoConfiguracoes = new JFXButton();
        SVGGlyph glyphOpcaoConfiguracoes = new SVGGlyph(-1,
				"test",
				"M267.92,119.461c-0.425-3.778-4.83-6.617-8.639-6.617\r\n" + 
				"			c-12.315,0-23.243-7.231-27.826-18.414c-4.682-11.454-1.663-24.812,7.515-33.231c2.889-2.641,3.24-7.062,0.817-10.133" + 
				"			c-6.303-8.004-13.467-15.234-21.289-21.5c-3.063-2.458-7.557-2.116-10.213,0.825c-8.01,8.871-22.398,12.168-33.516,7.529" + 
				"			c-11.57-4.867-18.866-16.591-18.152-29.176c0.235-3.953-2.654-7.39-6.595-7.849c-10.038-1.161-20.164-1.197-30.232-0.08" + 
				"			c-3.896,0.43-6.785,3.786-6.654,7.689c0.438,12.461-6.946,23.98-18.401,28.672c-10.985,4.487-25.272,1.218-33.266-7.574" + 
				"			c-2.642-2.896-7.063-3.252-10.141-0.853c-8.054,6.319-15.379,13.555-21.74,21.493c-2.481,3.086-2.116,7.559,0.802,10.214" + 
				"			c9.353,8.47,12.373,21.944,7.514,33.53c-4.639,11.046-16.109,18.165-29.24,18.165c-4.261-0.137-7.296,2.723-7.762,6.597" + 
				"			c-1.182,10.096-1.196,20.383-0.058,30.561c0.422,3.794,4.961,6.608,8.812,6.608c11.702-0.299,22.937,6.946,27.65,18.415" + 
				"			c4.698,11.454,1.678,24.804-7.514,33.23c-2.875,2.641-3.24,7.055-0.817,10.126c6.244,7.953,13.409,15.19,21.259,21.508" + 
				"			c3.079,2.481,7.559,2.131,10.228-0.81c8.04-8.893,22.427-12.184,33.501-7.536c11.599,4.852,18.895,16.575,18.181,29.167" + 
				"			c-0.233,3.955,2.67,7.398,6.595,7.85c5.135,0.599,10.301,0.898,15.481,0.898c4.917,0,9.835-0.27,14.752-0.817" + 
				"			c3.897-0.43,6.784-3.786,6.653-7.696c-0.451-12.454,6.946-23.973,18.386-28.657c11.059-4.517,25.286-1.211,33.281,7.572" + 
				"			c2.657,2.89,7.047,3.239,10.142,0.848c8.039-6.304,15.349-13.534,21.74-21.494c2.48-3.079,2.13-7.559-0.803-10.213" + 
				"			c-9.353-8.47-12.388-21.946-7.529-33.524c4.568-10.899,15.612-18.217,27.491-18.217l1.662,0.043" + 
				"			c3.853,0.313,7.398-2.655,7.865-6.588C269.044,139.917,269.058,129.639,267.92,119.461z M134.595,179.491" + 
				"			c-24.718,0-44.824-20.106-44.824-44.824c0-24.717,20.106-44.824,44.824-44.824c24.717,0,44.823,20.107,44.823,44.824" + 
				"			C179.418,159.385,159.312,179.491,134.595,179.491z",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoConfiguracoes.setSize(18, 18);
        Label lblOpcaoConfiguracoes = new Label("Configurações");
        
        JFXButton btnOpcaoAjuda = new JFXButton();
        SVGGlyph glyphOpcaoAjuda = new SVGGlyph(-1,
				"test",
				"M45.386,0.004C19.983,0.344-0.333,21.215,0.005,46.619c0.34,25.393,21.209,45.715,46.611,45.377" + 
				"		c25.398-0.342,45.718-21.213,45.38-46.615C91.656,19.986,70.786-0.335,45.386,0.004z M45.25,74l-0.254-0.004" + 
				"		c-3.912-0.116-6.67-2.998-6.559-6.852c0.109-3.788,2.934-6.538,6.717-6.538l0.227,0.004c4.021,0.119,6.748,2.972,6.635,6.937" + 
				"		C51.904,71.346,49.123,74,45.25,74z M61.705,41.341c-0.92,1.307-2.943,2.93-5.492,4.916l-2.807,1.938" + 
				"		c-1.541,1.198-2.471,2.325-2.82,3.434c-0.275,0.873-0.41,1.104-0.434,2.88l-0.004,0.451H39.43l0.031-0.907" + 
				"		c0.131-3.728,0.223-5.921,1.768-7.733c2.424-2.846,7.771-6.289,7.998-6.435c0.766-0.577,1.412-1.234,1.893-1.936" + 
				"		c1.125-1.551,1.623-2.772,1.623-3.972c0-1.665-0.494-3.205-1.471-4.576c-0.939-1.323-2.723-1.993-5.303-1.993" + 
				"		c-2.559,0-4.311,0.812-5.359,2.478c-1.078,1.713-1.623,3.512-1.623,5.35v0.457H27.936l0.02-0.477" + 
				"		c0.285-6.769,2.701-11.643,7.178-14.487C37.947,18.918,41.447,18,45.531,18c5.346,0,9.859,1.299,13.412,3.861" + 
				"		c3.6,2.596,5.426,6.484,5.426,11.556C64.369,36.254,63.473,38.919,61.705,41.341z",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoAjuda.setSize(18, 18);
        Label lblOpcaoAjuda = new Label("Ajuda");
        
        JFXButton btnOpcaoSair = new JFXButton();
        SVGGlyph glyphOpcaoSair = new SVGGlyph(-1,
				"test",
				"M256,0C114.842,0,0,114.842,0,256s114.842,256,256,256s256-114.842,256-256S397.158,0,256,0z M232.727,116.364" + 
				"			c0-12.853,10.42-23.273,23.273-23.273c12.853,0,23.273,10.42,23.273,23.273v99.739c0,12.853-10.42,23.273-23.273,23.273" + 
				"			c-12.853,0-23.273-10.42-23.273-23.273V116.364z M256,418.909c-81.579,0-147.948-66.369-147.948-147.948" + 
				"			c0-36.346,13.309-71.297,37.474-98.413c8.552-9.598,23.262-10.44,32.859-1.89c9.596,8.552,10.44,23.262,1.89,32.86" + 
				"			c-16.558,18.579-25.678,42.53-25.678,67.443c0,55.913,45.489,101.402,101.402,101.402s101.402-45.489,101.402-101.402" + 
				"			c0-24.916-9.118-48.867-25.676-67.443c-8.554-9.595-7.708-24.306,1.888-32.858c9.593-8.552,24.306-7.709,32.858,1.888" + 
				"			c24.166,27.113,37.475,62.062,37.475,98.413C403.948,352.54,337.579,418.909,256,418.909z",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoSair.setSize(18, 18);
        Label lblOpcaoSair = new Label("Sair");
        
        btnOpcaoHome.setOnAction(e -> btnOpcaoHomeAction(stage, stack, timer, mediaPlayer));
        btnOpcaoVolume.setOnAction(e -> btnOpcaoVolumeAction(stage, stack, btnOpcaoVolume, volumeSlider, mediaPlayer));
        btnOpcaoConfiguracoes.setOnAction(e -> btnOpcaoConfiguracoesAction(stage, stack, volumeSlider, timer, mediaPlayer));
        btnOpcaoAjuda.setOnAction(e -> btnOpcaoAjudaAction(stage, stack, timer, mediaPlayer));
        btnOpcaoSair.setOnAction(e -> btnOpcaoSairAction(stage, stack, timer, mediaPlayer));
        
        ContainerOpcaoLateralComponent containerOpcaoHome = new ContainerOpcaoLateralComponent(stage, btnOpcaoHome, glyphOpcaoHome, lblOpcaoHome);
        ContainerOpcaoLateralComponent containerOpcaoVolume = new ContainerOpcaoLateralComponent(stage, btnOpcaoVolume, glyphOpcaoVolume, lblOpcaoVolume);
        ContainerOpcaoLateralComponent containerOpcaoConfiguracoes = new ContainerOpcaoLateralComponent(stage, btnOpcaoConfiguracoes, glyphOpcaoConfiguracoes, lblOpcaoConfiguracoes);
        ContainerOpcaoLateralComponent containerOpcaoAjuda = new ContainerOpcaoLateralComponent(stage, btnOpcaoAjuda, glyphOpcaoAjuda, lblOpcaoAjuda);
        ContainerOpcaoLateralComponent containerOpcaoSair = new ContainerOpcaoLateralComponent(stage, btnOpcaoSair, glyphOpcaoSair, lblOpcaoSair);
        
        this.getChildren().addAll(containerOpcaoHome, containerOpcaoVolume, containerOpcaoConfiguracoes, containerOpcaoAjuda, containerOpcaoSair);
	}
	
	public void btnOpcaoHomeAction(Stage stage, StackPane stack, Timer timer, MediaPlayer mediaPlayer) {
		AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
		audioClip.play();
    	
		// TODO Ver isso ai
		timer.stop();
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text("Voltar ao menu principal"));
		dialogContent.setBody(new Text("Deseja realmente abandonar a partida atual?"));
		
		JFXButton cancelar = new JFXButton("Cancelar");
		cancelar.setButtonType(JFXButton.ButtonType.RAISED);
		cancelar.setStyle("-fx-background-color: #1ab3ce;");
		cancelar.setTextFill(Color.WHITE);
		
		JFXButton confirmar = new JFXButton("Confimar");
		confirmar.setButtonType(JFXButton.ButtonType.RAISED);
		confirmar.setStyle("-fx-background-color: #1ab3ce;");
		confirmar.setTextFill(Color.WHITE);
		dialogContent.setActions(cancelar, confirmar);
		dialogContent.setStyle("-fx-font-size: 14.0px;");
		
		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
		
		cancelar.setOnAction((ActionEvent e) -> {
			AudioClip audioClip2 = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
			audioClip2.play();
			
			timer.startFromCurrent();
			dialog.close();
		});
		
		confirmar.setOnAction((ActionEvent e) -> {
			Main.getStack().getChildren().remove(0);
			
			MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
			
			Main.getStack().getChildren().add(menuAluno);
			
			dialog.close();
			// this.setEffect(null);
		});
		
		dialog.show();
		
		dialog.setOnDialogClosed(Event -> {
			timer.startFromCurrent();
			// this.setEffect(null);
		});
		
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		// this.setEffect(blur);
	}
	
	public void btnOpcaoVolumeAction(Stage stage, StackPane stac, JFXButton btnOpcaoVolume, JFXSlider volumeSlider, MediaPlayer mediaPlayer) {
		AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
		audioClip.play();
		
		HBox hbox = (HBox) btnOpcaoVolume.getParent();
		
		HBox.setMargin(volumeSlider, new Insets(0, 10, 0, 0));
		
		if (hbox.getChildren().contains(volumeSlider)) {
			hbox.getChildren().remove(hbox.getChildren().size() - 1);
			hbox.getChildren().get(1).setVisible(true); // Torna a label visivel novamente
		} else {
			hbox.getChildren().add(volumeSlider);
			hbox.getChildren().get(1).setVisible(false); // Torna a label invisivel novamente
		}
	}
	
	public void btnOpcaoConfiguracoesAction(Stage stage, StackPane stack, JFXSlider volumeSlider, Timer timer, MediaPlayer mediaPlayer) {
		AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
		audioClip.play();
    	
		timer.stop();
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text("Configurações"));
		
		JFXButton btnDesligarMusica = new JFXButton("Desligar música");
		btnDesligarMusica.setOnAction(ev -> {
			volumeSlider.setValue(0);
		});
		
		dialogContent.setBody(btnDesligarMusica);
		
		JFXButton voltar = new JFXButton("Voltar");
		voltar.setButtonType(JFXButton.ButtonType.RAISED);
		voltar.setStyle("-fx-background-color: #1ab3ce;");
		voltar.setTextFill(Color.WHITE);
		
		dialogContent.setActions(voltar);
		dialogContent.setStyle("-fx-font-size: 14.0px;");
		
		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
		
		voltar.setOnAction((ActionEvent e) -> {
			AudioClip audioClip2 = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
			audioClip2.play();
			timer.startFromCurrent();
			
			dialog.close();
		});
		
		dialog.show();
		
		dialog.setOnDialogClosed(Event -> {
			// this.setEffect(null);
		});
		
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		// this.setEffect(blur);
	}
	
	public void btnOpcaoAjudaAction(Stage stage, StackPane stack, Timer timer, MediaPlayer mediaPlayer) {
		AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
		audioClip.play();
    	
		timer.stop();
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text("Ajuda"));
		Label lblBody = new Label("Nas questões de múltipla escolha e verdadeiro/falso basta clicar na opção "
				+ "que deseja. Nas questões de relacionar, é necessário clicar no número desejado e arrastar até o "
				+ "círculo da resposta correspondente. Caso acerte a questão, ela automaticamente passa para a próxima, "
				+ "caso erre, uma vida será descontada. A partida é ganha quando todas as alternativas são respondidas "
				+ "e o número de vidas é maior que zero.");
		lblBody.setWrapText(true);
		dialogContent.setBody(lblBody);
		
		JFXButton voltar = new JFXButton("Voltar");
		voltar.setButtonType(JFXButton.ButtonType.RAISED);
		voltar.setStyle("-fx-background-color: #1ab3ce;");
		voltar.setTextFill(Color.WHITE);
		
		dialogContent.setActions(voltar);
		dialogContent.setStyle("-fx-font-size: 14.0px;");
		
		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
		
		voltar.setOnAction((ActionEvent e) -> {
			AudioClip audioClip2 = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
			audioClip2.play();
			timer.startFromCurrent();
			
			dialog.close();
		});
		
		dialog.show();
		
		dialog.setOnDialogClosed(Event -> {
			// this.setEffect(null);
		});
		
		BoxBlur blur = new BoxBlur(3, 3, 3);
		
		// this.setEffect(blur);
	}
	
	public void btnOpcaoSairAction(Stage stage, StackPane stack, Timer timer, MediaPlayer mediaPlayer) {
		AudioClip audioClip = new AudioClip(getClass().getResource("/sounds/BotaoOpcao1.wav").toExternalForm());
		audioClip.play();
		
		Platform.exit();
		
		// Encerrar a partida atual
		// Fazer logout do usuario
		// Fazer aparecer um card de usuário deslogou com sucesso na tela de login e voltar para o login
	}
}
