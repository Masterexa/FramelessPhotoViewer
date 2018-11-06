package masterexa.frameless_photoviewer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageFrameView implements Initializable {


	/* Class */
		/* Methods */
			public static StageFrameView makeStageFrame(Stage stage) throws IOException
			{
				FXMLLoader loader = new FXMLLoader(StageFrameView.class.getResource("StageFrame.fxml"));
				loader.load();

				Scene			scene = new Scene(loader.getRoot());
				StageFrameView 	view = loader.getController();

				stage.initStyle(StageStyle.TRANSPARENT);
				scene.getStylesheets().add(Main.getMainCSS());
				scene.setFill(new Color(0.0,0.0,0.0,0.01));

				stage.setScene(scene);
				view.setStage(stage);
				return view;
			}


	/* Instance */
		/* Fields */
			Stage	stage;
			final BooleanProperty keepAspect = new SimpleBooleanProperty(false);

		/* UI */
			final FrameMoveControl 		moveControl = new FrameMoveControl();
			final FrameResizeControl 	resizeControl = new FrameResizeControl();
			final NodeFader				nodeFader = new NodeFader();
			final Pane	transparentOuterPane = new Pane();
			final Pane	transparentInnerPane = new Pane();

		/* FXMLs */
			/* Controls */
			@FXML
			AnchorPane		root;
			@FXML
			Button			closeButton;
			@FXML
			Button			minimizeButton;
			@FXML
			ToggleButton	focusPinToggle;
			@FXML
			AnchorPane frameAnchor;


			/* Sub-scenes */
			@FXML
			SubScene	outerSubScene;
			@FXML
			SubScene	innerSubScene;
			@FXML
			HBox		optionButtonArea;

			/* Borders */
			@FXML
			Pane	boundTop;
			@FXML
			Pane	boundBottom;
			@FXML
			Pane	boundLeft;
			@FXML
			Pane	boundRight;
			@FXML
			Pane	boundLeftTop;
			@FXML
			Pane	boundLeftBottom;
			@FXML
			Pane	boundRightTop;
			@FXML
			Pane	boundRightBottom;

		/* Properties */
			BooleanProperty keepAspectProperty()
			{
				return keepAspect;
			}


		/* Inits */
			@Override
			public void initialize(URL location, ResourceBundle resources) {
				// TODO 自動生成されたメソッド・スタブ

				closeButton.setOnAction((ev)->
				{
					if( Objects.nonNull(stage) )
					{
						stage.close();
					}
				});
				minimizeButton.setOnAction( (ev)->
				{
					if( Objects.nonNull(stage) )
					{
						stage.setIconified(true);
					}
				});
				focusPinToggle.selectedProperty().addListener((ob,oldValue,newValue)->
				{
					if( Objects.nonNull(stage) )
					{
						stage.setAlwaysOnTop(newValue);
					}
				});

				// Set transparent
				transparentOuterPane.setBackground(null);
				transparentOuterPane.setMouseTransparent(true);
				transparentInnerPane.setBackground(null);
				transparentInnerPane.setMouseTransparent(true);
				outerSubScene.setFill(null);
				innerSubScene.setFill(null);
				outerSubScene.setRoot(transparentOuterPane);
				innerSubScene.setRoot(transparentInnerPane);

				moveControl.addEventToNode(frameAnchor);
				resizeControl.setRegion(root);
				resizeControl.setNodeFader(nodeFader);
				resizeControl.addBorderNode(boundTop, boundBottom, boundLeft, boundRight, boundLeftTop, boundRightTop, boundLeftBottom, boundRightBottom);
				resizeControl.keepAspectProperty().bind(keepAspect);
				nodeFader.nodeProperty().set(frameAnchor);
				nodeFader.addEventToNode(root);

				outerSubScene.widthProperty().bind( frameAnchor.widthProperty() );
				outerSubScene.heightProperty().bind( frameAnchor.heightProperty() );
				innerSubScene.widthProperty().bind( frameAnchor.widthProperty() );
				innerSubScene.heightProperty().bind( frameAnchor.heightProperty() );
			}

		/* Methods */
			public void setStage(Stage stage)
			{
				this.stage = stage;
				moveControl.setStage(stage);
				resizeControl.setStage(stage);
			}

			public void setOuterScene(Parent root)
			{
				outerSubScene.setRoot(root);
			}

			public void setInnerScene(Parent root)
			{
				innerSubScene.setRoot(root);
			}


			public ObservableList<Node> getOptionButtons()
			{
				return optionButtonArea.getChildren();
			}


			public FrameMoveControl getFrameMoveControl()
			{
				return moveControl;
			}

			public FrameResizeControl getFrameResizeControl()
			{
				return resizeControl;
			}
}
