package masterexa.frameless_photoviewer;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FrameMoveControl {

	/* Instance */
		/* Fields */
			private Stage	stage;
			private double	offsetX;
			private double	offsetY;


		/* Events */
			final EventHandler<MouseEvent> mouseEventDelegate = (ev)->
			{
				if( ev.getEventType()==MouseEvent.MOUSE_PRESSED )
				{
					offsetX = ev.getSceneX();
					offsetY = ev.getSceneY();
				}
				else if( ev.getEventType()==MouseEvent.MOUSE_DRAGGED )
				{
					stage.setX(ev.getScreenX() - offsetX);
					stage.setY(ev.getScreenY() - offsetY);
				}
			};


		/* Methods */
			public void setStage(Stage stage)
			{
				this.stage = stage;
			}

			public void addEventToNode(Node node)
			{
				node.addEventHandler(MouseEvent.ANY, mouseEventDelegate);
			}

			public void removeEventFromNode(Node node)
			{
				node.removeEventHandler(MouseEvent.ANY, mouseEventDelegate);
			}
}
