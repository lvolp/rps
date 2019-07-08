package rps
import Move._
import Result._

import scala.util.Random

trait GameService {
    def play(userMove : Move) : Unit
    def result : Option[Response]
}

class GameServiceImpl(gr: GameRepository) extends GameService {
  override def play(userMove : Move) : Unit = {
   val computerMove = generateRandom()
    val result =(userMove, computerMove) match {
        case (x,y) if x == y => Draw
        case (Rock,Paper) | (Paper,Scissors) | (Scissors,Rock) => Lose
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper) => Win   
    }    
    gr.saveGame(Response tupled (userMove,computerMove,result))
  }

  override def result : Option[Response] = {
    gr.getGame
  }

   def generateRandom() = {
    Random.nextInt(3) match {
      case 0 => Rock
      case 1 => Paper
      case 2 => Scissors
      case _ => throw new Exception("errorissimo")
    }
   }
}
