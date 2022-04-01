import org.junit.Test
import org.junit.Assert.*
import MancalaCore.*
import Models.*

class MoveTests:
  @Test def EmptySlotMovesNotAllowed(): Unit = 
    val startingGame = createGame(GameState.ToPlay(Player.One), 1)
    val moveWithNoSeeds = Position(1, Player.One)
    val moveResult = makeMove(moveWithNoSeeds, startingGame)
    assertEquals(startingGame, moveResult)
