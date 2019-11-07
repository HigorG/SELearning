package component.login;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import component.menu.professor.MenuProfessorComponent;
import component.util.CustomAvatarSelector;
import component.util.CustomJFXSnackBar;
import dao.AlunoDAO;
import dao.ProfessorDAO;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.Professor;
import util.UsuarioAtual;
import validation.CustomLimitedTextValidator;
import validation.CustomTextValidator;

public class ConteudoDireitaComponent extends HBox {
	public ConteudoDireitaComponent (Stage stage, LoginTelaComponent loginTelaComponent, MediaPlayer mediaPlayer) {
		this.setStyle("-fx-background-color: grey;");
		this.setAlignment(Pos.CENTER);
		
		VBox vboxLogin = new VBox();
		vboxLogin.setStyle("-fx-background-color: white;");
		
		Label lblLogin = new Label("Login");
		
		Label lblEmail = new Label("Email");
		JFXTextField textFieldEmail = new JFXTextField();
		
		Label lblSenha = new Label("Senha");
		JFXTextField textFieldSenha = new JFXTextField();
		
		JFXButton btnEntrar = new JFXButton("Entrar");
		btnEntrar.setStyle("-fx-background-color: #2dc6c1;");
		btnEntrar.setAlignment(Pos.CENTER);
		
		btnEntrar.setOnAction(event -> {
			validacaoLoginEmail(textFieldEmail);
			validacaoLoginSenha(textFieldSenha);
			
			ProfessorDAO professorDAO = new ProfessorDAO();
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
			EntityManager em = emf.createEntityManager();
			
			ObservableList<Professor> professorList = FXCollections.observableArrayList(professorDAO.getListProfessores(em));
			
			int naoEncontrei = 1;
			
			for(int i = 0; i < professorList.size(); i++){
				if(textFieldEmail.getText().equals(professorList.get(i).getEmail()) && textFieldSenha.getText().equals(professorList.get(i).getSenha())){
					UsuarioAtual.setUsuarioAtualProfessor(professorList.get(i));
					
					btnEntrar.setDisable(true);
					FadeTransition fadeTransition = new FadeTransition();
					fadeTransition.setDuration(Duration.millis(1000));
					fadeTransition.setNode(loginTelaComponent);
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					
					fadeTransition.setOnFinished((ActionEvent ev) ->{
						carregaVisaoProfessor(stage, mediaPlayer);
					});
					fadeTransition.play();
					
					naoEncontrei = 0;
					
					break;
				}
			}
			
			AlunoDAO alunoDAO = new AlunoDAO();
			EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
			EntityManager em1 = emf.createEntityManager();
			
			ObservableList<Aluno> alunoList = FXCollections.observableArrayList(alunoDAO.getListAlunos(em1));

			for(int i = 0; i < alunoList.size(); i++){
				if(textFieldEmail.getText().equals(alunoList.get(i).getEmail()) && textFieldSenha.getText().equals(alunoList.get(i).getSenha())){
					UsuarioAtual.setUsuarioAtualAluno(alunoList.get(i));
					
					btnEntrar.setDisable(true);
					
					FadeTransition fadeTransition = new FadeTransition();
					fadeTransition.setDuration(Duration.millis(1000));
					fadeTransition.setNode(loginTelaComponent);
					fadeTransition.setFromValue(1);
					fadeTransition.setToValue(0);
					
					fadeTransition.setOnFinished((ActionEvent ev) ->{
						carregaVisaoAluno(stage, mediaPlayer);
					});
					fadeTransition.play();
					
					naoEncontrei = 0;
					
					break;
				}
			}
			
			if (naoEncontrei == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Login Inválido");
				alert.setTitle("Erro");
				alert.setContentText("O erro aconteceu devido ao usuário ser inválido");
				alert.show();
			}
		});

		Label lblCadastrar = new Label("Não tem conta ainda? Cadastre-se!");
		lblCadastrar.setWrapText(true);
		
		JFXButton btnCadastrar = new JFXButton("Cadastrar-se");
		
		HBox hboxButtonEntrar = new HBox();
		hboxButtonEntrar.getChildren().add(btnEntrar);
		hboxButtonEntrar.setPadding(new Insets(0, 0, 0, 150));
		
		HBox hboxLblCadastrar = new HBox();
		hboxLblCadastrar.getChildren().add(lblCadastrar);
		
		HBox hboxButtonCadastrar = new HBox();
		hboxButtonCadastrar.getChildren().add(btnCadastrar);
		
		vboxLogin.getChildren().addAll(lblLogin, lblEmail, textFieldEmail, lblSenha, textFieldSenha, hboxButtonEntrar, hboxLblCadastrar, hboxButtonCadastrar);
		
		VBox vboxCadastro = new VBox();
		vboxCadastro.setStyle("-fx-background-color: white;");
		
		Label lblNovoNome = new Label("Usuario");
		JFXTextField textFieldNovoNome = new JFXTextField();
		
		Label lblNovoEmail = new Label("Email");
		JFXTextField textFieldNovoEmail = new JFXTextField();
		
		Label lblNovaSenha = new Label("Senha");
		JFXTextField textFieldNovaSenha = new JFXTextField();
		
		Label lblTipo = new Label("Sou um:");
		
		JFXRadioButton radioButtonProfessor = new JFXRadioButton("Professor");
		radioButtonProfessor.setPadding(new Insets(0, 20, 0, 0));
		JFXRadioButton radioButtonAluno = new JFXRadioButton("Aluno");
		
		ToggleGroup toggleGroupTipo = new ToggleGroup();
		
		radioButtonProfessor.setToggleGroup(toggleGroupTipo);
		radioButtonAluno.setToggleGroup(toggleGroupTipo);
		
		// Verificar se o professor ja está cadastrado

		ProfessorDAO professorDAO = new ProfessorDAO();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
		EntityManager em = emf.createEntityManager();
		
		ObservableList<Professor> list = FXCollections.observableArrayList(professorDAO.getListProfessores(em));
		
		if (!list.isEmpty()){
			radioButtonProfessor.setVisible(false);
			radioButtonProfessor.setDisable(true);
			
			radioButtonAluno.setSelected(true);
			radioButtonAluno.setVisible(false);
			radioButtonAluno.setDisable(true);
			
			lblTipo.setDisable(true);
			lblTipo.setVisible(false);
		}
		
		HBox hboxTipo = new HBox();
		hboxTipo.getChildren().addAll(radioButtonProfessor, radioButtonAluno);
		
		
		
		JFXButton btnEfetuarCadastro = new JFXButton("Cadastrar");

		btnEfetuarCadastro.setOnAction(ActionEvent -> {
			StackPane stack = Main.getStack();
			
			validacaoCadastroNome(textFieldNovoNome);
			validacaoCadastroEmail(textFieldNovoEmail);
			validacaoCadastroSenha(textFieldNovaSenha);
			
			btnEfetuarCadastro.setDisable(true);
			
			if((toggleGroupTipo.getSelectedToggle() == null) || !textFieldNovoNome.validate() || !textFieldNovoEmail.validate() || !textFieldNovaSenha.validate()){	
				JFXDialogLayout dialogContent = new JFXDialogLayout();
	    		dialogContent.setHeading(new Text("Falha no Cadastro"));
	    		dialogContent.setBody(new Text("Preencha corretamente as informações!"));
	    		
	    		JFXButton fechar = new JFXButton("OK");
	    		fechar.setButtonType(JFXButton.ButtonType.RAISED);
	    		fechar.setStyle("-fx-background-color: #1ab3ce;");
	    		fechar.setTextFill(Color.WHITE);
	    		dialogContent.setActions(fechar);
	    		dialogContent.setStyle("-fx-font-size: 14.0px;");
	    		
	    		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
	    		fechar.setOnAction((ActionEvent e) -> {
	    			dialog.close();
	    		});
	    		dialog.show();
	    	}else{
	    		EntityManagerFactory emfCadastro = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
				EntityManager emCadastro = emfCadastro.createEntityManager();
				
				String nome = textFieldNovoNome.getText();
				nome = nome.replaceFirst("^+ *", "");
				nome = nome.replaceFirst("^+ $", "");
				String email = textFieldNovoEmail.getText();
				String senha = textFieldNovaSenha.getText();
				
	    		if(toggleGroupTipo.getSelectedToggle() != null){
					if(toggleGroupTipo.getSelectedToggle().equals(radioButtonProfessor)){
						Professor professor = new Professor(nome, email, senha);
						
						ProfessorDAO professorDAOCadastro = new ProfessorDAO();
						professorDAOCadastro.armazenaDados(emCadastro, professor);
						
						radioButtonProfessor.setVisible(false);
						radioButtonProfessor.setDisable(true);
						
						radioButtonAluno.setSelected(true);
						radioButtonAluno.setVisible(false);
						radioButtonAluno.setDisable(true);
						
						lblTipo.setDisable(true);
						lblTipo.setVisible(false);
					}else{
						if (toggleGroupTipo.getSelectedToggle().equals(radioButtonAluno)){
							Aluno aluno = new Aluno(nome, email, senha);
							
							AlunoDAO alunoDAO = new AlunoDAO();
							alunoDAO.armazenaDados(emCadastro, aluno);
						}
					}
				}
	    		
	    		textFieldNovoNome.clear();
				textFieldNovoEmail.clear();
				textFieldNovaSenha.clear();
	    		
	    		JFXDialogLayout dialogContent = new JFXDialogLayout();
	    		dialogContent.setHeading(new Text("Cadastro concluido"));
	    		dialogContent.setBody(new Text("Cadastrado com sucesso!"));
	    		
	    		JFXButton fechar = new JFXButton("OK");
	    		fechar.setButtonType(JFXButton.ButtonType.RAISED);
	    		fechar.setStyle("-fx-background-color: #1ab3ce;");
	    		fechar.setTextFill(Color.WHITE);
	    		dialogContent.setActions(fechar);
	    		dialogContent.setStyle("-fx-font-size: 14.0px;");
	    		
	    		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
	    		fechar.setOnAction((ActionEvent e) -> {
	    			dialog.close();
	    		});
	    		dialog.show();
	    		
	    		StackPane stackPaneComponents = Main.getStack();
				
				JFXDialog dialog1 = (JFXDialog) stackPaneComponents.getChildren().get(1);
				dialog1.close();
//				
//				CustomJFXSnackBar snackbar = new CustomJFXSnackBar(stage, loginTelaComponent);
//				snackbar.show("Cadastro Efetuado", 10000);
	    	}
			
			btnEfetuarCadastro.setDisable(false);
		});
		
		// TODO isso nao funciona
		CustomAvatarSelector avatarSelector = new CustomAvatarSelector();
		
		vboxCadastro.getChildren().addAll(lblNovoNome, textFieldNovoNome, lblNovoEmail, textFieldNovoEmail, lblNovaSenha, textFieldNovaSenha, lblTipo, hboxTipo, avatarSelector, btnEfetuarCadastro);

		btnCadastrar.setOnAction(ActionEvent -> {
			StackPane stackPaneComponents = Main.getStack();
			
			JFXDialogLayout dialogContent = new JFXDialogLayout();
			dialogContent.setPrefSize(300, 450);
    		dialogContent.setHeading(new Text("Cadastro"));
    		// dialogContent.setBody(new Text("Preencha corretamente as informações!"));
			
			dialogContent.setPrefSize(vboxLogin.getWidth(), vboxLogin.getHeight());
			
    		dialogContent.getChildren().add(vboxCadastro);
    		
    		JFXButton fechar = new JFXButton("OK");
    		fechar.setButtonType(JFXButton.ButtonType.RAISED);
    		fechar.setStyle("-fx-background-color: #1ab3ce;");
    		fechar.setTextFill(Color.WHITE);
    		// dialogContent.setActions(fechar);
    		dialogContent.setStyle("-fx-font-size: 14.0px;");
    		
    		JFXDialog dialogCadastro;
    		dialogCadastro = new JFXDialog(stackPaneComponents, dialogContent, JFXDialog.DialogTransition.CENTER);
    		fechar.setOnAction((ActionEvent e) -> {
    			dialogCadastro.close();
    		});
    		dialogCadastro.show();
    		
    		dialogCadastro.setOnDialogClosed(Event -> {
    			loginTelaComponent.setEffect(null);
    		});
    		
    		BoxBlur blur = new BoxBlur(3, 3, 3);
    		loginTelaComponent.setEffect(blur);
		});
		
		this.getChildren().add(vboxLogin);
		
		float initWidth = (float) 916.0;
		float initHeight = (float) 599.0;

		this.setPrefSize(initWidth/2, initHeight/2);
		this.setPadding(new Insets((5.00 / 100) * initWidth, 
				0, (5.00 / 100) * initWidth, 0));
		HBox.setMargin(vboxLogin, new Insets((20.00 / 100) * initHeight, 0, (20.00 / 100) * initHeight, 0));
		
		vboxLogin.setPrefSize((60.00 / 100) * this.getWidth(), (80.00 / 100) * this.getHeight());
		vboxLogin.setPadding(new Insets((5.00 / 100) * initWidth,  
				(5.00 / 100) * initWidth, 0, (5.00 / 100) * initWidth));
	
		// ------------------------------------------------------------------------------------

		HBox.setMargin(vboxCadastro, new Insets((20.00 / 100) * initHeight, 0, (20.00 / 100) * initHeight, 0));
		
		vboxCadastro.setPrefSize((60.00 / 100) * this.getWidth(), (80.00 / 100) * this.getHeight());
		vboxCadastro.setPadding(new Insets((5.00 / 100) * initWidth,  
				(5.00 / 100) * initWidth, 0, (5.00 / 100) * initWidth));
		
		@SuppressWarnings("static-access")
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			vboxLogin.setMargin(lblLogin, new Insets(0, 0, (5.00 / 100) * stage.getHeight(), 0));
			vboxLogin.setMargin(lblEmail, new Insets(0, 0, (0.50 / 100) * stage.getHeight(), 0));
			vboxLogin.setMargin(textFieldEmail, new Insets(0, 0, (5.00 / 100) * stage.getHeight(), 0));
			vboxLogin.setMargin(lblSenha, new Insets(0, 0, (0.50 / 100) * stage.getHeight(), 0));
			vboxLogin.setMargin(textFieldSenha, new Insets(0, 0, (18.00 / 100) * stage.getHeight(), 0));

			vboxCadastro.setMargin(lblNovoNome, new Insets(-80, 0, 0, 0));
			vboxCadastro.setMargin(textFieldNovoNome, new Insets(0, 0, (5.00 / 100) * stage.getHeight(), 0));
			vboxCadastro.setMargin(lblNovoEmail, new Insets(0, 0, 0, 0));
			vboxCadastro.setMargin(textFieldNovoEmail, new Insets(0, 0, (5.00 / 100) * stage.getHeight(), 0));
			vboxCadastro.setMargin(lblNovaSenha, new Insets(0, 0, 0, 0));
			vboxCadastro.setMargin(textFieldNovaSenha, new Insets(0, 0, (5.00 / 100) * stage.getHeight(), 0));
			vboxCadastro.setMargin(hboxTipo, new Insets(0, 0, (8.00 / 100) * stage.getHeight(), 0));
			vboxCadastro.setMargin(btnEfetuarCadastro, new Insets(0, 0, (1.00 / 100) * stage.getHeight(), 0));
			
			hboxButtonEntrar.setPadding(new Insets(0, 0, 0, stage.getWidth() / 13));
			hboxLblCadastrar.setPadding(new Insets(0, 0, 0, -stage.getWidth() / 60));
			hboxButtonCadastrar.setPadding(new Insets(0, 0, 0, stage.getWidth() / 16));
			
			hboxButtonEntrar.setMargin(btnEntrar, new Insets(-50, 0, 60, 0));
			hboxLblCadastrar.setMargin(lblCadastrar, new Insets(-30, 0, 60, 0));
			hboxButtonCadastrar.setMargin(btnCadastrar, new Insets(-55, 0, 0, 0));
			
			this.setPrefSize(stage.getWidth()/2, stage.getHeight()/2);
			this.setPadding(new Insets((5.00 / 100) * stage.getWidth(), 
					0, (5.00 / 100) * stage.getWidth(), 0));
			HBox.setMargin(vboxLogin, new Insets((20.00 / 100) * stage.getHeight(), 0, (20.00 / 100) * stage.getHeight(), 0));
			this.setPadding(new Insets(0,  (2.00 / 100) * stage.getWidth(), 0, 0));
			
			vboxLogin.setPrefSize((30.00 / 100) * stage.getWidth(), (50.00 / 100) * stage.getHeight());
			vboxLogin.setPadding(new Insets((5.00 / 100) * stage.getWidth(),  
					(5.00 / 100) * stage.getWidth(), 0, (5.00 / 100) * stage.getWidth()));

			HBox.setMargin(vboxCadastro, new Insets((20.00 / 100) * stage.getHeight(), 0, (20.00 / 100) * stage.getHeight(), 0));
			vboxCadastro.setPrefSize((60.00 / 100) * stage.getWidth(), (50.00 / 100) * stage.getHeight());
			vboxCadastro.setPadding(new Insets((5.00 / 100) * stage.getWidth(),  
					(5.00 / 100) * stage.getWidth(), 0, (5.00 / 100) * stage.getWidth()));
		};

	    stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}

	private void carregaVisaoProfessor(Stage stage, MediaPlayer mediaPlayer) {
		Main.getStack().getChildren().remove(0);
		
		MenuProfessorComponent menuProfessor = new MenuProfessorComponent(stage, mediaPlayer);
		
		Main.getStack().getChildren().add(menuProfessor);
	}
	
	private void carregaVisaoAluno(Stage stage, MediaPlayer mediaPlayer) {
		Main.getStack().getChildren().remove(0);
		
		MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
		
		Main.getStack().getChildren().add(menuAluno);
	}
	
	private void validacaoLoginEmail(JFXTextField txtFieldEmail){
		RequiredFieldValidator requiredFieldValidatorEmail = new RequiredFieldValidator();
		requiredFieldValidatorEmail.setMessage("Campo obrigatorio!");

		requiredFieldValidatorEmail.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
	        .size("1em")
	        .styleClass("error")
	        .build());
		txtFieldEmail.getValidators().add(requiredFieldValidatorEmail);
		txtFieldEmail.focusedProperty().addListener((o, oldVal, newVal) -> {
	        if (!newVal) {
	        	txtFieldEmail.validate();
	        }
	    });
	}
	
	private void validacaoLoginSenha(JFXTextField txtFieldSenha){
		RequiredFieldValidator requiredFieldValidatorSenha = new RequiredFieldValidator();
		requiredFieldValidatorSenha.setMessage("Campo obrigatorio!");
		
		requiredFieldValidatorSenha.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
	        .size("1em")
	        .styleClass("error")
	        .build());
		txtFieldSenha.getValidators().add(requiredFieldValidatorSenha);
		txtFieldSenha.focusedProperty().addListener((o, oldVal, newVal) -> {
	        if (!newVal) {
	        	txtFieldSenha.validate();
	        }
	    });
	}
	
	private void validacaoCadastroNome(JFXTextField txtFieldNome){
		CustomTextValidator textValidatorNome = new CustomTextValidator();
		
		RequiredFieldValidator requiredFieldValidatorNome = new RequiredFieldValidator();
		requiredFieldValidatorNome.setMessage("Campo obrigatorio!");
		
		CustomLimitedTextValidator limitedTextValidator = new CustomLimitedTextValidator();
		limitedTextValidator.setLimit((int)20);

		limitedTextValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
				.glyph(FontAwesomeIcon.WARNING)
			    .size("1em")
			    .styleClass("error")
			    .build());
		txtFieldNome.getValidators().add(limitedTextValidator);
		txtFieldNome.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				txtFieldNome.validate();
			}
		});
		
		requiredFieldValidatorNome.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
	        .size("1em")
	        .styleClass("error")
	        .build());
		txtFieldNome.getValidators().add(requiredFieldValidatorNome);
		txtFieldNome.focusedProperty().addListener((o, oldVal, newVal) -> {
	        if (!newVal) {
	        	txtFieldNome.validate();
	        }
	    });
		
		textValidatorNome.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
				.glyph(FontAwesomeIcon.WARNING)
		        .size("1em")
		        .styleClass("error")
		        .build());
		txtFieldNome.getValidators().add(textValidatorNome);
		txtFieldNome.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				txtFieldNome.validate();
			}
		});
	}
	
	private void validacaoCadastroEmail(JFXTextField txtFieldEmail){
		RequiredFieldValidator requiredFieldValidatorEmail = new RequiredFieldValidator();
		requiredFieldValidatorEmail.setMessage("Campo obrigatorio!");
		
		CustomLimitedTextValidator limitedTextValidator = new CustomLimitedTextValidator();
		limitedTextValidator.setLimit((int)30);

		limitedTextValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
				.glyph(FontAwesomeIcon.WARNING)
			    .size("1em")
			    .styleClass("error")
			    .build());
		txtFieldEmail.getValidators().add(limitedTextValidator);
		txtFieldEmail.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				txtFieldEmail.validate();
			}
		});
		
		requiredFieldValidatorEmail.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
	        .size("1em")
	        .styleClass("error")
	        .build());
		txtFieldEmail.getValidators().add(requiredFieldValidatorEmail);
		txtFieldEmail.focusedProperty().addListener((o, oldVal, newVal) -> {
	        if (!newVal) {
	        	txtFieldEmail.validate();
	        }
	    });
	}
	
	private void validacaoCadastroSenha(JFXTextField txtFieldSenha){
		RequiredFieldValidator requiredFieldValidatorSenha = new RequiredFieldValidator();
		requiredFieldValidatorSenha.setMessage("Campo obrigatorio!");
		
		CustomLimitedTextValidator limitedTextValidator = new CustomLimitedTextValidator();
		limitedTextValidator.setLimit((int)20);

		limitedTextValidator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
				.glyph(FontAwesomeIcon.WARNING)
			    .size("1em")
			    .styleClass("error")
			    .build());
		txtFieldSenha.getValidators().add(limitedTextValidator);
		txtFieldSenha.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				txtFieldSenha.validate();
			}
		});
		
		requiredFieldValidatorSenha.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
			.glyph(FontAwesomeIcon.WARNING)
	        .size("1em")
	        .styleClass("error")
	        .build());
		txtFieldSenha.getValidators().add(requiredFieldValidatorSenha);
		txtFieldSenha.focusedProperty().addListener((o, oldVal, newVal) -> {
	        if (!newVal) {
	        	txtFieldSenha.validate();
	        }
	    });
	}
}
