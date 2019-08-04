package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece

class Knight(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> =
		listOf(
			MoveDirection(-2, -1, 1),
			MoveDirection(-2, 1, 1),
			MoveDirection(-1, -2, 1),
			MoveDirection(-1, 2, 1),
			MoveDirection(1, -2, 1),
			MoveDirection(1, 2, 1),
			MoveDirection(2, -1, 1),
			MoveDirection(2, 1, 1)
		)

	override fun toCode() = 'N'
	override fun toString() = if (color == Piece.Color.WHITE) "♘" else "♞"
}