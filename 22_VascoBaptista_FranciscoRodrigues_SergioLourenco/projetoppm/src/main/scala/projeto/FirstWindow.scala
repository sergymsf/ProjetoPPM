package projeto

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control._
import javafx.scene.{Parent, Scene}
import javafx.stage.{FileChooser, Stage}
import projeto.Board.{Board, createboard, play}
import projeto.Cells.{Blue, Red}

import java.io.File
import java.nio.file.Paths
import scala.io.Source

class FirstWindow {
  @FXML
  private var button1: Button = _
  @FXML
  private var playercolor: ColorPicker = _
  @FXML
  private var npccolor: ColorPicker = _
  @FXML
  private var playerbox: CheckBox = _
  @FXML
  private var npcbox: CheckBox = _
  @FXML
  private var PvP: CheckBox = _
  @FXML
  private var label1: Label = _
  @FXML
  private var loadButton: Button = _
  @FXML
  private var cancelButton: Button = _
  @FXML
  private var scroll: Slider = _

  var Box: Boolean = false
  var file: File = null

  def loadButtonClicked(): Unit = {
    val filechooser = new FileChooser()
    val path = Paths.get(".").toAbsolutePath.normalize().toString
    filechooser.setInitialDirectory(new File(path))
    file = filechooser.showOpenDialog(loadButton.getScene.getWindow)

    if (file != null) {
      label1.setText("Selected file: " + file.getAbsolutePath())
      label1.setOpacity(1)
    }
  }

  def cancelButtonClicked(): Unit = {
    file = null
    label1.setText("No file selected")
    label1.setOpacity(1)
  }

  def onButtonClicked(): Unit = {

    if (playercolor.getValue == npccolor.getValue) {
      label1.setText("Can't choose the same color for both players")
      label1.setOpacity(1)

    } else if (playerbox.isSelected && npcbox.isSelected) {
      label1.setText("Can't both player start")
      label1.setOpacity(1)

    } else if (!playerbox.isSelected && !npcbox.isSelected && !PvP.isSelected) {
      label1.setText("Someone has to start")
      label1.setOpacity(1)
    } else if (PvP.isSelected && (playerbox.isSelected || npcbox.isSelected)){
      label1.setText("Player vs Player mode selected")
      label1.setOpacity(1)
    }
    else {

      val secondStage: Stage = new Stage()
      secondStage.setTitle("HexBoard Game (ProjetoPPM-22/23)")
      val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val scene = new Scene(mainViewRoot)
      secondStage.setScene(scene)
      secondStage.show()
      button1.getScene.getWindow.hide()
      val controller2 = fxmlLoader.getController[GraphicInterface]
      controller2.colorpicker(playercolor.getValue, npccolor.getValue)
      controller2.setDifficulty(scroll.getValue)

      if (file != null) {
        controller2.setGame(file)
      }
      if (!PvP.isSelected) {
        if (npcbox.isSelected) {
          controller2.setGame(file)
        }
      } else {
        controller2.setPvP(1)
      }
    }
  }

}
