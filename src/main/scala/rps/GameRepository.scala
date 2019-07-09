package rps
import scala.collection.concurrent.TrieMap

trait GameRepository  {
    def saveGame(id:GameId,gameResult: Response) : Unit
    def getGame(id: GameId) : Option[Response]

}

class GameRepositoryImpl extends GameRepository {
  private val map = TrieMap.empty[GameId,Response]
  
  override def saveGame(id:GameId, gameResult: Response) = {
    map.put(id,gameResult)
  }

  override def getGame(id: GameId) : Option[Response] = {
    map.get(id)
  }
}