package rps
import scala.io.StdIn.readLine
import scala.util.Random
import Move._
object Main extends App {
  Game.play
}

object Game {
  def play() : Unit = {

  println("Choose Rock (0),PAPER (1),SCISSOR (2)")
  val move = Move.parse(readLine)
  val computerMove = Move.generateRandom()

  val output = move.map(m => {
  val resultString = s"${m} VS ${computerMove}-> "
  val result =(m, computerMove) match {
        case (x,y) if x == y => "Draw!"
        case (ROCK,PAPER) | (PAPER,SCISSOR) | (SCISSOR,ROCK) => "You lost!"
        case (ROCK,SCISSOR) | (PAPER,ROCK) | (SCISSOR,PAPER)  => "You win!"
        case _ => "Unknown combination!"
      }    
      s"$resultString $result"
    }).getOrElse("Not an Option!")
    println(output)
  }
}

object Move {  
  sealed trait Move
  
  case object ROCK extends Move
  case object PAPER extends Move
  case object SCISSOR extends Move 

  def generateRandom() = {
    Random.nextInt(3) match {
      case 0 => ROCK
      case 1 => PAPER
      case 2 => SCISSOR
      case _ => throw new Exception("errorissimo")
    }
  }

  def parse(moveCode : String) = {
    moveCode match {
      case "0" => Option(ROCK)
      case "1" => Option(PAPER)
      case "2" => Option(SCISSOR)
      case _ => None
    }
  }
  
}