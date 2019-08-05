package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState

class Queen(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> =
		listOf(
			MoveDirection(-1, 0, 0),
			MoveDirection(0, -1, 0),
			MoveDirection(0, 1, 0),
			MoveDirection(1, 0, 0),
			MoveDirection(-1, -1, 0),
			MoveDirection(-1, 1, 0),
			MoveDirection(1, -1, 0),
			MoveDirection(1, 1, 0)
		)

	override fun toCode() = 'Q'
	override fun toString() = if (color == Color.WHITE) "♕" else "♛"
}