package rps

import io.circe.generic.auto._
import wiro.annotation._
import scala.concurrent.{ExecutionContext, Future}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import HashId._

@path("rps")
trait GameController  {
 @command
  def play(userMove:Move): Future[Either[Throwable,IdType]]

  @query
  def result(id: IdType): Future[Either[Throwable,Response]]
}

class GameControllerImpl(gs: GameService)(implicit ec: ExecutionContext) extends GameController {
  override def play(userMove:Move) : Future[Either[Throwable,IdType]] = Future {
    Right(gs.play(userMove))
  }

  override def result(id: IdType) : Future[Either[Throwable,Response]] = Future {
    gs.result(id) match {
        case Some(x) => Right(x)
        case None => Left(new Throwable("Error"))
    }
  }    
}
