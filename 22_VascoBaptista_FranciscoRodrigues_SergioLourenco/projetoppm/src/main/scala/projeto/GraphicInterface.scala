package projeto

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import projeto.Board._
import projeto.Main.HelloWorld

import java.io.File
import scala.io.Source

class GraphicInterface {

  @FXML
  private var button00: Button = _
  @FXML
  private var button01: Button = _
  @FXML
  private var button02: Button = _
  @FXML
  private var button03: Button = _
  @FXML
  private var button04: Button = _
  @FXML
  private var button10: Button = _
  @FXML
  private var button11: Button = _
  @FXML
  private var button12: Button = _
  @FXML
  private var button13: Button = _
  @FXML
  private var button14: Button = _
  @FXML
  private var button20: Button = _
  @FXML
  private var button21: Button = _
  @FXML
  private var button22: Button = _
  @FXML
  private var button23: Button = _
  @FXML
  private var button24: Button = _
  @FXML
  private var button30: Button = _
  @FXML
  private var button31: Button = _
  @FXML
  private var button32: Button = _
  @FXML
  private var button33: Button = _
  @FXML
  private var button34: Button = _
  @FXML
  private var button40: Button = _
  @FXML
  private var button41: Button = _
  @FXML
  private var button42: Button = _
  @FXML
  private var button43: Button = _
  @FXML
  private var button44: Button = _
  @FXML
  private var e1: Label = _
  @FXML
  private var e2: Label = _
  @FXML
  private var e3: Label = _
  @FXML
  private var e4: Label = _
  @FXML
  private var e5: Label = _
  @FXML
  private var e6: Label = _
  @FXML
  private var e7: Label = _
  @FXML
  private var e8: Label = _
  @FXML
  private var e9: Label = _
  @FXML
  private var e0: Label = _
  @FXML
  private var b1: Label = _
  @FXML
  private var b0: Label = _
  @FXML
  private var textField1: TextField = _
  @FXML
  private var anchorPane: AnchorPane = _
  @FXML
  private var label1: Label = _
  @FXML
  private var undo1: Button = _

  val HelloWorld = new HelloWorld()
  var (board, rand, coord, npc) = HelloWorld.getValues()
  var difficulty = 0.0
  var pvp = 0
  var playable = 0
  var pvpplayer: Color = Color.WHITE
  var player: Color = Color.WHITE
  var computer: Color = Color.WHITE

  def setGame(file: File): Unit = {
    board = loadGame(file)
  }

  def colorpicker(i: Color, j: Color): Unit = {
    player = i
    computer = j

    e1.setTextFill(player)
    e2.setTextFill(player)
    e3.setTextFill(player)
    e4.setTextFill(player)
    e5.setTextFill(player)
    e6.setTextFill(player)
    e7.setTextFill(player)
    e8.setTextFill(player)
    e9.setTextFill(player)
    e0.setTextFill(player)
    b0.setTextFill(computer)
    b1.setTextFill(computer)

  }

  def buttonclicked(event: MouseEvent): Unit = {
    val source = event.getSource
    val clickedButton: Button = source.asInstanceOf[Button]
    val Array(x, y) = clickedButton.getText.split(",")
    var button: Button = new Button()
    button = anchorPane.lookup(s"#button$x$y").asInstanceOf[Button]
    button.setId(s"button$x$y")
    label1.setOpacity(0)
    undo1.setOpacity(1)

    (x, y) match {
      case (x, y) if board(y.toInt)(x.toInt) == Cells.Empty =>
        clickedButton.setStyle("-fx-background-color: " + player.toString().replace("0x", "#") + ";")
        board = Board.play(board, Cells.Red, x.toInt, y.toInt)
        coord = (x.toInt, y.toInt)
        playable = 1
      case _ =>
        label1.setText("Something went wrong, choose other coordinate")
        label1.setOpacity(1)
        playable = 0
    }

    if (pvp == 1 && playable == 1) {
      pvpplayer = if (pvpplayer == player) computer else player
      if (pvpplayer == player) {
        clickedButton.setStyle("-fx-background-color: " + player.toString().replace("0x", "#") + ";")
        board = Board.play(board, Cells.Red, x.toInt, y.toInt)
        coord = (x.toInt, y.toInt)
      } else if (pvpplayer == computer) {
        clickedButton.setStyle("-fx-background-color: " + computer.toString().replace("0x", "#") + ";")
        board = Board.play(board, Cells.Blue, x.toInt, y.toInt)
        coord = (x.toInt, y.toInt)
      }
    } else if(playable == 1) {
      val (newboard, newNpc) = playNPC(board, rand, npc)
      button = anchorPane.lookup(s"#button${newNpc._1}${newNpc._2}").asInstanceOf[Button]
      button.setId(s"button${newNpc._1}${newNpc._2}")
      button.setStyle("-fx-background-color: " + computer.toString().replace("0x", "#") + ";")
      board = newboard
      npc = newNpc
    }

    hasContiguousLine(board) match {
      case None =>
      case Some(Cells.Red) =>
        val thirdStage: Stage = new Stage()
        thirdStage.setTitle("My Hello World App")
        val fxmlLoader = new FXMLLoader(getClass.getResource("Winner.fxml"))
        val mainViewRoot: Parent = fxmlLoader.load()
        val scene = new Scene(mainViewRoot)
        thirdStage.setScene(scene)
        thirdStage.show()
        if (pvp == 1) {
          val controller2 = fxmlLoader.getController[Winner]
          controller2.getLabel(2)
        }
        clickedButton.getScene.getWindow.hide()

      case Some(Cells.Blue) =>
        val thirdStage: Stage = new Stage()
        thirdStage.setTitle("My Hello World App")
        val fxmlLoader = new FXMLLoader(getClass.getResource("Winner.fxml"))
        val mainViewRoot: Parent = fxmlLoader.load()
        val scene = new Scene(mainViewRoot)
        thirdStage.setScene(scene)
        thirdStage.show()
        val controller2 = fxmlLoader.getController[Winner]
        if (pvp == 1) {
          controller2.getLabel(3)
        } else {
          controller2.getLabel(1)
        }
        clickedButton.getScene.getWindow.hide()


    }
  }

  def playNPC(b: Board, r: RandomWithState, npc: (Int, Int)): (Board, (Int, Int)) = {
    if (difficulty < 0.6) {
      val ((x, y), rando) = randomMove(b, r)
      if (b(y)(x) == Cells.Empty) {
        (Board.play(b, Cells.Blue, x, y), (x, y))

      } else {
        playNPC(b, rando, npc)
      }
    } else {
      val bestMove = getBestMove(b, Cells.Blue)
      if (bestMove.nonEmpty) {
        val (x, y) = bestMove.get
        (Board.play(b, Cells.Blue, x, y), (x, y))
      } else {
        val ((x, y), rando) = randomMove(b, r)
        if (b(y)(x) == Cells.Empty) {
          (Board.play(b, Cells.Blue, x, y), (x, y))
        } else {
          playNPC(b, rando, npc)
        }
      }
    }
  }

  def undoButtonClicked(): Unit = {
    if (undo1.getOpacity == 1) {
      var button: Button = new Button()
      //val newBoard = board.updated(coord._2, board(coord._2).updated(coord._1, Cells.Empty)).updated(npc._2, board(npc._2).updated(npc._1, Cells.Empty))

      val newBoard = Board.undo(board, "src/main/scala/projeto/plays.txt")
      button = anchorPane.lookup(s"#button${npc._1}${npc._2}").asInstanceOf[Button]
      button.setId(s"button${npc._1}${npc._2}")
      button.setStyle("")
      button = anchorPane.lookup(s"#button${coord._1}${coord._2}").asInstanceOf[Button]
      button.setId(s"button${coord._1}${coord._2}")
      button.setStyle("")
      undo1.setOpacity(0.5)
      board = newBoard
    } else {
      label1.setOpacity(1)
      label1.setText("One undo per move")
    }
  }

  def npcfirst(): Unit = {

    var button: Button = new Button()
    val (newboard, newNpc) = playNPC(board, rand, npc)


    button = anchorPane.lookup(s"#button${newNpc._1}${newNpc._2}").asInstanceOf[Button]
    button.setId(s"button${newNpc._1}${newNpc._2}")
    button.setStyle("-fx-background-color: " + computer.toString().replace("0x", "#") + ";")


    board = newboard
    npc = newNpc
  }

  def setDifficulty(diff: Double): Unit = difficulty = diff

  def setPvP(aux: Int): Unit = pvp = aux

  def getBestMove(b: Board, player: Cells.Value): Option[(Int, Int)] = {
    val emptyCells = getEmptyCells(b)
    if (emptyCells.isEmpty) {
      return None
    }

    val (_, bestMove) = (emptyCells foldLeft(0.0, None: Option[(Int, Int)])) {
      case ((currentBestScore, currentBestMove), (x, y)) =>
        val score = evaluateMove(b, player, x, y)

        if (score > currentBestScore)
          (score, Some((x, y)))
        else
          (currentBestScore, currentBestMove)
    }

    bestMove
  }

  def getEmptyCells(b: Board): List[(Int, Int)] = {
    def collectEmptyCells(board: Board, y: Int, x: Int, result: List[(Int, Int)]): List[(Int, Int)] = {
      if (y >= board.length) {
        result
      } else {
        val cell = board(y)(x)
        val updatedResult = if (cell == Cells.Empty) result :+ (x, y) else result

        val (nextY, nextX) =
          if (x < board(y).length - 1) (y, x + 1)
          else (y + 1, 0)

        collectEmptyCells(board, nextY, nextX, updatedResult)
      }
    }

    collectEmptyCells(b, 0, 0, List.empty)
  }

  def evaluateMove(b: Board, player: Cells.Value, x: Int, y: Int): Double = {
    if (player == Cells.Blue) {
      if (x == 4) {
        return 1.0
      }
      if (x == 3) {
        return 0.5
      }
      return 0.75 / (5 - x)
    }
    return 0.0
  }

  def loadGame(file: File): Board = {
    val board = createboard(5)
    if (!file.exists()) {
      println("Error: The file does not exist")
      return board
    }
    val lines = Source.fromFile(file).getLines().toList

    def loadMoves(index: Int, currentBoard: Board): Board = {
      if (index < 0) {
        currentBoard
      } else {
        val line = lines(index)
        val (row, col, team) = line.split(",") match {
          case Array(rowStr, colStr, teamStr) => (rowStr.toInt, colStr.toInt, teamStr)
        }
        if (team == " R") {
          val newBoard = play(currentBoard, Cells.Red, row, col)
          var button: Button = new Button()
          button = anchorPane.lookup(s"#button$row$col").asInstanceOf[Button]
          button.setStyle("-fx-background-color: " + player.toString().replace("0x", "#") + ";")
          loadMoves(index - 1, newBoard)
        } else if (team == " B") {
          val newBoard = play(currentBoard, Cells.Blue, row, col)
          var button: Button = new Button()
          button = anchorPane.lookup(s"#button$row$col").asInstanceOf[Button]
          button.setStyle("-fx-background-color: " + computer.toString().replace("0x", "#") + ";")
          loadMoves(index - 1, newBoard)
        }
        currentBoard
      }
    }

    loadMoves(lines.length - 1, board)
  }
}