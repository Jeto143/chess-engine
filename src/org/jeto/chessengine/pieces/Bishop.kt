package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece

class Bishop(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> =
		listOf(
			MoveDirection(-1, -1, 0),
			MoveDirection(-1, 1, 0),
			MoveDirection(1, -1, 0),
			MoveDirection(1, 1, 0)
		)

	override fun toCode() = 'B'
	override fun toString() = if (color == Piece.Color.WHITE) "♗" else "♝"
}