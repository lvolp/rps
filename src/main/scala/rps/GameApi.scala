package rps
import scala.util.Random
import rps.Move._
import rps.Response._
import rps.Result._
import io.circe.generic.auto._
import io.buildo.enumero.circe._
import wiro.annotation._
import scala.concurrent.{ExecutionContext, Future}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._
import wiro.server.akkaHttp._

@path("rps")
trait GameApi {
  @command
  def play(userMove:Move): Future[Either[Throwable, Response]]
}

class GameApiImpl(implicit ec: ExecutionContext) extends GameApi {

  override def play(userMove: Move): Future[Either[Throwable, Response]] = Future {
    val computerMove = generateRandom()
    val result =(userMove, computerMove) match {
        case (x,y) if x == y => Draw
        case (Rock,Paper) | (Paper,Scissors) | (Scissors,Rock) => Lose
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper) => Win   
    }    
    Right(Response tupled (userMove,computerMove,result))
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

