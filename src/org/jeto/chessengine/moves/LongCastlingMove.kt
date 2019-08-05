package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.King

class LongCastlingMove(king: King, modifier: Modifier = Modifier.NONE) : Move(
		king,
		if (king.color == Piece.Color.WHITE) Position.fromCode("e1") else Position.fromCode("e8"),
		if (king.color == Piece.Color.WHITE) Position.fromCode("c1") else Position.fromCode("c8"),
		modifier
) {
	override fun perform(boardState: BoardState): BoardState {
		val rookFromPosition = if (piece.isWhite()) Position.fromCode("a1") else Position.fromCode("a8")
		val rookToPosition = if (piece.isWhite()) Position.fromCode("d1") else Position.fromCode("d8")

		return super.perform(boardState)
			.movePiece(boardState[rookFromPosition]!!, rookFromPosition, rookToPosition, swapTurnColor = false)
			.disableCastling(piece.color)
	}
}