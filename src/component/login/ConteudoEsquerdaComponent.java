package component.login;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.AlunoDAO;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Aluno;
import util.Global;
import util.UsuarioAtual;

public class ConteudoEsquerdaComponent extends HBox {
	public ConteudoEsquerdaComponent (Stage stage) {
		this.setStyle("-fx-background-color: linear-gradient(to left bottom, rgba(49, 170, 188, 1) 60%, rgba(118, 118, 115, 1) 50%);");
		this.setAlignment(Pos.CENTER);
		
		VBox vboxLogo = new VBox();
		vboxLogo.setAlignment(Pos.CENTER);
		vboxLogo.setStyle("-fx-background-color: transparent;");
		
		AlunoDAO alunoDAO = new AlunoDAO();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
		EntityManager em = emf.createEntityManager();
		
		ObservableList<Aluno> list = FXCollections.observableArrayList(alunoDAO.getListAlunos(em));
		
		if (!list.isEmpty()){
			UsuarioAtual.setUsuarioAtualAluno(list.get(0));
			
			System.out.println(UsuarioAtual.getUsuarioAtualAluno().getNome());
		}
		
		ImageView logo = new ImageView("/resources/images/Logo.png");
		logo.setFitWidth(400);
		logo.setPreserveRatio(true);
		
		vboxLogo.getChildren().addAll(logo);
		
		this.setPrefSize(Global.initWidth / 2, Global.initHeight / 2);
		
		this.setPadding(new Insets(
							(5.00 / 100) * Global.initWidth,
							(5.00 / 100) * Global.initWidth,
							(5.00 / 100) * Global.initWidth,
							0)
		);
		
		this.getChildren().add(vboxLogo);
		
		HBox.setMargin(vboxLogo, new Insets((20.00 / 100) * Global.initHeight, 0, (20.00 / 100) * Global.initHeight, 0));
		
		vboxLogo.setPrefSize((60.00 / 100) * this.getWidth(), (80.00 / 100) * this.getHeight());
		vboxLogo.setPadding(new Insets((5.00 / 100) * Global.initWidth,  
				(5.00 / 100) * Global.initWidth, (5.00 / 100) * Global.initWidth, (5.00 / 100) * Global.initWidth));
		
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
			this.setPrefSize(stage.getWidth()/2, stage.getHeight()/2);
			this.setPadding(new Insets((5.00 / 100) * stage.getWidth(), 
					0, (5.00 / 100) * stage.getWidth(), 0));
			HBox.setMargin(vboxLogo, new Insets((20.00 / 100) * stage.getHeight(), 0, (20.00 / 100) * stage.getHeight(), 0));
			this.setPadding(new Insets(0,  (2.00 / 100) * stage.getWidth(), 0, 0));
			
			vboxLogo.setPrefSize((60.00 / 100) * this.getWidth(), (80.00 / 100) * this.getHeight());
			vboxLogo.setPadding(new Insets((5.00 / 100) * stage.getWidth(),  
					(5.00 / 100) * stage.getWidth(), 0, (5.00 / 100) * stage.getWidth()));
			
			logo.setFitWidth(0.3 * stage.getWidth());
			
			// Evita que o logo diminua demais
			if (logo.getFitWidth() < 200.0) {
				logo.setFitWidth(200);
			}
		};
		
		stage.widthProperty().addListener(stageSizeListener);
	    stage.heightProperty().addListener(stageSizeListener);
	}
}
