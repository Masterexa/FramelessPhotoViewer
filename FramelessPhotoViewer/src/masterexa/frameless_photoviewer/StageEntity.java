package masterexa.frameless_photoviewer;

import javafx.stage.Stage;

public class StageEntity {

	/* Instance */
		/* Fields */
			private Stage	stage;

		/* Accesser */
			public Stage getStage()
			{
				return stage;
			}

		/* Inits */
			public StageEntity(){
				stage = new Stage();
			}
}
