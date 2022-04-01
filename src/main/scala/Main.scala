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

  /** Create a game of your choosing
    * @param seedCounts Starting in Player One's left-most slot and continuing around the board counterclockwise,
    * fills each slot and mancala with the requested number of seeds. Will truncate to fourteen entries, or pad
    * with zero seeds for remaining unspecified slots if not enough seed counts are given.
    * @return The game with player one to play
    */
  def createGame(seedCounts: Int*): Game =
    val seedsToUse = if seedCounts.size < 14 then seedCounts.toArray.padTo(14, 0) else seedCounts.take(14).toArray
    val playerOneSlots = seedsToUse.take(6).toVector
    val playerOneMancala = seedsToUse(6)
    val playerTwoSlots = seedsToUse.drop(7).take(6).toVector
    val playerTwoMancala = seedsToUse(13)
    val slots = Map[Player, Vector[Int]] (Player.One -> playerOneSlots, Player.Two -> playerTwoSlots)
    val mancalas = Map[Player, Int] (Player.One -> playerOneMancala, Player.Two -> playerTwoMancala)
    Game(Board(mancalas, slots), GameState.ToPlay(Player.One))

  /** Update the game state with a given move
    * @param move Which slot the player is moving seeds from
    * @param game The current game state
    * @return Update the game state if the move is valid; otherwise, return the original game state
    */
  def makeMove(move: Position, game: Game): Game =
    if moveIsValid(move, game)
    then commitMove(move, game.board) 
    else game
