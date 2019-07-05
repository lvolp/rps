package rps
import scala.io.StdIn.readLine
import scala.util.Random
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.{CaseEnumIndex, CaseEnumSerialization}
import Move._
import Result._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.buildo.enumero.circe._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object Main extends App {
  WebServer.run
}

object Game {
  def play(move : Move) : (Move,Move,Result) = {
    val computerMove = generateRandom()
    val result =(move, computerMove) match {
        case (x,y) if x == y => Draw
        case (Rock,Paper) | (Paper,Scissors) | (Scissors,Rock) => Lose
        case (Rock,Scissors) | (Paper,Rock) | (Scissors,Paper)  => Win
        
    }    
    return (move,computerMove,result)
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

object WebServer {
  def run()= {

    implicit val system = ActorSystem("rps")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      pathPrefix("rps") {
      path("play") {
        post {
           entity(as[Request]) { move =>
             complete(Response tupled Game.play(move.userMove))
        }
        }~ options(complete())
      }
      }
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

case class Request (userMove : Move)
case class Response (userMove: Move, computerMove: Move, result: Result)