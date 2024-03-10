package projeto

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import projeto.Board._
import projeto.Cells.{Blue, Red}

import java.io.File
import scala.io.Source

object Main extends App {

  val file = new File("src/main/scala/projeto/plays.txt")
  if (file.exists()) {
    file.delete()
  }

  val scanner = new java.util.Scanner(System.in)
  println(Console.YELLOW + "\nChoose an interface: (1) Text, (2) Graphic")
  val choice = scanner.nextInt()
  if (choice == 1) {
    TextInterface.start()
  } else if (choice == 2) {

    Application launch(classOf[HelloWorld], args: _*)

  } else {
    println("Invalid choice")
  }

  class HelloWorld extends Application {

    var board : Board = createboard(5)
    var myRandom = new MyRandom(10)
    var coord: (Int, Int) = (0, 0)
    var npc: (Int, Int) = (0, 0)
    val i : Int = 0
    val j : Int = 0

    override def start(primaryStage: Stage): Unit = {
      primaryStage.setTitle("Settings")
      val fxmlLoader = new FXMLLoader(getClass.getResource("FirstWindow.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val scene = new Scene(mainViewRoot)
      primaryStage.setScene(scene)
      primaryStage.show()
      val secondStage: Stage = new Stage()
      secondStage.setTitle("HexBoard Game (ProjetoPPM-22/23)")
      val fxmLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
      val mainViewRot: Parent = fxmLoader.load()
      val scee = new Scene(mainViewRot)
      secondStage.setScene(scee)

    }
    def getValues(): (Board, MyRandom, (Int, Int), (Int, Int)) = (board, myRandom, coord, npc)
  }
}


