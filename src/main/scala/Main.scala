import GameUpdates.*
import MoveValidations.*
import Utilities.*
import Models.*

object MancalaCore:

  /** @return Generates a new game
    */
  def startingGame(): Game =
    val board = startingBoard()
    val state = GameState.ToPlay(Player.One)
    Game(board, state)

  /** Creates a game of your choosing
    * @param gameState Which game state you'd like to have the game in
    * @param seedCounts Starting in Player One's left-most slot and continuing around the board counterclockwise,
    * fills each slot and mancala with the requested number of seeds. Will truncate to fourteen entries, or pad
    * with zero seeds for remaining unspecified slots if not enough seed counts are given.
    */
  def createGame(gameState: GameState, seedCounts: Int*): Game =
    val seedsToUse = if seedCounts.size < 14 then seedCounts.toArray.padTo(14, 0) else seedCounts.take(14).toArray
    val playerOneSlots = seedsToUse.take(6).toVector
    val playerOneMancala = seedsToUse(6)
    val playerTwoSlots = seedsToUse.drop(7).take(6).toVector
    val playerTwoMancala = seedsToUse(13)
    val slots = Map(Player.One -> playerOneSlots, Player.Two -> playerTwoSlots)
    val mancalas = Map(Player.One -> playerOneMancala, Player.Two -> playerTwoMancala)
    Game(Board(mancalas, slots), gameState)

  /** Updates the game state with a given move
    * @param move Which slot the player is moving seeds from
    * @param game The current game state
    * @return Updates the game state if the move is valid; otherwise, return the original game state
    */
  def makeMove(move: Position, game: Game): Game =
    if moveIsValid(move, game)
    then commitMove(move, game.board) 
    else game
