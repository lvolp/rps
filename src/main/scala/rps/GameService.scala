package rps
import Move._
import Result._
import ID._
import scala.util.Random

trait GameService {
    def play(userMove : Move) : IdType
    def result(id: IdType) : Option[Response]
}

class GameServiceImpl(gr: GameRepository) extends GameService {
  override def play(userMove : Move) : IdType = {
   val computerMove = generateRandom()
    val result =(userMove, computerMove) match {
        case (x,y) if x == y => Draw
        case (Rock,Paper) | (Paper,Scissors) | (Scissors,Rock) => Lose
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper) => Win   
    }    
    val game = Response(userMove,computerMove,result)
    val id = generateGameId(game)
    gr.saveGame(id,game)
    id
  }

  override def result(id : IdType) : Option[Response] = {
    gr.getGame(id)
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
