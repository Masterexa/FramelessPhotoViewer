package masterexa.frameless_photoviewer;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class PhotoSceneView implements Initializable {


	/* Instance */
		/* FXMLs */
			@FXML
			AnchorPane	root;
			@FXML
			ImageView	imageView;

		/* Fields */
			final ObjectProperty<TrimmingInfo>	trimmingInfo = new SimpleObjectProperty<>();
			final TrimmingAnimation				trimmingAnimation = new TrimmingAnimation();
			final TrimmingGesture				trimmingGesture = new TrimmingGesture();

		/* Properties */
			public ObjectProperty<Image>	imageProperty()
			{
				return imageView.imageProperty();
			}

			public ObjectProperty<TrimmingInfo>	trimmingInfoProperty()
			{
				return trimmingInfo;
			}


		/* Events */
			final ChangeListener<Boolean> onChangeEnabledDelegate = (ob,oldVal,newVal)->
			{
				if( newVal )
				{
					imageView.fitWidthProperty().unbind();
				}
				else{
					imageView.fitWidthProperty().bind( root.widthProperty() );
				}
			};

			final ChangeListener<TrimmingInfo> onChangeTrimmingInfoDelegate = (ob,oldVal,newVal)->
			{
				if( Objects.nonNull(oldVal) )
				{
					oldVal.enabledProperty().removeListener(onChangeEnabledDelegate);
				}

				if( Objects.nonNull(newVal) )
				{
					newVal.enabledProperty().addListener(onChangeEnabledDelegate);
				}
			};


			@Override
			public void initialize(URL location, ResourceBundle resources) {

				imageView.fitWidthProperty().bind( root.widthProperty() );
				trimmingInfo.addListener(onChangeTrimmingInfoDelegate);

				trimmingGesture.addEventToNode(imageView);
				trimmingGesture.trimmingInfoProperty().bind(trimmingInfo);

				trimmingAnimation.setNode(imageView);
				trimmingAnimation.trimmingInfoProperty().bind(trimmingInfo);
				trimmingAnimation.play();
			}
}
