package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.King

abstract class CastlingMove(king: King, fromPosition: Position, toPosition: Position) : Move(king, fromPosition, toPosition) {
	override fun perform(boardState: BoardState): BoardState {
		val rookFromPosition = getRookStartPosition()
		val rookToPosition = getRookEndPosition()

		return super.perform(boardState)
			.movePiece(boardState[rookFromPosition]!!, rookFromPosition, rookToPosition, swapTurnColor = false)
			.disableCastling(piece.color)
	}

	abstract fun getRookStartPosition(): Position
	abstract fun getRookEndPosition(): Position
}