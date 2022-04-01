import Models.*

package object Utilities:
  def startingBoard(): Board = 
    val mancalas = Player.values map (p => (p, 0)) toMap
    val slots = Vector.fill(6)(4)
    val playerSlots = Player.values map (p => (p, slots)) toMap
    val board = Board(mancalas, playerSlots)
    board

  def getSeedCount(position: Position, board: Board): Int =
    board.slots(position.player)(position.slot)

  def numberOfLoopsMoveWillMake(seedsBeingMoved: Int): Int =
    seedsBeingMoved / 13

  def remainingSeedsAfterLooping(seedsBeingMoved: Int): Int =
    seedsBeingMoved % 13

  def getOtherPlayer(player: Player): Player =
    player match
      case Player.One => Player.Two
      case Player.Two => Player.One

  def positionOfFinalPlacedSeed(move: Position, seedsBeingMoved: Int): BoardPosition =
    val remainder = remainingSeedsAfterLooping(seedsBeingMoved)
    val finalIndex = (move.slot + remainder) % 13
    val otherPlayer = getOtherPlayer(move.player)
    if 
      finalIndex > 6
    then
      BoardPosition.Slot(Position(finalIndex - 7, otherPlayer))
    else if 
      finalIndex == 6
    then
      BoardPosition.Mancala(move.player)
    else
      BoardPosition.Slot(Position(finalIndex, move.player))

  def sumOfSlots(player: Player, board: Board) =
    board.slots(player).sum

  def playerHasEmptySlots(player: Player, board: Board): Boolean =
    sumOfSlots(player, board) == 0

  def getUpdatedSeedCount(seedsToMove: Int, move: Position, slotToUpdate: Position, existingSeedCount: Int): Int =
    val loops = numberOfLoopsMoveWillMake(seedsToMove)
    val remainder = remainingSeedsAfterLooping(seedsToMove)
    val relativePosition = 
      if 
        slotToUpdate.player == move.player
      then
        slotToUpdate.slot - move.slot
      else
        slotToUpdate.slot - move.slot + 7
    val startingSeedCount = if relativePosition == 0 then 0 else existingSeedCount
    if Math.floorMod(relativePosition, 13) <= remainder && relativePosition > 0
    then startingSeedCount + loops + 1 
    else startingSeedCount + loops

  def getUpdatedMancalaSeedCount(seedsToMove: Int, move: Position, existingSeedCount: Int): Int =
    val loops = numberOfLoopsMoveWillMake(seedsToMove)
    val remainder = remainingSeedsAfterLooping(seedsToMove)
    val remainderWillEndUpInMancala = 6 - move.slot <= remainder
    if remainderWillEndUpInMancala
    then existingSeedCount + loops + 1 
    else existingSeedCount + loops

  def getNewSlotsForPlayer(move: Position, player: Player, board: Board): Vector[Int] =
    val seedsToMove = getSeedCount(move, board)
    val previousSlots = board.slots(player)
    previousSlots.zipWithIndex.map { case(seedCount, i) => 
      val positionToUpdate = Position(i, player)
      getUpdatedSeedCount(seedsToMove, move, positionToUpdate, getSeedCount(positionToUpdate, board))
    };
