package rps

import scala.collection.concurrent.TrieMap

trait GameRepository {
    def saveGame(gameResult: Response) : Unit
    def getGame : Option[Response]

}

class GameRepositoryImpl extends GameRepository {
  private val map = TrieMap.empty[String,Response]
  
  override def saveGame(gameResult: Response) = {
    map.put("game",gameResult)
  }

  override def getGame : Option[Response] = {
    map.get("game")
  }
}