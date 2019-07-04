package rps
import scala.io.StdIn.readLine
import scala.util.Random
object Main extends App {
  Game.play
}

object Game {
  def play() : Unit = {
    val moveMap = Map("0" -> "Rock", "1" -> "Paper", "2" -> "Scissor")
    println("Choose Rock (0), Paper(1), Scissor(2)")
    val move = moveMap.getOrElse(readLine(),"None")
    val computerMove = moveMap(Random.nextInt(3).toString)
    val resultString = s"$move VS $computerMove -> "
    val result =(move, computerMove) match {
      case (x,y) if x == y => "Draw!"
      case ("Rock","Paper") | ("Paper","Scissor") | ("Scissor","Rock") => "You lost!"
      case ("Rock","Scissor") | ("Paper","Rock") | ("Scissor","Paper")  => "You win!"
      case (_,_) =>  "Not an Option!"
    }
    println(s"$resultString $result")
  }
}