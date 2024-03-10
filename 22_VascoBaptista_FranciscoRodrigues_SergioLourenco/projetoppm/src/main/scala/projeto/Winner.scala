package projeto

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import javafx.scene.paint.Color

class Winner {

  @FXML
  private var label1: Label = _
  @FXML
  private var image: ImageView = _
  @FXML
  private var image2: ImageView = _

  def getLabel(x: Int): Unit = {
    if (x == 1) {
      label1.setText("YOU LOST...")
      label1.setTextFill(Color.RED)
      val imageUrl = getClass.getResource("images/loser.gif").toExternalForm
      val gifImage = new Image(imageUrl)
      image.setImage(gifImage)
      image2.setImage(gifImage)
    } else if (x == 2) {
      label1.setText("PLAYER 1 \n WON!!!")
      label1.setTextFill(Color.BLUE)
      val imageUrl = getClass.getResource("images/loser.gif").toExternalForm
      val gifImage = new Image(imageUrl)
      image2.setImage(gifImage)
    } else if (x == 3) {
      label1.setText("PLAYER 2 \n WON!!!")
      label1.setTextFill(Color.BLUE)
      val imageUrl = getClass.getResource("images/loser.gif").toExternalForm
      val gifImage = new Image(imageUrl)
      image2.setImage(gifImage)

    }
  }
}
