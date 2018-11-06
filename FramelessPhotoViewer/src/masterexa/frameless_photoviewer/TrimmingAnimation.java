package masterexa.frameless_photoviewer;


import java.util.Objects;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.util.Duration;

public class TrimmingAnimation {

	class Cycle extends AnimationTimer{

		public long previousTime;

		@Override
		public void handle(long now) {

			double deltaTime 	= (double)(now - previousTime) / 1000000000.0;
			previousTime 		= now;

			Interpolator	interpol	= Interpolator.LINEAR;
			TrimmingInfo 	info		= trimmingInfo.get();
			double 			t 			= deltaTime*(1.0/duration.get().toSeconds());


			if( Objects.nonNull(node) && Objects.nonNull(info) )
			{
				node.setTranslateX(
					interpol.interpolate(node.getTranslateX(), info.translateXProperty().get(), t)
				);
				node.setTranslateY(
					interpol.interpolate(node.getTranslateY(), info.translateYProperty().get(), t)
				);
				node.setScaleX(
					interpol.interpolate(node.getScaleX(), info.zoomFactorProperty().get(), t)
				);
				node.setScaleY(
					interpol.interpolate(node.getScaleY(), info.zoomFactorProperty().get(), t)
				);
				node.setRotate(
					interpol.interpolate(node.getRotate(), info.rotationAngleProperty().get(), t)
				);
			}
		}

	}


	/* Instance */
		/* Fields */
			final ObjectProperty<TrimmingInfo>	trimmingInfo 	= new SimpleObjectProperty<>();
			final ObjectProperty<Duration>		duration		= new SimpleObjectProperty<>(Duration.seconds(0.05));

			Node	node = null;
			Cycle	cycle = new Cycle();


		/* Properties */
			ObjectProperty<TrimmingInfo> trimmingInfoProperty()
			{
				return trimmingInfo;
			}


		/* Init */
			public TrimmingAnimation()
			{
			}


		/* Methods */
			public void setNode(Node node)
			{
				this.node = node;
			}

			public void play()
			{
				cycle.previousTime = System.nanoTime();
				cycle.start();
			}
}
