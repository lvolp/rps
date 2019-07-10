package rps
import Move._
import Result._
import scala.util.Random
import scala.concurrent.{Future, ExecutionContext, Await}
import scala.util.{Success, Failure}
import scala.concurrent.duration._
trait GameService {
    def play(userMove : Move)(implicit ec: ExecutionContext) : Future[Int]
    def result(id: Int) : Future[Option[Response]]
}

class GameServiceImpl(gr: GameRepository) extends GameService {
  
  def play(userMove : Move)(implicit ec: ExecutionContext) : Future[Int] = {
    val computerMove = generateRandom()
    val result =(userMove, computerMove) match {
        case (x,y) if x == y => Draw
        case (Rock,Paper) | (Paper,Scissors) | (Scissors,Rock) => Lose
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper) => Win   
    }    
    val game = Response(userMove,computerMove,result)
    gr.saveGame(game)
   }

  override def result(id : Int) : Future[Option[Response]] = {
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
