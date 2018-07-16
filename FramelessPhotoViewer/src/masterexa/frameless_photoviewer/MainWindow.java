package masterexa.frameless_photoviewer;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow {

	/* Class */
		/* Methods */
			public static MainWindow createWindow(Stage primaryStage) throws Exception
			{
				FXMLLoader	loader	= new FXMLLoader(Main.class.getResource("MainWindow.fxml"));
				loader.load();

				// Init Controller
				MainWindow controller = (MainWindow)loader.getController();
				controller.initController(primaryStage, loader.getRoot());

				return controller;
			}


	/* Instance */
		/* Fields */
			private double	offsetX = 0;
			private double	offsetY = 0;

			private Stage	stage;
			private Pane	root;
			private Scene	scene;

		/* Methods */
			public void initController(Stage stage, Pane root){

				this.stage 	= stage;
				this.root	= root;
				this.scene	= new Scene(this.root);

				// Apply Styles
				stage.initStyle(StageStyle.TRANSPARENT);
				scene.getStylesheets().add(Main.getMainCSS());
				scene.setFill(new Color(1,1,1,0.01));

				// Show Window
				stage.setTitle("Frameless Photo Viewer");
				stage.setScene(scene);
				stage.show();
			}

		/* Events */
			@FXML
			public void onMinimum(){

				stage.setIconified(true);
			}

			@FXML
			public void onClose(){

				Platform.exit();
			}

			@FXML
			public void onPressOpen(){
				PhotoWindow.createWindowFromChooser(null);
			}

			@FXML
			public void onFileDragOver(DragEvent ev){

				if( ev.getDragboard().hasFiles() )
				{
					ev.acceptTransferModes(TransferMode.LINK);
				}
				ev.consume();
			}

			@FXML
			public void onFileDropped(DragEvent ev){

				Dragboard 	board 	= ev.getDragboard();
				boolean		success	= false;

				if( board.hasFiles() )
				{
					for(File it : board.getFiles() )
					{
						PhotoWindow.createWindow(it,null);
					}
					success = true;
				}

				ev.setDropCompleted(success);
				ev.consume();
			}

			@FXML
			public void onMoveStart(MouseEvent ev){
				offsetX = ev.getSceneX();
				offsetY = ev.getSceneY();
			}

			@FXML
			public void onMoving(MouseEvent ev)
			{
				stage.setX(ev.getScreenX() - offsetX);
				stage.setY(ev.getScreenY() - offsetY);
			}
}
