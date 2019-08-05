package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position

class Pawn(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> {
		val piecePosition: Position = boardState.getPiecePosition(this)

		val moveDirections = mutableListOf<MoveDirection>()

		if ((color == Color.WHITE && piecePosition.row == 7) || (color == Color.BLACK && piecePosition.row == 2)) {
			moveDirections.add(MoveDirection(0, if (color == Color.WHITE) 1 else -1, 1))
		}

		if ((color == Color.WHITE && piecePosition.row == 2) || (color == Color.BLACK && piecePosition.row == 7)) {
			moveDirections.add(MoveDirection(0, if (color == Color.WHITE) 2 else -2, 1))
		}

		return moveDirections
	}

	override fun getTakeDirections(boardState: BoardState): List<MoveDirection> =
		listOf(
			MoveDirection(-1, if (color == Color.WHITE) 1 else -1, 1),
			MoveDirection(1, if (color == Color.WHITE) 1 else -1, 1)
		)

	override fun toCode(): Nothing? = null
	override fun toString() = if (color == Color.WHITE) "♙" else "♟"
}