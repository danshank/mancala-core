enum Player:
    case One, Two

case class Board(
    mancalas: Map[Player, Int], 
    slots:Map[Player, Vector[Int]]
)

enum GameState:
    case ToPlay(p: Player)
    case Won(p: Player)
    case Tied

case class Game(
    board: Board,
    state: GameState
)

case class Position(
    slot: Int,
    player: Player
)

enum BoardPosition:
    case Slot(position: Position)
    case Mancala(player: Player)
