package rps
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
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import wiro.Config
import wiro.server.akkaHttp._
import akka.http.scaladsl.model._

import java.util.concurrent.atomic.AtomicInteger

object WebServer extends App with RouterDerivationModule {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  implicit def throwableResponse: ToHttpResponse[Throwable] = error => 
     HttpResponse(
      status = StatusCodes.InternalServerError,
      entity = HttpEntity(
        ContentType(MediaTypes.`application/json`),
        s"""{"error":${error.getMessage()}}"""))

  val gameRepository : GameRepository = GameRepositoryImpl
  val gameService : GameService = new GameServiceImpl(gameRepository)
  val gameController = new GameControllerImpl(gameService)
  val gameRouter = deriveRouter[GameController](gameController)

  new HttpRPCServer(
    config = Config("localhost", 8080),
    routers = List(gameRouter)
  )
}

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

case class Response (userMove: Move, computerMove: Move, result: Result)
