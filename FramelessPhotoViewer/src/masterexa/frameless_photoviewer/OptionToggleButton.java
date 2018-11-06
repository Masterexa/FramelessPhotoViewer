package masterexa.frameless_photoviewer;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OptionToggleButton {

	/* Class */
		/* Methods */
			public static OptionToggleButton create(Image image)
			{
				return new OptionToggleButton(image);
			}

	/* Instance */
		/* Fields */
			final ToggleButton	toggleButton;
			final ImageView		imageView;

		/* getters */
			ToggleButton getToggleButton()
			{
				return toggleButton;
			}

			ImageView getImageView()
			{
				return imageView;
			}


		/* Inits */
			private OptionToggleButton(Image image)
			{
				toggleButton 	= new ToggleButton();
				imageView		= new ImageView();

				imageView.setPreserveRatio(true);
				imageView.setSmooth(true);
				imageView.setFitHeight(18.0);
				imageView.setImage(image);

				toggleButton.getStyleClass().add("StageFrame");
				toggleButton.setGraphic(imageView);
			}
}
