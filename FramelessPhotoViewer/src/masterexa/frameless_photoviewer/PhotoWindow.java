package masterexa.frameless_photoviewer;

import java.io.File;
import java.util.HashSet;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class PhotoWindow{



	/* Class */
		/* Fields */
			static HashSet<PhotoWindow>	s_windows = new HashSet<>();
			static FileChooser	s_fileChooser;

		/* Methods */
			public static PhotoWindow createWindowFromChooser(Stage owner){

				if( s_fileChooser==null )
				{
					s_fileChooser = new FileChooser();
					s_fileChooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif")
					);
				}

				File file = s_fileChooser.showOpenDialog(owner);
				if( file!=null )
				{
					s_fileChooser.setInitialDirectory(file.getParentFile());
				}

				return createWindow(file, owner);
			}

			public static PhotoWindow createWindow(File file, Stage owner)
			{
				if( file==null )
				{
					return null;
				}
				PhotoWindow	controller = null;


				try {
					FXMLLoader	loader	= new FXMLLoader(Main.class.getResource("PhotoWindow.fxml"));
					loader.load();

					// Initialize controller
					controller = (PhotoWindow)loader.getController();
					controller.initPhotoWindow(loader.getRoot(), owner);
					controller.setImage(file);

				} catch(Exception e) {
					e.printStackTrace();
				}

				return controller;
			}

			public static void closeAll()
			{
				for(PhotoWindow it : s_windows)
				{
					it.close();
				}
			}




	/* Instance */
		/* Fields */
			private Stage	stage;
			private Pane	root;
			private Scene	scene;

			private double	offsetX = 0;
			private double	offsetY = 0;

			private SimpleDoubleProperty	aspectProperty = new SimpleDoubleProperty();
			private DoubleBinding			dropModeHeightScale;
			private SimpleDoubleProperty	dropModeQueryWidth = new SimpleDoubleProperty();

			private boolean trimModeEnabled = false;

			private boolean			borderDragging = false;
			private FadeTransition	borderFadein;
			private FadeTransition	borderFadeout;

			private ParallelTransition	trimModeExitAnimation;


			@FXML
			private ImageView	imageView;
			@FXML
			private AnchorPane	imagePane;
			@FXML
			private Pane		seResizePane;
			@FXML
			private CheckBox	viewerModeCheck;
			@FXML
			private Rectangle	borderRect;
			@FXML
			private Pane		controlsPane;
			@FXML
			private ToggleButton	frontPinToggle;

			@FXML
			private Pane	resizePaneNW;
			@FXML
			private Pane	resizePaneNE;
			@FXML
			private Pane	resizePaneSE;
			@FXML
			private Pane	resizePaneSW;
			@FXML
			private Pane	resizePaneN;
			@FXML
			private Pane	resizePaneS;
			@FXML
			private Pane	resizePaneW;
			@FXML
			private Pane	resizePaneE;



		/* Events */
			@FXML
			public void onZooming(ScrollEvent ev) {

				if( this.trimModeEnabled )
				{
					double delta = ev.getDeltaY() * 0.001;

					imageView.setScaleX(imageView.getScaleX() + delta);
					imageView.setScaleY(imageView.getScaleY() + delta);
				}
			}

			@FXML
			public void onResizeStart(MouseEvent ev){

				onFocusEnter(ev);
				this.borderDragging = true;

				offsetX = ev.getScreenX();
				offsetY = ev.getScreenY();
			}

			@FXML
			public void onResizing(MouseEvent ev){

				boolean scaleHeight = false;
				double top		= stage.getY();
				double left		= stage.getX();
				double bottom 	= stage.getY() + stage.getHeight();
				double right	= stage.getX() + stage.getWidth();

				double deltaX	= ev.getScreenX() - offsetX;
				double deltaY	= ev.getScreenY() - offsetY;
				offsetX = ev.getScreenX();
				offsetY = ev.getScreenY();

				if( ev.getTarget()==resizePaneW || ev.getTarget()==resizePaneNW || ev.getTarget()==resizePaneSW )
				{
					left += deltaX;
				}
				else if( ev.getTarget()==resizePaneE || ev.getTarget()==resizePaneNE || ev.getTarget()==resizePaneSE )
				{
					right += deltaX;
				}

				if( ev.getTarget()==resizePaneN || ev.getTarget()==resizePaneNW || ev.getTarget()==resizePaneNE )
				{
					top += deltaY;
					scaleHeight = true;
				}
				else if( ev.getTarget()==resizePaneS || ev.getTarget()==resizePaneSW || ev.getTarget()==resizePaneSE )
				{
					bottom += deltaY;
					scaleHeight = true;
				}


				setRect(top, bottom, left, right, scaleHeight);
			}

			public void onResizeExit(MouseEvent ev){

				this.borderDragging = false;
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

			@FXML
			public void onImageDragged(MouseEvent ev)
			{
				if( this.trimModeEnabled )
				{
					this.imageView.setTranslateX( imageView.getTranslateX() + (ev.getSceneX()-offsetX) );
					this.imageView.setTranslateY( imageView.getTranslateY() + (ev.getSceneY()-offsetY) );

					offsetX = ev.getSceneX();
					offsetY = ev.getSceneY();
				}
				else{
					onMoving(ev);
				}
			}

			@FXML
			public void onFocusEnter(MouseEvent ev)
			{
				if( !borderDragging )
				{
					borderFadeout.stop();
					borderFadein.play();
				}
			}

			@FXML
			public void onFocusExit(MouseEvent ev)
			{
				if( !borderDragging )
				{
					borderFadein.stop();
					borderFadeout.playFromStart();
				}
			}

			@FXML
			public void onChangeViewerMode(ActionEvent ev)
			{
				setViewerMode(viewerModeCheck.isSelected());
			}

			@FXML
			public void onFrontPinAction(ActionEvent ev){

				stage.setAlwaysOnTop(frontPinToggle.isSelected());
			}

			@FXML
			public void onClose(){
				close();
			}



		/* Methods */
			public void setRect(double top, double bottom, double left, double right, boolean heightScaling){

				double width	= right-left;
				double height	= bottom-top;

				stage.setX(left);
				stage.setY(top);
				if( trimModeEnabled )
				{
					stage.setWidth(width);
					stage.setHeight(height);
				}
				else{
					if( heightScaling )
					{
						width = height*aspectProperty.doubleValue();
					}
					stage.setWidth(width);
				}
			}

			public void resize(double w, double h)
			{
				stage.setWidth(w);
				stage.setHeight(h);
			}

			public void correctAspect(){
				Image image = imageView.getImage();

				double	aspect = (image!=null) ? (image.getWidth() / image.getHeight()) : 1.0;
				boolean	imageLongerWidth = aspect > 1.0;

				double base = imageLongerWidth ? stage.getWidth() : (stage.getHeight()*aspect);
				double w	= base;
				double h	= (base/aspect);
				resize(w, h);

				aspectProperty.set( aspect );
			}

			public void setImage(File file)
			{
				if( file==null )
				{
					return;
				}

				imageView.setImage( new Image("file:" + file.getAbsolutePath()) );
				this.stage.setTitle(file.getName());

				correctAspect();
			}

			public void setViewerMode(boolean b)
			{
				this.trimModeEnabled = b;

				if( b )
				{
					imageView.fitWidthProperty().unbind();
					stage.maxHeightProperty().unbind();
					stage.minHeightProperty().unbind();
				}
				else{
					correctAspect();
					imageView.fitWidthProperty().bind(imagePane.widthProperty());
					stage.maxHeightProperty().bind( dropModeHeightScale );
					stage.minHeightProperty().bind( dropModeHeightScale );

					trimModeExitAnimation.playFromStart();
				}
			}


			public void initPhotoWindow(Pane root, Stage owner) throws Exception{

				this.stage 	= new Stage();
				this.root	= root;
				this.scene	= new Scene(this.root);

				if( owner!=null )
					stage.initOwner(owner);


				// Apply styles
				resize(600, 600);
				stage.initStyle(StageStyle.TRANSPARENT);
				scene.getStylesheets().add(Main.getMainCSS());
				scene.setFill(new Color(1.0, 1.0, 1.0, 0.01));

				// Setup Border
				Rectangle rc = new Rectangle();
				rc.widthProperty().bind(imagePane.widthProperty());
				rc.heightProperty().bind(imagePane.heightProperty());
				imagePane.setClip(rc);
				// Binding
				borderRect.widthProperty().bind(imagePane.widthProperty());
				borderRect.heightProperty().bind(imagePane.heightProperty());

				// prop
				aspectProperty.set(1.0);
				dropModeHeightScale = stage.widthProperty().divide(aspectProperty);

				this.borderFadein = new FadeTransition(Duration.seconds(0.2), controlsPane);
				this.borderFadein.fromValueProperty().bind( controlsPane.opacityProperty() );
				this.borderFadein.setToValue(1);
				this.borderFadein.setCycleCount(1);

				this.borderFadeout = new FadeTransition(Duration.seconds(0.5), controlsPane);
				this.borderFadeout.fromValueProperty().bind( controlsPane.opacityProperty() );
				this.borderFadeout.setToValue(0);
				this.borderFadeout.setCycleCount(1);

				{
					Duration			dur			= Duration.seconds(0.15);
					TranslateTransition	translate	= new TranslateTransition(dur);
					ScaleTransition		scale		= new ScaleTransition(dur);

					translate.fromXProperty().bind( imageView.translateXProperty() );
					translate.fromYProperty().bind( imageView.translateYProperty() );
					translate.setToX(0);
					translate.setToY(0);

					scale.fromXProperty().bind( imageView.scaleXProperty() );
					scale.fromYProperty().bind( imageView.scaleYProperty() );
					scale.setToX(1);
					scale.setToY(1);

					trimModeExitAnimation = new ParallelTransition(imageView, translate, scale);
				}


				setViewerMode(false);
				s_windows.add(this);
				stage.setScene(scene);
				stage.show();
			}

			public void close(){
				stage.close();
				s_windows.remove(this);
			}
}
