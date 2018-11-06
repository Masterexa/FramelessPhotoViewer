package masterexa.frameless_photoviewer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class PhotoEntity extends StageEntity {

	/* Inner Class */
		public static class FileLoader implements FileLoadHandler{

			@Override
			public StageEntity loadFromFile(File file) {

				PhotoEntity entity = null;

				try {
					entity = new PhotoEntity();
					entity.setImage(new Image(file.toURI().toURL().toExternalForm()), file.getName());

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				return entity;
			}
		}


	/* Instance */
		/* Fields */
			StageFrameView	frameView;
			PhotoSceneView	photoSceneView;
			AnchorPane		photoRoot;

			final ObjectProperty<TrimmingInfo>	trimmingInfo	= new SimpleObjectProperty<>(new TrimmingInfo());
			final BooleanProperty				trimmingEnabled = new SimpleBooleanProperty(false);
			double imageAspect = 1.0;

		/* Events */
			ChangeListener<Boolean> onChangeEnableDelegate = (ob,oldVal,newVal)->
			{
				if( Objects.isNull(frameView) )
				{
					return;
				}


				if( newVal )
				{
					frameView.getFrameMoveControl().removeEventFromNode(photoRoot);
				}
				else{
					trimmingInfo.get().setInitialValue();

					FrameResizeControl	resizeCtrl	= frameView.getFrameResizeControl();
					double				height		= getStage().getHeight();
					resizeCtrl.setFrameSize(height*imageAspect, height);

					frameView.getFrameMoveControl().addEventToNode(photoRoot);
				}
			};


		/* Inits */
			public PhotoEntity() throws IOException
			{
				super();
				OptionToggleButton	toggle =  OptionToggleButton.create( new Image(Main.class.getResource("assets/images/trimming_icon.png").toExternalForm()) );
				FXMLLoader			loader = new FXMLLoader(PhotoSceneView.class.getResource("PhotoScene.fxml"));
				trimmingInfo.get().enabledProperty().bindBidirectional(trimmingEnabled);


				// Setup Inner Scene
				loader.load();
				photoRoot 		= loader.getRoot();
				photoSceneView 	= loader.getController();
				photoRoot.setBackground(null);
				photoSceneView.trimmingInfoProperty().bind(trimmingInfo);

				// Setup Frame
				frameView = StageFrameView.makeStageFrame(getStage());
				frameView.getFrameMoveControl().addEventToNode(photoRoot);
				frameView.getFrameResizeControl().keepAspectProperty().bind(trimmingEnabled.not());
				frameView.setOuterScene(photoRoot);

				// Setup trimming settings
				trimmingEnabled.addListener(onChangeEnableDelegate);
				frameView.getOptionButtons().add(toggle.getToggleButton());
				trimmingEnabled.bind( toggle.getToggleButton().selectedProperty() );


				getStage().show();
			}

		/* Methods */
			public void setImage(Image image, String title)
			{
				photoSceneView.imageProperty().set(image);
				getStage().setTitle(title);

				imageAspect = image.getWidth()/image.getHeight();
				frameView.getFrameResizeControl().setFrameSize(400.0*imageAspect, 400.0);
			}
}
