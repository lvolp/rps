package rps
import Move._
import Result._
import scala.util.Random

trait GameService {
    def play(userMove : Move) : GameId
    def result(id: GameId) : Option[Response]
}

class GameServiceImpl(gr: GameRepository) extends GameService with IdGenerator {
  override def play(userMove : Move) : GameId = {
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

  override def result(id : GameId) : Option[Response] = {
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
