package org.jeto.chessengine.evaluation.criteria.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.evaluation.criteria.BoardStateCriterionEvaluator
import org.jeto.chessengine.extension.rangeLerp
import org.jeto.chessengine.pieces.*

class PiecesPositionsEvaluator : BoardStateCriterionEvaluator {
	companion object {
		val valueMap = mapOf(
			Pawn::class to arrayOf(
				0,  0,  0,  0,  0,  0,  0,  0,
				50, 50, 50, 50, 50, 50, 50, 50,
				10, 10, 20, 30, 30, 20, 10, 10,
				 5,  5, 10, 25, 25, 10,  5,  5,
				 0,  0,  0, 20, 20,  0,  0,  0,
				 5, -5,-10,  0,  0,-10, -5,  5,
				 5, 10, 10,-20,-20, 10, 10,  5,
				 0,  0,  0,  0,  0,  0,  0,  0
			),
			Knight::class to arrayOf(
				-50,-40,-30,-30,-30,-30,-40,-50,
				-40,-20,  0,  0,  0,  0,-20,-40,
				-30,  0, 10, 15, 15, 10,  0,-30,
				-30,  5, 15, 20, 20, 15,  5,-30,
				-30,  0, 15, 20, 20, 15,  0,-30,
				-30,  5, 10, 15, 15, 10,  5,-30,
				-40,-20,  0,  5,  5,  0,-20,-40,
				-50,-40,-30,-30,-30,-30,-40,-50
			),
			Bishop::class to arrayOf(
				-20,-10,-10,-10,-10,-10,-10,-20,
				-10,  0,  0,  0,  0,  0,  0,-10,
				-10,  0,  5, 10, 10,  5,  0,-10,
				-10,  5,  5, 10, 10,  5,  5,-10,
				-10,  0, 10, 10, 10, 10,  0,-10,
				-10, 10, 10, 10, 10, 10, 10,-10,
				-10,  5,  0,  0,  0,  0,  5,-10,
				-20,-10,-10,-10,-10,-10,-10,-20
			),
			Rook::class to arrayOf(
				0,  0,  0,  0,  0,  0,  0,  0,
				5, 10, 10, 10, 10, 10, 10,  5,
				-5,  0,  0,  0,  0,  0,  0, -5,
				-5,  0,  0,  0,  0,  0,  0, -5,
				-5,  0,  0,  0,  0,  0,  0, -5,
				-5,  0,  0,  0,  0,  0,  0, -5,
				-5,  0,  0,  0,  0,  0,  0, -5,
				0,  0,  0,  5,  5,  0,  0,  0
			),
			Queen::class to arrayOf(
				-20,-10,-10, -5, -5,-10,-10,-20,
				-10,  0,  0,  0,  0,  0,  0,-10,
				-10,  0,  5,  5,  5,  5,  0,-10,
				 -5,  0,  5,  5,  5,  5,  0, -5,
					0,  0,  5,  5,  5,  5,  0, -5,
				-10,  5,  5,  5,  5,  5,  0,-10,
				-10,  0,  5,  0,  0,  0,  0,-10,
				-20,-10,-10, -5, -5,-10,-10,-20
			),
			King::class to arrayOf(
				-30,-40,-40,-50,-50,-40,-40,-30,
				-30,-40,-40,-50,-50,-40,-40,-30,
				-30,-40,-40,-50,-50,-40,-40,-30,
				-30,-40,-40,-50,-50,-40,-40,-30,
				-20,-30,-30,-40,-40,-30,-30,-20,
				-10,-20,-20,-20,-20,-20,-20,-10,
				 20, 20,  0,  0,  0,  0, 20, 20,
				 20, 30, 10,  0,  0, 10, 30, 20
			)
		)

		const val MAX_SCORE = 800.0
	}

	override fun evaluate(boardState: BoardState): Float {
		val absoluteScore = boardState.getPieces()
			.map { piece -> getPieceScore(boardState, piece) * (if (piece.isWhite()) 1 else -1) }
			.sum()

		return rangeLerp(absoluteScore.toDouble(), -MAX_SCORE, MAX_SCORE, -10.0, 10.0).toFloat()
	}

	private fun getPieceScore(boardState: BoardState, piece: Piece): Int {
		val (x, y) = boardState.getPiecePosition(piece)

		val indexInValueMap = if (piece.isWhite()) (8 - y) * 8 + x - 1 else y * 7 + x - 1

		return valueMap.getValue(piece.javaClass.kotlin)[indexInValueMap]
	}
}