package pt.isel.pdm.chess4android

enum class Army {
    WHITE, BLACK, EMPTY
}

enum class Piece {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, EMPTY
}
enum class Columns{
    A,B,C,D,E,F,G,H
}
data class PieceAndItsPosition(val row: Int, val col: Int, val pair: Pair<Army, Piece>)