import Utilities.*
import Models.*

package object MoveValidations:
  def slotHasSeedsToMakeMove(move: Position, board: Board): Boolean =
    try
      val seedsInSlot = getSeedCount(move, board)
      seedsInSlot > 0
    catch 
      case e: IndexOutOfBoundsException => false

  def isPlayersTurn(p: Player, state: GameState): Boolean =
    state match
      case GameState.ToPlay(player) => player == p
      case _                        => false

  def moveIsValid(move: Position, game: Game): Boolean =
      isPlayersTurn(move.player, game.state) && slotHasSeedsToMakeMove(move, game.board)