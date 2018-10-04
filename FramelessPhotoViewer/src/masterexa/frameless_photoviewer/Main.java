package masterexa.frameless_photoviewer;

import javafx.application.Application;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class Main extends Application {

	/* Class */
		/* Fields */
			static String	s_css;
			public static String getMainCSS()
			{
				return s_css;
			}

		/* Methods */
			public static void main(String[] args) {
				launch(args);
			}



	/* Instance */
		/* Fields */
			private MainWindow controller;

		/* Methods */
			@Override
			public void start(Stage primaryStage) {

				try {
					// Load CSS
					s_css = getClass().getResource("application.css").toExternalForm();

					// Create MainWindow
					controller = MainWindow.createWindow(primaryStage);

					// Register PhotoEntity
					controller.addFileLoader(new PhotoEntity.FileLoader(), "bmp", "gif", "jpg", "jpeg", "png");
					controller.getChooserExtensionFilters().add(new ExtensionFilter("Images", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"));


				} catch(Exception e) {
					e.printStackTrace();
				}
			}
}
