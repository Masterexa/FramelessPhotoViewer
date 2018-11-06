package masterexa.frameless_photoviewer;

import java.util.HashMap;
import java.util.Objects;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class FrameResizeControl {

	/* Inner Class */
		public static class Rect{
			/* Fields */
				double left;
				double right;
				double top;
				double bottom;

			/* Inits */
				public Rect()
				{
					this.left = 0.0;
					this.top	= 0.0;
					this.right = 0.0;
					this.bottom	= 0.0;
				}

				public Rect(double left, double top, double right, double bottom)
				{
					this.left	= left;
					this.top	= top;
					this.right	= right;
					this.bottom	= bottom;
				}

			/* Methods */
		}


	/* Instance */
		/* Fields */
			private Stage		stage = null;
			private Region		region = null;
			private NodeFader	nodeFader = null;

			private final HashMap<Object,Rect>		moveSets = new HashMap<>();

			private final BooleanProperty	keepAspect = new SimpleBooleanProperty(false);
			private final DoubleProperty	aspect = new SimpleDoubleProperty(1.0);
			private DoubleBinding			stageHeight = null;

			// States
			private Point2D		offset = new Point2D(0, 0);

		/* Properties */
			public BooleanProperty keepAspectProperty()
			{
				return keepAspect;
			}


		/* Events */
			private final EventHandler<MouseEvent> onMouseDelegate = (ev)->
			{
				if( Objects.isNull(stage) || !stage.isResizable() )
				{
					return;
				}

				if( ev.getEventType()==MouseEvent.MOUSE_PRESSED )
				{
					offset = new Point2D(ev.getScreenX(), ev.getScreenY());
					if( Objects.nonNull(nodeFader) )
					{
						nodeFader.enabledProperty().set(false);
						nodeFader.play(false);
					}
				}
				else if( ev.getEventType()==MouseEvent.MOUSE_DRAGGED )
				{
					Rect factor = moveSets.get(ev.getTarget());
					if( Objects.isNull(factor) )
					{
						return;
					}

					moveStage(factor, ev.getScreenX()-offset.getX(), ev.getScreenY()-offset.getY());
					offset = new Point2D(ev.getScreenX(), ev.getScreenY());
				}
				else if( ev.getEventType()==MouseEvent.MOUSE_RELEASED )
				{
					if( Objects.nonNull(nodeFader) )
					{
						nodeFader.enabledProperty().set(true);
						nodeFader.play(true);
					}
				}
			};


		/* Inits */
			public FrameResizeControl()
			{

				// Aspect keeper
				keepAspect.addListener((ob,oldVal,isKeep)->
				{
					if( Objects.isNull(stage) )
					{
						return;
					}

					aspect.set( stage.getWidth() / stage.getHeight() );
				});
			}

		/* Border */
			public void setStage(Stage stage)
			{
				this.stage = stage;
				stageHeight = stage.widthProperty().divide(aspect);
			}

			public void setRegion(Region region)
			{
				this.region = region;
			}

			public void setNodeFader(NodeFader fader)
			{
				nodeFader = fader;
			}

			public void addBorderNode(Node node, Rect factor)
			{
				moveSets.put(node, factor);
				node.addEventHandler(MouseEvent.ANY, onMouseDelegate);
			}

			public void removeBorderNode(Node node)
			{
				moveSets.remove(node);
				node.removeEventHandler(MouseEvent.ANY, onMouseDelegate);
			}

			public void addBorderNode(Node top, Node bottom, Node left, Node right, Node leftTop, Node rightTop, Node leftBottom, Node rightBottom)
			{
				// TOP
				if( !Objects.isNull(top) )
				{
					addBorderNode(top, new Rect(0, 1, 0, 0));
				}
				// BOTTOM
				if( !Objects.isNull(bottom) )
				{
					addBorderNode(bottom, new Rect(0, 0, 0, 1));
				}
				// LEFT
				if( !Objects.isNull(left) )
				{
					addBorderNode(left, new Rect(1, 0, 0, 0));
				}
				// RIGHT
				if( !Objects.isNull(right) )
				{
					addBorderNode(right, new Rect(0, 0, 1, 0));
				}


				// LEFT-TOP
				if( !Objects.isNull(leftTop) )
				{
					addBorderNode(leftTop, new Rect(1, 1, 0, 0));
				}
				// RIGHT-TOP
				if( !Objects.isNull(rightTop) )
				{
					addBorderNode(rightTop, new Rect(0, 1, 1, 0));
				}
				// LEFT-BOTTOM
				if( !Objects.isNull(leftBottom) )
				{
					addBorderNode(leftBottom, new Rect(1, 0, 0, 1));
				}
				// RIGHT-BOTTOM
				if( !Objects.isNull(rightBottom) )
				{
					addBorderNode(rightBottom, new Rect(0, 0, 1, 1));
				}
			}

			public void setFrameSize(double width, double height){

				double subW = stage.getWidth();
				double subH = stage.getHeight();
				double w = width;
				double h = height;

				aspect.set( w / h );
				stage.setWidth( w );
				stage.setHeight( h );
			}

			public double getFrameWidth()
			{
				return stage.getWidth()-region.getWidth();
			}

			public double getFrameHeight()
			{
				return stage.getHeight()-region.getHeight();
			}


		/* Inner */
			private void moveStage(Rect factor, double deltaX, double deltaY)
			{
				Rect rc = new Rect(
					stage.getX(), stage.getY(),
					stage.getX() + stage.getWidth(),
					stage.getY() + stage.getHeight()
				);


				rc.left 	+= deltaX * factor.left;
				rc.top		+= deltaY * factor.top;
				rc.right	+= deltaX * factor.right;
				rc.bottom	+= deltaY * factor.bottom;

				stage.setX(rc.left);
				stage.setY(rc.top);
				if( keepAspect.get() )
				{
					if( (factor.top+factor.bottom) > 0.0 )
					{
						stage.setWidth( Math.max(100.0, (rc.bottom-rc.top)*aspect.get()) );
						stage.setHeight( Math.max(100.0, (rc.bottom-rc.top)) );
					}
					else{
						stage.setWidth( Math.max(100.0, rc.right-rc.left) );
						stage.setHeight( Math.max(100.0, (rc.right-rc.left)/aspect.get()) );
					}
				}
				else{
					stage.setWidth(Math.max(100.0,rc.right-rc.left));
					stage.setHeight(Math.max(100.0,rc.bottom-rc.top));
				}
			}
}
