package projeto

trait RandomWithState {
  def nextInt: (Int, RandomWithState)

  def nextInt(n: Int): (Int, RandomWithState)
}
