object Models:
  enum Player:
    case One, Two

  case class Board(
    mancalas: Map[Player, Int], 
    slots:Map[Player, Vector[Int]]
  ) derives CanEqual

  enum GameState:
    case ToPlay(p: Player)
    case Won(p: Player)
    case Tied

  case class Game(
    board: Board,
    state: GameState
  ) derives CanEqual

  case class Position(
    slot: Int,
    player: Player
  ) derives CanEqual

  enum BoardPosition:
    case Slot(position: Position)
    case Mancala(player: Player)
