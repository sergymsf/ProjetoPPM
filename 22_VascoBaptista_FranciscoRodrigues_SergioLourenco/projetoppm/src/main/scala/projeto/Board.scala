package projeto


import projeto.Cells._

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import scala.annotation.tailrec
import scala.io.Source

object Board {
  type Board = List[List[Cells.Cell]]

  def createboard(x: Int): Board = {
    List.fill(x)(List.fill(x)(Cells.Empty))
  }

  //T1
  def randomMove(board: Board, rand: RandomWithState): ((Int, Int), RandomWithState) = {
    val (col, rand1) = rand.nextInt(board.length)
    val (row, rand2) = rand1.nextInt(board.head.length)
    return ((col, row), rand2)
  }

  //T2
  def play(booard: Board, player: Cells.Cell, row: Int, col: Int): Board = {

    val writer = new PrintWriter(new FileWriter("src/main/scala/projeto/plays.txt", true))
    if (player == Blue)
      writer.print("\n" + row + "," + col + ", B")
    if (player == Red)
      writer.print("\n" + row + "," + col + ", R")

    writer.close()
    booard.updated(col, booard(col).updated(row, player))
  }

  //T3
  def printBoard(board: Board): Unit = {
    val gameSize = board.size

    def printLine(i: Int, j: Int, tab: Int): Unit = {
      if (i == 0 && j == 0) {
        printFirstLine(gameSize, i)
      }
      if (i == gameSize) {
        printFirstLine(gameSize, i)
        return
      }
      val tabs = " " * tab
      if (j == 0) {
        print(tabs + Console.RED + "* " + colorToValue(board, i, j) + Console.RESET)
        printLine(i, j + 1, tab)
      } else if (j == gameSize) {
        print(Console.RED + " *\n")
        if (i < gameSize - 1) {
          printInnerLine(gameSize, tab)
        }
        printLine(i + 1, 0, tab + 1)
      } else {
        print(Console.WHITE + " - " + colorToValue(board, i, j) + Console.RESET)
        printLine(i, j + 1, tab)
      }
    }

    def printInnerLine(size: Int, tab: Int): Unit = {
      def printRow(i: Int, line: String): Unit = {
        if (i == size) {
          println(line)
        } else if (i == size - 1) {
          printRow(i + 1, line + Console.WHITE + "\\ ")
        } else {
          printRow(i + 1, line + Console.WHITE + "\\ / ")
        }
      }

      printRow(0, "  " + " " * tab)
    }

    def printFirstLine(size: Int, tab: Int): Unit = {
      def printRow(i: Int, line: String): Unit = {
        if (i == size) {
          println(line)
        } else {
          printRow(i + 1, line + Console.BLUE + " *  ")
        }
      }

      printRow(0, " " * tab)
    }

    def colorToValue(board: Board, row: Int, col: Int): String = {
      board(row)(col) match {
        case Blue => Console.BLUE + "0"
        case Red => Console.RED + "X"
        case _ => Console.WHITE + "."
      }
    }

    printLine(0, 0, 0)
  }

  //T4
  def hasContiguousLine(board: Board): Option[Cells.Cell] = {

    def dfs(curr: (Int, Int), visited: Set[(Int, Int)], color: Cells.Cell): Boolean = {
      val (y, x) = curr
      if (board(y)(x) != color)
        false
      else if ((color == Cells.Red && x == board.size - 1) || (color == Cells.Blue && y == board.size - 1))
        true
      else {
        val neighbors = List((y - 1, x), (y + 1, x), (y, x - 1), (y, x + 1), (y - 1, x + 1), (y + 1, x - 1))
          .filter(n => n._1 >= 0 && n._1 < board.size && n._2 >= 0 && n._2 < board.size && board(n._1)(n._2) == color)
        neighbors.exists(n => !visited.contains(n) && dfs(n, visited + curr, color))
      }
    }

    val redVisited = (0 until board.size).map((_, 0)).toSet
    val redContiguous = redVisited.exists(n => dfs(n, Set.empty, Cells.Red))
    if (redContiguous)
      return Some(Cells.Red)

    val blueVisited = (0 until board.size).map((0, _)).toSet
    val blueContiguous = blueVisited.exists(n => dfs(n, Set.empty, Cells.Blue))
    if (blueContiguous)
      return Some(Cells.Blue)

    None
  }

  //T5
  def undo(board: Board, file: String): Board = {
    val lines = Source.fromFile(file).getLines().toSeq
    if (lines.length < 2) {
      println(Console.YELLOW + "\nNo moves left to undo.\n")
      return board
    }
    val (row1, col1, _) = lines(lines.length - 2).split(",") match {
      case Array(rowStr, colStr, team) => (rowStr.toInt, colStr.toInt, team)
    }
    val (row2, col2, _) = lines(lines.length - 1) split (",") match {
      case Array(rowStr, colStr, team) => (rowStr.toInt, colStr.toInt, team)
    }

    val newBoard = board.updated(col1, board(col1).updated(row1, Cells.Empty)).updated(col2, board(col2).updated(row2, Cells.Empty))

    val bw = new BufferedWriter(new FileWriter(new File(file), true))
    try {
      bw.write("\n" + row1 + "," + col1)
      bw.write("\n" + row2 + "," + col2)
      bw.flush()
    } finally {
      bw.close()
    }

    val linesToKeep = lines.dropRight(2).mkString("\n")
    val writer = new BufferedWriter(new FileWriter(new File(file)))
    try {
      writer.write(linesToKeep)
      writer.flush()
    } finally {
      writer.close()
    }
    newBoard
  }
}
