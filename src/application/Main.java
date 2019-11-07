package application;

import java.io.File;
import java.net.URL;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;

import automation.InicializadorBanco;
import component.login.LoginTelaComponent;
import component.menu.aluno.MenuAlunoComponent;
import component.menu.professor.MenuProfessorComponent;
import component.partida.PartidaTelaComponent;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import model.ConfiguracoesJogo;
import model.Professor;
import util.Configuracoes;
import util.UsuarioAtual;
import component.util.CustomJFXDecorator;
import dao.AlunoDAO;
import dao.ConfiguracoesJogoDAO;
import dao.ProfessorDAO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {
	private static BorderPane root = new BorderPane();
	private static StackPane stackComponentes = new StackPane();
	static volatile MediaPlayer mediaPlayer;
	
	public static BorderPane getRoot(){
		return root;
	}
	
	public static StackPane getStack() {
		return stackComponentes;
	}
	
	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	@Override
	public void start(Stage stage) {
		try {
			CustomJFXDecorator decorator = new CustomJFXDecorator(stage, root, true, true, true);
			decorator.setCustomMaximize(true); // Isso previne que o programa cubra o taskbar do windows

			Media sound = new Media(getClass().getResource("/music/netherplace.mp3").toExternalForm()); 
			mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				public void run() {
					mediaPlayer.seek(Duration.ZERO);
				}
			});
			
			mediaPlayer.play();
			
			InicializadorBanco ib = new InicializadorBanco();
			ib.inicializaBanco();
			
			ConfiguracoesJogoDAO configuracoesJogoDAO = new ConfiguracoesJogoDAO();
			EntityManagerFactory emfConfiguracoesJogo = Persistence.createEntityManagerFactory("objectdb/db/GameDataBase.odb");
			EntityManager emExisteDatabaseeConfiguracoesJogo = emfConfiguracoesJogo.createEntityManager();
			
			ObservableList<ConfiguracoesJogo> listConfiguracoesJogo = FXCollections.observableArrayList(configuracoesJogoDAO.getListConfiguracoesJogo(emExisteDatabaseeConfiguracoesJogo));
			
			ConfiguracoesJogo configuracoes = listConfiguracoesJogo.get(0);
			
			Configuracoes.ordem = configuracoes.getOrdem();
			Configuracoes.tempoParaResponderAtivado = configuracoes.getTempoParaResponderAtivado();
			Configuracoes.tempoMaxParaResponder = configuracoes.getTempoMaxParaResponder();
			Configuracoes.qtdVidas = configuracoes.getQtdVidas();
			
			// ----------------------------------------------------------------
			// Só pra começar o jogo sem precisar do login
			// ---------------------------------------------------------------
//			AlunoDAO alunoDAO = new AlunoDAO();
//			EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
//			EntityManager em = emf.createEntityManager();
//			
//			ObservableList<Aluno> list = FXCollections.observableArrayList(alunoDAO.getListAlunos(em));
//			
//			if (!list.isEmpty()){
//				UsuarioAtual.setUsuarioAtualAluno(list.get(1));
//				
//				System.out.println(UsuarioAtual.getUsuarioAtualAluno().getNome());
//			}
			
//			ProfessorDAO professorDAO = new ProfessorDAO();
//			EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
//			EntityManager em = emf.createEntityManager();
//			
//			ObservableList<Professor> list = FXCollections.observableArrayList(professorDAO.getListProfessores(em));
//			
//			if (!list.isEmpty()){
//				UsuarioAtual.setUsuarioAtualProfessor(list.get(0));
//				
//				System.out.println(UsuarioAtual.getUsuarioAtualProfessor().getNome());
//			}
			
			// ----------------------------------------------------------------
			// Só pra começar o jogo sem precisar do login
			// ---------------------------------------------------------------
			
			LoginTelaComponent loginComponent = new LoginTelaComponent(stage, mediaPlayer);
			stackComponentes.getChildren().add(loginComponent);
			
//			MenuProfessorComponent menuProfessor = new MenuProfessorComponent(stage, mediaPlayer);
//			stackComponentes.getChildren().add(menuProfessor);
			
//			MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
//			stackComponentes.getChildren().add(menuAluno);
			
//			PartidaTelaComponent partidaComponent = new PartidaTelaComponent(stage, mediaPlayer);
//			stackComponentes.getChildren().add(partidaComponent);
			
			root.setCenter(stackComponentes);
			
			// root.setCenter(createVolumeControls(mediaPlayer));
			
			Scene scene = new Scene(decorator, 900, 560);
			
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/DecoratorStyleSheet.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/HiddenTabPane.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/StyledTextArea.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/game.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/jfoenix-design.css").toExternalForm());
//			scene.getStylesheets().add(this.getClass().getResource("/resources/css/jfoenix-components.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/CustomListView.css").toExternalForm());
			scene.getStylesheets().add(this.getClass().getResource("/resources/css/LojaItem.css").toExternalForm());
			
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
			
			Platform.exit();
		}
	}
	
	private static final Duration FADE_DURATION = Duration.seconds(2.0);
	
	public VBox createVolumeControls(final MediaPlayer mediaPlayer) {
	    final Slider volumeSlider = new Slider(0, 1, 1);
	    volumeSlider.setOrientation(Orientation.HORIZONTAL);

	    mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());

	    final Timeline fadeInTimeline = new Timeline(
	      new KeyFrame(
	        FADE_DURATION,
	        new KeyValue(mediaPlayer.volumeProperty(), 1.0)
	      )
	    );

	    final Timeline fadeOutTimeline = new Timeline(
	      new KeyFrame(
	        FADE_DURATION,
	        new KeyValue(mediaPlayer.volumeProperty(), 0.0)
	      )
	    );

	    JFXButton fadeIn = new JFXButton("Fade In");
	    fadeIn.setOnAction(new EventHandler<ActionEvent>() {
	      @Override public void handle(ActionEvent t) {
	        fadeInTimeline.play();
	      }
	    });
	    fadeIn.setMaxWidth(Double.MAX_VALUE);

	    JFXButton fadeOut = new JFXButton("Fade Out");
	    fadeOut.setOnAction(new EventHandler<ActionEvent>() {
	      @Override public void handle(ActionEvent t) {
	        fadeOutTimeline.play();
	      }
	    });
	    fadeOut.setMaxWidth(Double.MAX_VALUE);

	    VBox controls = new VBox();
	    controls.getChildren().setAll(
	      volumeSlider,
	      fadeIn,
	      fadeOut
	    );
	    controls.setAlignment(Pos.CENTER);
	    VBox.setVgrow(volumeSlider, Priority.ALWAYS);

	    controls.disableProperty().bind(
	      Bindings.or(
	        Bindings.equal(Timeline.Status.RUNNING, fadeInTimeline.statusProperty()),
	        Bindings.equal(Timeline.Status.RUNNING, fadeOutTimeline.statusProperty())
	      )
	    );

	    return controls;
	  }
	
	public static void main(String[] args) {
		launch(args);
	}
}
