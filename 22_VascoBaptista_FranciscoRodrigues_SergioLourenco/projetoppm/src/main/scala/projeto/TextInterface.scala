package projeto

import projeto.Board._

import java.io.{File, FileWriter, PrintWriter}
import scala.annotation.tailrec

//T6
object TextInterface {

  // Prompt's
  def showPromptSize(): Unit = println(Console.WHITE + "\nEnter the size of the board:")

  def showPromptx(): Unit = print(Console.WHITE + "Enter the x-coordinate (or 'z' to undo): ")

  def showPrompty(): Unit = print(Console.WHITE + "Enter the y-coordinate (or 'z' to undo): ")

  def getUserInput(): String = scala.io.StdIn.readLine().trim()

  def boardloop(): Int = {
    showPromptSize()
    val size = getUserInput()
    if (size.toIntOption.isDefined && size.toInt > 1)
      return size.toInt
    println(Console.YELLOW + "\nInvalid size for the board (choose a number bigger then 1)\n")
    boardloop()
  }

  def start() {
    val hex: Board = createboard(boardloop())
    val myRandom = new MyRandom(10)
    val coord: (Int, Int) = (0, 0)
    val npc: (Int, Int) = (0, 0)

    mainloop(hex, myRandom, coord, npc)

    def mainloop(board: Board, random: RandomWithState, coordinates: (Int, Int), pc: (Int, Int)): Unit = {

      val rando: RandomWithState = random
      val rand: RandomWithState = random

      hasContiguousLine(board) match {
        case None =>
        case Some(Cells.Red) =>
          print(Console.GREEN + "\nThe game is over, Red wins\n")
          return
        case Some(Cells.Blue) =>
          print(Console.GREEN + "\nThe game is over, Blue wins\n")
          return
      }

      // User
      showPromptx()
      val userx = getUserInput()
      if (userx == "z") {
        val newBoard = Board.undo(board, "src/main/scala/projeto/plays.txt")
        printBoard(newBoard)
        mainloop(newBoard, rand, coord, npc)
      }
      showPrompty()
      val usery = getUserInput()
      if (usery == "z") {
        val newBoard = Board.undo(board, "src/main/scala/projeto/plays.txt")
        printBoard(newBoard)
        mainloop(newBoard, rand, coord, npc)
      }

      if (usery.toIntOption == None || userx.toIntOption == None) {
        println(Console.YELLOW + "\nSomething went wrong, try again (Invalid coordinates)\n")
        mainloop(board, rando, coord, npc)
      }


      // Computer
      @tailrec
      def playComputerMove(b: Board, r: RandomWithState, npc: (Int, Int)): (Board, (Int, Int)) = {
        val ((x, y), rando) = randomMove(b, r)
        if (b(y)(x) == Cells.Empty) {
          (Board.play(b, Cells.Blue, x, y), (x,y))
        } else {
          playComputerMove(b, rando, npc)
        }
      }

      (userx, usery) match {
        case (x, y)
          if (x.toInt >= 0 && y.toInt >= 0 && x.toInt < board.size && y.toInt < board.size && board(y.toInt)(x.toInt) == Cells.Empty) =>
          val newBoardP = Board.play(board, Cells.Red, x.toInt, y.toInt)
          val newCoord = (x.toInt, y.toInt)
          val (newBoard, newNpc) = playComputerMove(newBoardP, rand, npc)
          printBoard(newBoard)
          mainloop(newBoard, rand, newCoord, newNpc)

        case _ =>
          println(Console.YELLOW + "\nSomething went wrong, try again (Invalid coordinates)\n")
          mainloop(board, rando, coord, npc)
      }
    }
  }
}