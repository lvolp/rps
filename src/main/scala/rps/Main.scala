package rps
import scala.util.Random
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import Move._
import Response._
import Result._
import io.circe.generic.auto._
import io.buildo.enumero.circe._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import wiro.annotation._
import scala.concurrent.{ExecutionContext, Future}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._

@enum trait Move {
  object Rock
  object Paper
  object Scissors
}

@enum trait Result {
  object Win
  object Lose
  object Draw
}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import wiro.Config
import wiro.server.akkaHttp._

object WebServer extends App with RouterDerivationModule {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = null

  val gameRouter = deriveRouter[GameApi](new GameApiImpl)

  val rpcServer = new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  )

}

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
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper)  => Win   
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

case class Request (userMove : Move)
case class Response (userMove: Move, computerMove: Move, result: Result)