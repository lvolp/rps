package rps
import scala.io.StdIn.readLine
import scala.util.Random
import io.buildo.enumero.annotations.indexedEnum
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import Move._

object Main extends App {
  Game.play
}

object Game {
  def play() : Unit = {

  println("Choose Rock (0),PAPER (1),SCISSOR (2)")
  val move = CaseEnumIndex[Move].caseFromIndex(readLine())

  val computerMove = generateRandom()
  val output = move.map(m => {
  val resultString = s"${CaseEnumSerialization[Move].caseToString(m)} VS ${CaseEnumSerialization[Move].caseToString(computerMove)}-> "
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

   def generateRandom() = {
    Random.nextInt(3) match {
      case 0 => ROCK
      case 1 => PAPER
      case 2 => SCISSOR
      case _ => throw new Exception("errorissimo")
    }
}
}

@indexedEnum trait Move {
  type Index = String
  object ROCK { "0" }
  object PAPER { "1" }
  object SCISSOR { "2" }

}


  
