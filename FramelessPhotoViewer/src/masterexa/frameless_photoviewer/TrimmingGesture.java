package masterexa.frameless_photoviewer;

import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

public class TrimmingGesture {

	/* Instance */
		/* Fields */
			final ObjectProperty<TrimmingInfo>	trimmingInfo = new SimpleObjectProperty<>();

			Point2D prevPos;

		/* Properties */
			ObjectProperty<TrimmingInfo>	trimmingInfoProperty()
			{
				return trimmingInfo;
			}


		/* Events */
			final EventHandler<ScrollEvent> onZoomScrollDelegate = (ev)->
			{
				onZooming(ev.getDeltaY());
			};

			final EventHandler<ZoomEvent> onZoomDelegate = (ev)->
			{
				onZooming(ev.getZoomFactor());
			};

			final EventHandler<MouseEvent> onMouseDragDelegate = (ev)->
			{
				Point2D pos = new Point2D(ev.getSceneX(), ev.getSceneY());

				if( ev.getEventType()==MouseEvent.MOUSE_PRESSED )
				{
					onDragStart(pos);
				}
				else if( ev.getEventType()==MouseEvent.MOUSE_DRAGGED )
				{
					onDragging(pos);
				}
			};


			void onZooming(double delta)
			{
				TrimmingInfo info = trimmingInfo.get();

				if( Objects.nonNull(info) && info.enabledProperty().get() )
				{
					double zoom = info.zoomFactorProperty().get();

					zoom += delta*0.005;
					zoom = Math.max(0.1, zoom);
					info.zoomFactorProperty().set(zoom);
				}
			}

			void onDragStart(Point2D pos)
			{
				prevPos = pos;
			}

			void onDragging(Point2D pos)
			{
				TrimmingInfo info = trimmingInfo.get();

				if( Objects.nonNull(info) && info.enabledProperty().get() )
				{
					double x = info.translateXProperty().get();
					double y = info.translateYProperty().get();

					x += pos.getX()-prevPos.getX();
					y += pos.getY()-prevPos.getY();
					info.translateXProperty().set(x);
					info.translateYProperty().set(y);
					prevPos = pos;
				}
			}


		/* Methods */
			public void addEventToNode(Node node)
			{
				node.addEventHandler(MouseEvent.ANY, onMouseDragDelegate);
				node.addEventHandler(ScrollEvent.SCROLL, onZoomScrollDelegate);
				node.addEventHandler(ZoomEvent.ANY, onZoomDelegate);
			}

			public void removeEventFromNode(Node node)
			{
				node.removeEventHandler(MouseEvent.ANY, onMouseDragDelegate);
				node.removeEventHandler(ScrollEvent.SCROLL, onZoomScrollDelegate);
				node.removeEventHandler(ZoomEvent.ANY, onZoomDelegate);
			}
}
