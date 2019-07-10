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
  def play(userMove:Move): Future[Either[Throwable,Int]]

  @query
  def result(id: Int): Future[Either[Throwable,Response]]
}

class GameControllerImpl(gs: GameService)(implicit ec: ExecutionContext) extends GameController  {
  override def play(userMove:Move) : Future[Either[Throwable,Int]] = gs.play(userMove) map {
    case i:Int => Right(i)
  }
  

  override def result(id: Int) : Future[Either[Throwable,Response]] = 
    gs.result(id) map {
      case Some(x) => Right(x)
      case None => Left(new Exception("Not found"))
    }
  
}
