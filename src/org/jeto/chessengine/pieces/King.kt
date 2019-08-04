package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece

class King(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> =
		listOf(
			MoveDirection(-1, 0, 1),
			MoveDirection(0, -1, 1),
			MoveDirection(0, 1, 1),
			MoveDirection(1, 0, 1),
			MoveDirection(-1, -1, 1),
			MoveDirection(-1, 1, 1),
			MoveDirection(1, -1, 1),
			MoveDirection(1, 1, 1)
		)

	override fun toCode() = 'K'
	override fun toString() = if (color == Piece.Color.WHITE) "♔" else "♚"
}