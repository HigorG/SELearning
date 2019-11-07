package component.util;

import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CustomAvatarSelector extends VBox {
	private static final double TAM_IMAGEM = 36;
	private static final String PREFIXO = "Icon";
	private static final String SUFIXO = ".png";
	
	private static final ObservableList<String> avatars = FXCollections.unmodifiableObservableList(
		FXCollections.observableArrayList(
			"01",
			"02",
			"03",
			"04"
		)
	);
	
	private Map<String, Image> avatarCollection;
	
	public CustomAvatarSelector () {
		avatarCollection = avatars.stream().collect(
			Collectors.toMap(
				avatar -> avatar,
				avatar -> new Image(
					constructLabel("/resources/images/avatars/" + PREFIXO, avatar,SUFIXO),
									0,
									TAM_IMAGEM,
									true,
									true
								)
				)
		);
		
		ListView<String> avatarList = new ListView<>(avatars);
		avatarList.setCellFactory(p -> new AvatarCell());
		
		/*
		avatarList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
				@Override
				public ListCell<String> call(ListView<String> list) {
					ListCell<String> cell = new ListCell<String>() {
						private ImageView imageView = new ImageView();
						
						@Override
						protected void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							
							if (empty || item == null) {
								imageView.setImage(null);
								setGraphic(null);
								setText(null);
							} else {
								imageView.setImage(avatarCollection.get(item));
									
								setText(constructLabel(PREFIXO, item, SUFIXO));
								setGraphic(imageView);
							}
						}
			        };

			        System.out.println("A");
			        return cell;
				}
		});
		*/
		
		avatarList.setPrefWidth(230);
		avatarList.setPrefHeight(200);
			
		avatarList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println(PREFIXO + avatarList.getSelectionModel().getSelectedItem() + SUFIXO);
			}
		});
		
		this.getChildren().add(avatarList);
		this.setPadding(new Insets(10));
	}
	
	private class AvatarCell extends ListCell<String> {
		private ImageView imageView = new ImageView();
		
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			
			if (empty || item == null) {
				imageView.setImage(null);
				setGraphic(null);
				setText(null);
			} else {
				imageView.setImage(avatarCollection.get(item));
				
				setText(constructLabel(PREFIXO, item, SUFIXO));
				setGraphic(imageView);
			}
		}
	}
	
	private String constructLabel(String prefix, String avatar, String suffix) {
		return (prefix != null ? prefix : "") + avatar + (suffix != null ? suffix : "");
	}
}
