/*
 * Copyright (c) 2017 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package component.timer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Aluno;
import util.Configuracoes;
import util.InfoPartida;
import util.InfoSessao;
import util.UsuarioAtual;

import static javafx.animation.Interpolator.EASE_BOTH;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jfoenix.animation.JFXNodesAnimation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.svg.SVGGlyph;

import application.Main;
import component.menu.aluno.MenuAlunoComponent;
import component.timer.TimerEvent.Type;
import dao.AlunoDAO;


/**
 * User: hansolo
 * Date: 11.10.17
 * Time: 17:48
 */
@DefaultProperty("children")
public class Timer extends Region {
    public  enum         State { RUNNING, WAITING, STOPPED }
    public  static final Color                    DEFAULT_COLOR    = Color.web("0x407DBD");
    private static final double                   PREFERRED_WIDTH  = 19;
    private static final double                   PREFERRED_HEIGHT = 19;
    private static final double                   MINIMUM_WIDTH    = 19;
    private static final double                   MINIMUM_HEIGHT   = 19;
    private static final double                   MAXIMUM_WIDTH    = 1024;
    private static final double                   MAXIMUM_HEIGHT   = 1024;
    private        final TimerEvent               STARTED          = new TimerEvent(Timer.this, Type.STARTED);
    private        final TimerEvent               STOPPED          = new TimerEvent(Timer.this, Type.STOPPED);
    private        final TimerEvent               CONTINUED        = new TimerEvent(Timer.this, Type.CONTINUED);
    private        final TimerEvent               FINISHED         = new TimerEvent(Timer.this, Type.FINISHED);
    private        final TimerEvent               RESET            = new TimerEvent(Timer.this, Type.RESET);
    private        final TimerEvent               WAITING          = new TimerEvent(Timer.this, Type.WAITING);
    private              double                   size;
    private              double                   width;
    private              double                   height;
    private              double                   centerX;
    private              double                   centerY;
    private              Pane                     pane;
    private              Paint                    backgroundPaint;
    private              Paint                    borderPaint;
    private              double                   borderWidth;
    private              Arc                      ring;
    private              Arc                      progressBar;
    private              Rectangle                stopButton;
    private              Path                     playButton;
    private              MoveTo                   playButtonP1;
    private              LineTo                   playButtonP2;
    private              LineTo                   playButtonP3;
    private              Color                    _backgroundColor;
    private              ObjectProperty<Color>    backgroundColor;
    private              Color                    _color;
    private              ObjectProperty<Color>    color;
    private              Color                    _waitingColor;
    private              ObjectProperty<Color>    waitingColor;
    private              boolean                  _playButtonVisible;
    private              BooleanProperty          playButtonVisible;
    private              DoubleProperty           progress;
    private              State                    state;
    private              Duration                 _duration;
    private              ObjectProperty<Duration> duration;
    private              Duration                 currentDuration;
    private              Timeline                 timeline;
    private              List<TimerEventListener> listenerList = new CopyOnWriteArrayList<>();
    private BorderPane father;

    // ******************** Constructors **************************************
    public Timer(Stage stage, MediaPlayer mediaPlayer) {
        // getStylesheets().add(Timer.class.getResource("timer.css").toExternalForm());
        backgroundPaint    = Color.TRANSPARENT;
        borderPaint        = Color.TRANSPARENT;
        borderWidth        = 0d;
        _backgroundColor   = Color.TRANSPARENT;
        _color             = DEFAULT_COLOR;
        _waitingColor      = DEFAULT_COLOR;
        _playButtonVisible = true;
        state              = State.STOPPED;
        _duration          = Duration.seconds(10);
        currentDuration    = Duration.ZERO;
        progress           = new DoublePropertyBase(0) {
            @Override protected void invalidated() { progressBar.setLength(-360.0 * get()); }
            @Override public Object getBean() { return Timer.this; }
            @Override public String getName() { return "progress"; }
        };
        timeline           = new Timeline();
        initGraphics();
        registerListeners(stage, mediaPlayer);
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        getStyleClass().add("timer");

        ring = new Arc();
        ring.setStartAngle(0);
        ring.setLength(360);
        ring.setStroke(_color);
        ring.setStrokeType(StrokeType.INSIDE);

        progressBar = new Arc();
        progressBar.setType(ArcType.OPEN);
        progressBar.setFill(_backgroundColor);
        progressBar.setStroke(_color);
        progressBar.setStrokeLineCap(StrokeLineCap.BUTT);
        progressBar.setStartAngle(90);
        progressBar.setLength(0);
        progressBar.setMouseTransparent(true);

        stopButton = new Rectangle();
        stopButton.setVisible(false);
        stopButton.setManaged(false);
        stopButton.setStroke(null);
        stopButton.setMouseTransparent(true);

        playButtonP1 = new MoveTo();
        playButtonP2 = new LineTo();
        playButtonP3 = new LineTo();
        playButton = new Path(playButtonP1, playButtonP2, playButtonP3, new ClosePath());
        playButton.setStroke(null);
        playButton.setMouseTransparent(true);
        
/// ----------------------------------------------------------------------------------------------
        /*
        Button button = new Button();
        button.setText("Start Timer");
        
        ring.setOnMouseClicked(e -> {
        	System.out.println("Ativo");
                if (InfoSessao.timeline != null) {
                	InfoSessao.timeline.stop();
                }
                
                InfoSessao.timeSeconds.set(InfoSessao.STARTTIME);
                InfoSessao.timeline = new Timeline();
                InfoSessao.timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(InfoSessao.STARTTIME),
                        new KeyValue(InfoSessao.timeSeconds, 0)));
                InfoSessao.timeline.playFromStart();
        });
		
		Label lblTempo = new Label();
		
		lblTempo.textProperty().bind(InfoSessao.timeSeconds.asString());
		lblTempo.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals("0")) {
				System.out.println("CABO O TEMPO MANO");
				
			}
		});
		*/
        
/// ----------------------------------------------------------------------------------------------
        
        pane = new Pane(ring, progressBar, stopButton, playButton);
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidth))));

        getChildren().setAll(pane);
    }

    private void registerListeners(Stage stage, MediaPlayer mediaPlayer) {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        timeline.setOnFinished(event -> {
            finished();
            resetFromTimesUp(stage, mediaPlayer);
            fireTimerEvent(FINISHED);
        });
        
        // TODO Apenas marcando o evento de clique
        ring.setOnMousePressed(event -> {
            switch(state) {
                case RUNNING: stopFromClick(); break;
                case STOPPED: if (currentDuration.greaterThan(Duration.ZERO)) { startFromCurrent(); } else { start(); } break;
                case WAITING: stopFromClick(); break;
            }
        });
    }


    // ******************** Methods *******************************************
    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT) { return super.computePrefWidth(HEIGHT); }
    @Override protected double computePrefHeight(final double WIDTH) { return super.computePrefHeight(WIDTH); }
    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public Color getBackgroundColor() { return null == backgroundColor ? _backgroundColor : backgroundColor.get(); }
    public void setBackgroundColor(final Color COLOR) {
        if (null == backgroundColor) {
            _backgroundColor = COLOR;
            redraw();
        } else {
            backgroundColor.set(COLOR);
        }
    }
    
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == backgroundColor) {
            backgroundColor = new ObjectPropertyBase<Color>(_backgroundColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Timer.this; }
                @Override public String getName() { return "backgroundColor"; }
            };
            _backgroundColor = null;
        }
        return backgroundColor;
    }

    public Color getColor() { return null == color ? _color : color.get(); }
    public void setColor(final Color COLOR) {
        if (null == color) {
            _color = COLOR;
            redraw();
        } else {
            color.set(COLOR);
        }
    }
    
    public ObjectProperty<Color> colorProperty() {
        if (null == color) {
            color = new ObjectPropertyBase<Color>(_color) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Timer.this; }
                @Override public String getName() { return "color"; }
            };
            _color = null;
        }
        return color;
    }

    public Color getWaitingColor() { return null == waitingColor ? _waitingColor : waitingColor.get(); }
    public void setWaitingColor(final Color COLOR) {
        if (null == waitingColor) {
            _waitingColor = COLOR;
            redraw();
        } else {
            waitingColor.set(COLOR);
            redraw();
        }
    }
    public ObjectProperty<Color> waitingColorProperty() {
        if (null == waitingColor) {
            waitingColor = new ObjectPropertyBase<Color>(_waitingColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return Timer.this; }
                @Override public String getName() { return "waitingColor"; }
            };
            _waitingColor = null;
        }
        return waitingColor;
    }

    public boolean isPlayButtonVisible() { return null == playButtonVisible ? _playButtonVisible : playButtonVisible.get(); }
    public void setPlayButtonVisible(final boolean VISIBLE) {
        if (null == playButtonVisible) {
            _playButtonVisible = VISIBLE;
            if (VISIBLE) { enableNode(playButton); } else { disableNode(playButton); }
        } else {
            playButtonVisible.set(VISIBLE);
        }
    }
    
    public BooleanProperty playButtonVisibleProperty() {
        if (null == playButtonVisible) {
            playButtonVisible = new BooleanPropertyBase(_playButtonVisible) {
                @Override protected void invalidated() { if (get()) { enableNode(playButton); } else { disableNode(playButton); }}
                @Override public Object getBean() { return Timer.this; }
                @Override public String getName() { return "playButtonVisible"; }
            };
        }
        return playButtonVisible;
    }

    public double getProgress() { return progress.get(); }
    
    public void setProgress(final double PROGRESS) { progress.set(clamp(0.0, 1.0, PROGRESS)); }
    public ReadOnlyDoubleProperty progressProperty() { return progress; }

    public Duration getDuration() { return null == duration ? _duration : duration.get(); }
    
    public void setDuration(final Duration DURATION) {
        if (null == duration) {
            _duration = DURATION;
        } else {
            duration.set(DURATION);
        }
    }
    
    public ObjectProperty<Duration> durationProperty() {
        if (null == duration) {
            duration = new ObjectPropertyBase<Duration>(_duration) {
                @Override public Object getBean() { return Timer.this; }
                @Override public String getName() { return "duration"; }
            };
            _duration = null;
        }
        return duration;
    }

    public void start() {
        ring.setLength(360);

        KeyValue kv0 = new KeyValue(progress, 0.0);
        KeyValue kv1 = new KeyValue(progress, 1.0);

        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(getDuration(), kv1);

        timeline.getKeyFrames().setAll(kf0, kf1);

        if (isPlayButtonVisible()) { disableNode(playButton); }
        enableNode(stopButton);

        timeline.setCycleCount(1);
        timeline.playFromStart();

        state = State.RUNNING;
        fireTimerEvent(STARTED);
    }
    
    public void startFromCurrent() {
        ring.setLength(360);

        KeyValue kv0 = new KeyValue(progress, 0.0);
        KeyValue kv1 = new KeyValue(progress, 1.0);

        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(getDuration(), kv1);

        timeline.getKeyFrames().setAll(kf0, kf1);
        timeline.jumpTo(currentDuration);

        if (isPlayButtonVisible()) { disableNode(playButton); }
        enableNode(stopButton);

        timeline.setCycleCount(1);
        timeline.play();

        state = State.RUNNING;
        fireTimerEvent(CONTINUED);
    }
    
    public void stop() {
        currentDuration = state == State.WAITING ? Duration.ZERO : timeline.getCurrentTime();
        timeline.stop();
        timeline.setCycleCount(1);

        ring.setLength(360);
        ring.setStroke(getColor());

        disableNode(stopButton);
        stopButton.setFill(getColor());
        if (isPlayButtonVisible()) { enableNode(playButton); }

        state = State.STOPPED;
        fireTimerEvent(STOPPED);
    }
    
    public void reset() {
    	finished();
    	fireTimerEvent(RESET);
    }
    
    public void waiting() {
        timeline.stop();
        KeyValue kv0 = new KeyValue(ring.rotateProperty(), 0);
        KeyValue kv1 = new KeyValue(ring.rotateProperty(), 360);

        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(1), kv1);

        timeline.getKeyFrames().setAll(kf0, kf1);

        ring.setLength(300);
        ring.setStroke(getWaitingColor());

        if (isPlayButtonVisible()) { disableNode(playButton); }
        stopButton.setFill(getWaitingColor());
        enableNode(stopButton);

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        state = State.WAITING;
        fireTimerEvent(WAITING);
    }
    
    private void finished() {
        timeline.stop();
        timeline.setCycleCount(1);

        ring.setLength(360);
        setProgress(0);
        currentDuration = Duration.ZERO;

        disableNode(stopButton);
        if (isPlayButtonVisible()) { enableNode(playButton); }

        state = State.STOPPED;
        
        
    }
    
    public void stopFromClick () {
    	currentDuration = state == State.WAITING ? Duration.ZERO : timeline.getCurrentTime();
        timeline.stop();
        timeline.setCycleCount(1);

        ring.setLength(360);
        ring.setStroke(getColor());

        disableNode(stopButton);
        stopButton.setFill(getColor());
        if (isPlayButtonVisible()) { enableNode(playButton); }

        state = State.STOPPED;
        fireTimerEvent(STOPPED);
        
        BorderPane root = new BorderPane();
        
        root = Main.getRoot();
        System.out.println(root.getCenter());
        
        StackPane stack = new StackPane();
        stack = (StackPane) root.getCenter();
        
        // O Pai é o componente PartidaTelaComponent
        System.out.println("O pai eh: " + this.getParent().getParent().getParent());
        
        JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(new Text("Jogo Pausado"));
		dialogContent.setBody(new Text("Voltar a partida?"));
		JFXButton confirmar = new JFXButton("Confimar");
		confirmar.setButtonType(JFXButton.ButtonType.RAISED);
		confirmar.setStyle("-fx-background-color: #1ab3ce;");
		confirmar.setTextFill(Color.WHITE);
		dialogContent.setActions(confirmar);
		dialogContent.setStyle("-fx-font-size: 14.0px;");
		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
		confirmar.setOnAction((ActionEvent e) -> {
			dialog.close();
			this.getParent().getParent().getParent().setEffect(null);
		});
		dialog.show();
		
		dialog.setOnDialogClosed(Event -> {
			this.getParent().getParent().getParent().setEffect(null);
			startFromCurrent();
		});
		
		BoxBlur blur = new BoxBlur(3, 3, 3);
		this.getParent().getParent().getParent().setEffect(blur);
        
    }
    
    public void resetFromTimesUp (Stage stage, MediaPlayer mediaPlayer) {
    	finishedFromTimesUp(stage, mediaPlayer);
    	fireTimerEvent(RESET);
    }
    
    private void finishedFromTimesUp(Stage stage, MediaPlayer mediaPlayer) {
    	System.out.println("Estou entrando aqui, viu?");

        StackPane stack = Main.getStack();

        StackPane main = new StackPane();

		Label lblRecompensa = new Label("Ver Pontuacao Obtida");
		lblRecompensa.setStyle("-fx-text-fill: white");
		
        StackPane colorPane = new StackPane();
        colorPane.setStyle("-fx-background-radius:50; -fx-min-width:50; -fx-min-height:50;");
        colorPane.getStyleClass().add("blue-500");
        colorPane.getChildren().add(lblRecompensa);
        main.getChildren().add(colorPane);

        StackPane wizard = new StackPane();
        wizard.getChildren().add(main);
        
        StackPane nextPage = new StackPane();

        ImageView medalImage = new ImageView("/resources/images/medal.png");
        medalImage.setFitWidth(160);
        medalImage.setPreserveRatio(true);
        
        Button btnPontuacao = new Button();
		btnPontuacao.getStyleClass().add("pontuacaoIcon");
		btnPontuacao.setStyle("-width: 45; -height: 65; -fx-min-height: -height;\r\n" + 
				"    -fx-min-width: -width;\r\n" + 
				"    -fx-max-height: -height;\r\n" + 
				"    -fx-max-width: -width;");
		
		Label lblPont = new Label(String.valueOf(InfoPartida.alunoPontuacao));
		lblPont.setStyle("-fx-font-size: 80px; -fx-font-family: \"Yu Gothic UI Semilight\";");
		
		HBox hboxPontuacao = new HBox();
        hboxPontuacao.getChildren().addAll(btnPontuacao, lblPont);
        hboxPontuacao.setAlignment(Pos.CENTER);
        hboxPontuacao.setMargin(btnPontuacao, new Insets(0, 30, 0, 0));
		
		VBox recompensas = new VBox();
		recompensas.getChildren().addAll(hboxPontuacao);
        
//		JFXButton btnJogarNovamente = new JFXButton("Jogar Novamente");
//		btnJogarNovamente.setButtonType(JFXButton.ButtonType.RAISED);
//		btnJogarNovamente.setStyle("-fx-background-color: #1ab3ce;");
//		btnJogarNovamente.setTextFill(Color.WHITE);
		
		JFXButton btnVoltarMenuPrincipal = new JFXButton("Voltar ao Menu Principal");
        SVGGlyph glyphOpcaoMenuPrincipal = new SVGGlyph(-1,
				"test",
				"M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z ",
				Color.valueOf("#b0b0b0"));
        glyphOpcaoMenuPrincipal.setSize(18, 18);
        btnVoltarMenuPrincipal.setButtonType(JFXButton.ButtonType.RAISED);
        btnVoltarMenuPrincipal.setGraphic(glyphOpcaoMenuPrincipal);
        
		HBox hboxBtnsAcoes = new HBox();
		hboxBtnsAcoes.getChildren().addAll(btnVoltarMenuPrincipal);
		hboxBtnsAcoes.setAlignment(Pos.CENTER);
		
		VBox vboxFimJogo = new VBox();
		vboxFimJogo.setAlignment(Pos.CENTER);
		vboxFimJogo.setStyle("-fx-background-color: white");
        vboxFimJogo.setPrefWidth(1000);
        vboxFimJogo.setPrefHeight(700);
        
        vboxFimJogo.getChildren().addAll(medalImage, recompensas, hboxBtnsAcoes);
		
        nextPage.getChildren().add(vboxFimJogo);
        
        StackPane.setAlignment(vboxFimJogo, Pos.CENTER);
        StackPane.setMargin(vboxFimJogo, new Insets(-300, -200, 0, -200));

        JFXNodesAnimation<StackPane, StackPane> animation = 
        		new FlowPaneStackPaneJFXNodesAnimation(main, nextPage, wizard, colorPane);

        colorPane.setOnMouseClicked((click) -> animation.animate());
        
        // StackPane.setMargin(main, new Insets(100));
        wizard.setStyle("-fx-background-color:WHITE");
		
        VBox vboxImage = new VBox();
        vboxImage.setAlignment(Pos.CENTER);
        vboxImage.setStyle("-fx-background-color: transparent;");
		
		ImageView loseImage = new ImageView("/resources/images/timesupImage.png");
		loseImage.setFitWidth(400);
		loseImage.setPreserveRatio(true);
		
		vboxImage.getChildren().addAll(loseImage);

		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setHeading(vboxImage);
		// dialogContent.setBody(new Text("Deseja realmente abandonar a partida atual?"));
		dialogContent.setBody(wizard);
		
		dialogContent.setStyle("-fx-font-size: 14.0px;");
		
		JFXDialog dialog = new JFXDialog(stack, dialogContent, JFXDialog.DialogTransition.CENTER);
		dialog.setOverlayClose(false);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb/db/Usuarios.odb");
		EntityManager em = emf.createEntityManager();
		
		Aluno aluno = new Aluno(
				UsuarioAtual.getUsuarioAtualAluno().getPontuacao() +
				InfoPartida.alunoPontuacao, 
				UsuarioAtual.getUsuarioAtualAluno().getBitcoins()
								);
		
		AlunoDAO alunoDAO = new AlunoDAO();
		
		// Reseta a pontuacao porque acabou a partida
		InfoPartida.alunoPontuacao = 0;
		InfoPartida.questaoAtual = 1;
		InfoPartida.qtdVidasAtual = Configuracoes.qtdVidas;
		
		// Atualiza no banco
		alunoDAO.atualizaDados(em, UsuarioAtual.getUsuarioAtualAluno(), aluno);
		
		// Atualiza na interface
		EntityManager emAtualiza = emf.createEntityManager();
		UsuarioAtual.setUsuarioAtualAluno(alunoDAO.getAlunosPorId(emAtualiza, UsuarioAtual.getUsuarioAtualAluno().getId()));
		
		btnVoltarMenuPrincipal.setOnAction((ActionEvent event) -> {
			dialog.close();
			
			Main.getStack().getChildren().remove(0); // Remove o component de partida
			Main.getStack().getChildren().remove(0); // Remove o dialog
			
			MenuAlunoComponent menuAluno = new MenuAlunoComponent(stage, mediaPlayer);
				
			Main.getStack().getChildren().add(menuAluno);
			
			stack.getChildren().get(0).setEffect(null);
			// this.setEffect(null);
		});
		
		dialog.show();
		
		dialog.setOnDialogClosed(Event -> {
			// this.setEffect(null);
		});
		
		BoxBlur blur = new BoxBlur(3, 3, 3);
		stack.getChildren().get(0).setEffect(blur);
	}
    
    private static final class FlowPaneStackPaneJFXNodesAnimation extends JFXNodesAnimation<StackPane, StackPane> {
        private final Pane tempPage;
        private final StackPane main;
        private final StackPane nextPage;
        private final StackPane wizard;
        private final StackPane colorPane1;

        private double newX;
        private double newY;

        FlowPaneStackPaneJFXNodesAnimation(final StackPane main, final StackPane nextPage, final StackPane wizard,
                                           final StackPane colorPane1) {
            super(main, nextPage);
            this.main = main;
            this.nextPage = nextPage;
            this.wizard = wizard;
            this.colorPane1 = colorPane1;
            tempPage = new Pane();
            newX = 0;
            newY = 0;
        }

        @Override
        public void init() {
            nextPage.setOpacity(0);
            
            if (!wizard.getChildren().contains(tempPage)) {
            	wizard.getChildren().add(tempPage);
            }
            
            if (!wizard.getChildren().contains(nextPage)) {
            	wizard.getChildren().add(nextPage);
            }
            
            newX = colorPane1.localToScene(colorPane1.getBoundsInLocal()).getMinX();
            newY = colorPane1.localToScene(colorPane1.getBoundsInLocal()).getMinY();
            
            if (!tempPage.getChildren().contains(colorPane1)) {
            	tempPage.getChildren().add(colorPane1);
            }
            
            colorPane1.setTranslateX(newX);
            colorPane1.setTranslateY(newY);
            
            if (!colorPane1.getChildren().isEmpty()) {
            	colorPane1.getChildren().remove(0);
            }
            
            // Isso previne que o evento possa ser disparado novamente
            colorPane1.setDisable(true);
        }

        @Override
        public void end() {

        }

        @Override
        public Animation animateSharedNodes() {
            return new Timeline();
        }

        @Override
        public Animation animateExit() {
            final Integer endValue = 0;
            return new Timeline(
                new KeyFrame(Duration.millis(300),
                             new KeyValue(main.opacityProperty(), endValue, EASE_BOTH)),
                new KeyFrame(Duration.millis(520),
                             new KeyValue(colorPane1.translateXProperty(), endValue, EASE_BOTH),
                             new KeyValue(colorPane1.translateYProperty(), endValue, EASE_BOTH)),
                new KeyFrame(Duration.millis(200),
                             new KeyValue(colorPane1.scaleXProperty(), 1, EASE_BOTH),
                             new KeyValue(colorPane1.scaleYProperty(), 1, EASE_BOTH)),
                new KeyFrame(Duration.millis(1000),
                             new KeyValue(colorPane1.scaleXProperty(), 40, EASE_BOTH),
                             new KeyValue(colorPane1.scaleYProperty(), 40, EASE_BOTH)));
        }

        @Override
        public Animation animateEntrance() {
            return new Timeline(new KeyFrame(Duration.millis(320),
                                             new KeyValue(nextPage.opacityProperty(), 1, EASE_BOTH)));
        }
    }

    private double clamp(final double min, final double max, final double value) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    private void enableNode(final Node NODE) {
        NODE.setManaged(true);
        NODE.setVisible(true);
    }
    private void disableNode(final Node NODE) {
        NODE.setVisible(false);
        NODE.setManaged(false);
    }


    // ******************** EventHandling *************************************
    public void setOnTimerEvent(final TimerEventListener LISTENER) { addTimerEventListener(LISTENER); }
    public void addTimerEventListener(final TimerEventListener LISTENER) { if (!listenerList.contains(LISTENER)) listenerList.add(LISTENER); }
    public void removeTimerEventListener(final TimerEventListener LISTENER) { if (listenerList.contains(LISTENER)) listenerList.remove(LISTENER); }

    public void fireTimerEvent(final TimerEvent EVENT) {
        for (TimerEventListener listener : listenerList) {  
        	listener.onTimerEvent(EVENT);
        	
        }
    }


    // ******************** Resizing ******************************************
    private void resize() {
        width   = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height  = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size    = width < height ? width : height;
        centerX = size * 0.5;
        centerY = size * 0.5;

        if (width > 0 && height > 0) {
            pane.setMaxSize(size, size);
            pane.setPrefSize(size, size);
            pane.relocate((getWidth() - size) * 0.5, (getHeight() - size) * 0.5);

            ring.setCenterX(centerX);
            ring.setCenterY(centerY);
            ring.setRadiusX(centerX);
            ring.setRadiusY(centerY);
            ring.setStrokeWidth(size * 0.05263158);

            progressBar.setCenterX(centerX);
            progressBar.setCenterY(centerY);
            progressBar.setRadiusX(size * 0.44736842);
            progressBar.setRadiusY(size * 0.44736842);
            progressBar.setStrokeWidth(size * 0.10526316);

            stopButton.setWidth(size * 0.26315789);
            stopButton.setHeight(size * 0.26315789);
            stopButton.relocate((centerX - size * 0.13157895), (centerY - size * 0.13157895));

            playButtonP1.setX(size * 0.36842105);
            playButtonP1.setY(size * 0.26315789);
            playButtonP2.setX(size * 0.73684211);
            playButtonP2.setY(size * 0.5);
            playButtonP3.setX(size * 0.36842105);
            playButtonP3.setY(size * 0.73684211);

            redraw();
        }
    }

    private void redraw() {
        ring.setFill(getBackgroundColor());
        ring.setStroke(state == State.WAITING ? getWaitingColor() : getColor());
        progressBar.setFill(getBackgroundColor());
        progressBar.setStroke(getColor());
        stopButton.setFill(state == State.WAITING ? getWaitingColor() : getColor());
        playButton.setFill(getColor());
        pane.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(borderPaint, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidth / PREFERRED_WIDTH * size))));
    }
}
