package masterexa.frameless_photoviewer;

import java.util.Objects;

import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class NodeFader {

	/* Instance */
		/* Fields */
			final BooleanProperty			enabled = new SimpleBooleanProperty(true);
			final ObjectProperty<Duration>	duration = new SimpleObjectProperty<>(Duration.seconds(0.2));
			final ObjectProperty<Node>		node = new SimpleObjectProperty<>();

			final FadeTransition	fadeinTransition	= new FadeTransition();
			final FadeTransition	fadeoutTransition	= new FadeTransition();

		/* Properties */
			public BooleanProperty enabledProperty()
			{
				return enabled;
			}

			public ObjectProperty<Duration> durationProperty()
			{
				return duration;
			}

			public ObjectProperty<Node> nodeProperty()
			{
				return node;
			}


		/* Events */
			final EventHandler<MouseEvent> onMouseDelegate = (ev)->
			{
				if( !enabled.get() )
				{
					return;
				}

				if( ev.getEventType()==MouseEvent.MOUSE_ENTERED )
				{
					this.play(false);
				}
				else if( ev.getEventType()==MouseEvent.MOUSE_EXITED )
				{
					this.play(true);
				}
			};

			final ChangeListener<Node> onChangeNodeDelegate = (ob,oldVal,newVal)->
			{
				if( Objects.nonNull(newVal) )
				{
					fadeinTransition.fromValueProperty().bind(newVal.opacityProperty());
					fadeoutTransition.fromValueProperty().bind(newVal.opacityProperty());
				}
			};


		/* Init */
			public NodeFader()
			{
				fadeinTransition.durationProperty().bind(duration);
				fadeinTransition.nodeProperty().bind(node);
				fadeinTransition.setToValue(1.0);

				fadeoutTransition.durationProperty().bind(duration);
				fadeoutTransition.nodeProperty().bind(node);
				fadeoutTransition.setToValue(0.0);
			}


		/* Methods */
			public void addEventToNode(Node node)
			{
				node.addEventHandler(MouseEvent.ANY, onMouseDelegate);
			}

			public void removeEventFromNode(Node node)
			{
				node.removeEventHandler(MouseEvent.ANY, onMouseDelegate);
			}

			public void play(boolean isFadeout)
			{
				if( isFadeout )
				{
					fadeinTransition.stop();
					fadeoutTransition.playFromStart();
				}
				else{
					fadeoutTransition.stop();
					fadeinTransition.playFromStart();
				}
			}
}
