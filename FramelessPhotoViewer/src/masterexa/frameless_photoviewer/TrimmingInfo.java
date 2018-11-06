package masterexa.frameless_photoviewer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

public class TrimmingInfo {

	/* Instance */
		/* Fields */
			final BooleanProperty	enabled			= new SimpleBooleanProperty(false);
			final DoubleProperty	translateX		= new SimpleDoubleProperty(0.0);
			final DoubleProperty	translateY		= new SimpleDoubleProperty(0.0);
			final DoubleProperty	zoomFactor		= new SimpleDoubleProperty(1.0);
			final DoubleProperty	rotationAngle	= new SimpleDoubleProperty(0.0);

		/* Properties */
			public BooleanProperty enabledProperty()
			{
				return enabled;
			}

			public DoubleProperty translateXProperty()
			{
				return translateX;
			}

			public DoubleProperty translateYProperty()
			{
				return translateY;
			}

			public DoubleProperty zoomFactorProperty()
			{
				return zoomFactor;
			}

			public DoubleProperty rotationAngleProperty()
			{
				return rotationAngle;
			}


		/* Events */
			final ChangeListener<Boolean> onChangeEnabledDelegate = (ob,oldVal,newVal)->
			{
				if( !newVal )
				{
					setInitialValue();
				}
			};


		/* Methods */
			public void setInitialValue()
			{
				translateX.set(0);
				translateY.set(0);
				zoomFactor.set(1.0);
			}
}
