package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.King

class ShortCastlingMove(king: King, modifier: Modifier = Modifier.NONE) : Move(
		king,
		if (king.color == Piece.Color.WHITE) Position.fromCode("e1") else Position.fromCode("e8"),
		if (king.color == Piece.Color.WHITE) Position.fromCode("g1") else Position.fromCode("g8"),
		modifier
) {
	override fun perform(boardState: BoardState): BoardState {
		val rookFromPosition = if (piece.isWhite()) Position.fromCode("h1") else Position.fromCode("h8")
		val rookToPosition = if (piece.isWhite()) Position.fromCode("f1") else Position.fromCode("f8")

		return super.perform(boardState)
			.movePiece(boardState[rookFromPosition]!!, rookFromPosition, rookToPosition, swapTurnColor = false)
			.disableCastling(piece.color)
	}

	override fun toString(): String = "O-O"
}