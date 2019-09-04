package org.jeto.chessengine.evaluation.impl.criteria

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.evaluation.BoardStateEvaluator
import org.jeto.chessengine.extension.rangeLerp
import org.jeto.chessengine.pieces.*

class PiecesValuesEvaluator : BoardStateEvaluator {
	companion object {
		val values = mapOf(
			Pawn::class to 100,
			Knight::class to 320,
			Bishop::class to 330,
			Rook::class to 500,
			Queen::class to 900,
			King::class to 20000
		)

		const val MAX_SCORE = 1000.0
	}

	override fun evaluate(boardState: BoardState): Float {
		val absoluteScore = boardState.getPieces()
			.map { piece -> getPieceScore(piece) * (if (piece.isWhite()) 1 else -1) }
			.sum()

		return rangeLerp(absoluteScore.toDouble(), -MAX_SCORE, MAX_SCORE, -10.0, 10.0).toFloat()
	}

	private fun getPieceScore(piece: Piece): Int = values.getValue(piece.javaClass.kotlin)
}