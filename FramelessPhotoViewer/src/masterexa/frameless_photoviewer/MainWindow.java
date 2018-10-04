package masterexa.frameless_photoviewer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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

			static String getExtension(File file)
			{
				String name = file.getName();
				int i = name.lastIndexOf('.');

				return (i>0) ? name.substring(i+1) : "";
			}


	/* Instance */
		/* Fields */
			private Stage	stage;
			private Pane	root;
			private Scene	scene;
			private FrameMoveControl	moveControl = new FrameMoveControl();

			private HashMap<String,FileLoadHandler>	loaders = new HashMap<>();
			private FileChooser	fileChooser = new FileChooser();


		/* Methods */
			public void initController(Stage stage, Pane root){

				this.stage 	= stage;
				this.root	= root;
				this.scene	= new Scene(this.root);

				moveControl.setStage(stage);
				moveControl.addEventToNode(root);

				// Apply Styles
				stage.initStyle(StageStyle.TRANSPARENT);
				scene.getStylesheets().add(Main.getMainCSS());
				scene.setFill(new Color(1,1,1,0.01));

				// Show Window
				stage.setTitle("Frameless Photo Viewer");
				stage.setScene(scene);
				stage.show();
			}

			public void addFileLoader(FileLoadHandler handler, String ... exts)
			{
				for(String it : exts)
				{
					loaders.put(it, handler);
				}
			}

			public ObservableList<FileChooser.ExtensionFilter> getChooserExtensionFilters()
			{
				return fileChooser.getExtensionFilters();
			}


		/* Inner Methods */
			private void openFiles(List<File> files)
			{
				for(File it : files )
				{
					FileLoadHandler loader = loaders.get( getExtension(it) );
					if( Objects.nonNull(loader) )
					{
						loader.loadFromFile(it);
					}
				}
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
			public void onPressOpen()
			{
				List<File> files = fileChooser.showOpenMultipleDialog(stage);
				if( Objects.nonNull(files) )
				{
					openFiles( files );
				}
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
					openFiles(board.getFiles());
					success = true;
				}

				ev.setDropCompleted(success);
				ev.consume();
			}
}
