package rps
import scala.collection.concurrent.TrieMap
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import io.buildo.enumero.CaseEnumSerialization
import scala.concurrent.Future
import wiro.annotation.`package`.query
import scala.concurrent.duration._
import scala.concurrent.Await

trait GameRepository  {
    def saveGame(gameResult: Response) : Future[Int]
    def getGame(id: Int) : Future[Option[Response]]
}

object GameRepositoryImpl extends GameRepository with ColumnMapping {

  private val db = Database.forConfig("h2mem1")
  val games = TableQuery[Games]  
  val creation = games.schema.create
  Await.result(db.run(creation),5 seconds)


  override def saveGame(gameResult: Response) : Future[Int] = {
    val insert = (games returning games.map(_.id)) += GameData(0,gameResult.userMove,gameResult.computerMove,gameResult.result)
    db.run(insert)
  }

  override def getGame(id: Int) : Future[Option[Response]] =  {
    val query = games.filter(game => game.id === id)
    db.run(query.result).map(_.headOption.map(r => Response(r.userMove,r.computerMove,r.response)))
  }

}

trait ColumnMapping {

  implicit val moveColumnType = MappedColumnType.base[Move,String] (
    {m => s"$m"},
    {s => CaseEnumSerialization[Move].caseFromString(s).getOrElse(Move.Rock)}
  )

  implicit val resultColumnType = MappedColumnType.base[Result,String] (
    {r => s"$r"},
    {s => CaseEnumSerialization[Result].caseFromString(s).getOrElse(Result.Lose)}
  )

}

case class GameData(id:Int,userMove:Move,computerMove:Move,response:Result) 

class Games (tag: Tag) extends Table[GameData](tag,"GAMES") with ColumnMapping{

  def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
  def userMove = column[Move]("USER_MOVE")
  def computerMove = column[Move]("COMPUTER_MOVE")
  def result = column[Result]("RESULT")
  def * = (id, userMove, computerMove,result) <> (GameData.tupled,GameData.unapply)

} 


