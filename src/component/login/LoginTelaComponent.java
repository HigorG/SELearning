package component.login;

import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class LoginTelaComponent extends BorderPane {
	public LoginTelaComponent (Stage stage, MediaPlayer mediaPlayer) {
		ConteudoEsquerdaComponent conteudoEsquerda = new ConteudoEsquerdaComponent(stage);
		ConteudoDireitaComponent conteudoDireita = new ConteudoDireitaComponent(stage, this, mediaPlayer);

		this.setLeft(conteudoEsquerda);
		this.setRight(conteudoDireita);
	}
}
