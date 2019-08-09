package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState

class Pawn(color: Color) : Piece(color) {
	override fun getMoveDirections(boardState: BoardState): List<MoveDirection> =
		when (boardState.getPiecePosition(this).row) {
			if (isWhite()) 7 else 2 -> listOf()
			else -> listOf(MoveDirection(0, if (isWhite()) 1 else -1, 1))
		}

	override fun getTakeDirections(boardState: BoardState): List<MoveDirection> =
		when (boardState.getPiecePosition(this).row) {
			if (isWhite()) 7 else 2 -> listOf()
			else -> listOf(
				MoveDirection(-1, if (isWhite()) 1 else -1, 1),
				MoveDirection(1, if (isWhite()) 1 else -1, 1)
			)
		}

	override fun toCode(): Nothing? = null
	override fun toString() = if (isWhite()) "♙" else "♟"
}