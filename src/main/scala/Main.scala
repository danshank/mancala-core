import GameUpdates.*
import MoveValidations.*
import Utilities.*

object Mancala:
  def startingGame(): Game =
    val board = startingBoard()
    val state = GameState.ToPlay(Player.One)
    Game(board, state)

  def makeMove(move: Position, game: Game): Game =
      if MoveValidations.moveIsValid(move, game)
      then GameUpdates.commitMove(move, game.board) 
      else game
