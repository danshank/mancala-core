import org.junit.Test
import org.junit.Assert.*
import MancalaCore.*
import Models.*

class MoveTests:
  @Test def EmptySlotMovesDoNotUpdateGameState(): Unit = 
    val startingGame = createGame(GameState.ToPlay(Player.One), 1)
    val moveWithNoSeeds = Position(1, Player.One)
    val moveResult = makeMove(moveWithNoSeeds, startingGame)
    assertEquals(startingGame, moveResult)

  @Test def BasicStartingMove(): Unit =
    val newGame = startingGame()
    val moveFirstSlot = Position(0, Player.One)
    val expectedBoard = createGame(GameState.ToPlay(Player.Two), 0, 5, 5, 5, 5, 4, 0, 4, 4, 4, 4, 4, 4, 0)
    val actualBoard = makeMove(moveFirstSlot, newGame)
    assertEquals(expectedBoard, actualBoard)

  @Test def PlayerOneGetsAnotherTurn(): Unit =
    val newGame = startingGame()
    val moveToGoAgain = Position(2, Player.One)
    val expectedBoard = createGame(GameState.ToPlay(Player.One), 4, 4, 0, 5, 5, 5, 1, 4, 4, 4, 4, 4, 4, 0)
    val actualBoard = makeMove(moveToGoAgain, newGame)
    assertEquals(expectedBoard, actualBoard)

  @Test def SeedsGetCaptured(): Unit =
    val primeTimeToSteal = createGame(GameState.ToPlay(Player.One), 0, 0, 0, 0, 1, 0, 10, 11)
    val moveToSteal = Position(4, Player.One)
    val expectedBoard = createGame(GameState.Won(Player.One), 0, 0, 0, 0, 0, 0, 22)
    val actualBoard = makeMove(moveToSteal, primeTimeToSteal)
    assertEquals(expectedBoard, actualBoard)

  @Test def EndUpInATie(): Unit =
    val closeToTheEnd = createGame(GameState.ToPlay(Player.One), 0, 0, 0, 0, 0, 1, 12, 4, 3, 3, 3)
    val moveToEndIt = Position(5, Player.One)
    val expectedBoard = createGame(GameState.Tied, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 13)
    val actualBoard = makeMove(moveToEndIt, closeToTheEnd)
    assertEquals(expectedBoard, actualBoard)