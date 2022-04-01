import Utilities.*
import Models.*

package object GameUpdates:
  def stealSeeds(position: Position, board: Board): Board =
    val seedsInPosition = getSeedCount(position, board)
    val opposingPosition = Position(5 - position.slot, getOtherPlayer(position.player))
    val seedsInOpposingPosition = getSeedCount(opposingPosition, board)
    val seedsInPlayersMancala = board.mancalas(position.player)
    val updatedMancalas = board.mancalas + (position.player -> (seedsInPlayersMancala + seedsInPosition + seedsInOpposingPosition))
    val updatedSlots: Map[Player, Vector[Int]] = board.slots map ((player, slots) => if player == position.player
      then
        (player, slots.updated(position.slot, 0))
      else
        (player, slots.updated(opposingPosition.slot, 0))
    ) toMap
    val updatedBoard = Board(updatedMancalas, updatedSlots)
    updatedBoard

  def placeAllSeedsInMancalas(game: Game): Game =
    val playerSlotCounts = 
      Player.values map 
      (player => (player, sumOfSlots(player, game.board))) toMap
    val updatedMancalas = 
      game.board.mancalas map 
      ((player, seedCount) => (player, playerSlotCounts(player) + seedCount)) toMap
    val updatedSlots: Map[Player, Vector[Int]] = 
      Player.values map 
      (player => (player, Vector.fill(6)(0))) toMap
    val finalBoard = Board(updatedMancalas, updatedSlots)
    if updatedMancalas(Player.One) == updatedMancalas(Player.Two)
    then
      Game(finalBoard, GameState.Tied)
    else
      val (winningPlayer, _) = updatedMancalas.maxBy { case(player, seedCount) => seedCount }
      Game(finalBoard, GameState.Won(winningPlayer))

  def checkForGameOver(game: Game): Game =
    val oneSideIsEmpty = 
      Player.values map (player => playerHasEmptySlots(player, game.board)) reduce ((a, b) => a || b)
    if oneSideIsEmpty
    then
      placeAllSeedsInMancalas(game)
    else
      game

  def commitMove(move: Position, board: Board): Game =
    val seedsToMove = getSeedCount(move, board)
    val existingMancalaSeedCount = board.mancalas(move.player)
    val newMancalaCount = getUpdatedMancalaSeedCount(seedsToMove, move, existingMancalaSeedCount)
    val newSlots = board.slots map ((player, slots) => {
      (player, getNewSlotsForPlayer(move, player, board))
    }) toMap
    val updatedMancalas = board.mancalas + (move.player -> newMancalaCount)
    val boardAfterMovingStones = Board(updatedMancalas, newSlots)
    val finalPlacedSeedPosition = positionOfFinalPlacedSeed(move, seedsToMove)
    finalPlacedSeedPosition match
      case BoardPosition.Slot(position) =>
        if position.player == move.player && getSeedCount(position, boardAfterMovingStones) == 1
        then
          val finalBoard = stealSeeds(position, boardAfterMovingStones)
          val updatedGame = Game(finalBoard, GameState.ToPlay(getOtherPlayer(move.player)))
          checkForGameOver(updatedGame)
        else
          val updatedGame = Game(boardAfterMovingStones, GameState.ToPlay(getOtherPlayer(move.player)))
          checkForGameOver(updatedGame)
      case BoardPosition.Mancala(player) => 
        val updatedGame = Game(boardAfterMovingStones, GameState.ToPlay(move.player))
        checkForGameOver(updatedGame)
