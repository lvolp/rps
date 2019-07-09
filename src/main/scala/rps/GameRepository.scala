package rps
import HashId._
import scala.collection.concurrent.TrieMap

trait GameRepository  {
    def saveGame(id:IdType,gameResult: Response) : Unit
    def getGame(id: IdType) : Option[Response]

}

class GameRepositoryImpl extends GameRepository {
  private val map = TrieMap.empty[IdType,Response]
  
  override def saveGame(id:IdType, gameResult: Response) = {
    map.put(id,gameResult)
  }

  override def getGame(id: IdType) : Option[Response] = {
    map.get(id)
  }
}