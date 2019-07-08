package rps

import io.circe.generic.auto._
import wiro.annotation._
import scala.concurrent.{ExecutionContext, Future}
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.FailSupport._
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}

@path("rps")
trait GameController {
 @command
  def play(userMove:Move): Future[Either[Throwable,Unit]]

  @query
  def result(): Future[Either[Throwable,Response]]
}


class GameControllerImpl(gs : GameService)(implicit ec: ExecutionContext) extends GameController {
  override def play(userMove:Move) : Future[Either[Throwable,Unit]] = Future {
    Right(gs.play(userMove))
  }

  override def result : Future[Either[Throwable,Response]] = Future {
    gs.result match {
        case Some(x) => Right(x)
        case None => Left(new Throwable("Error"))
      }
  }  

 

    
}
