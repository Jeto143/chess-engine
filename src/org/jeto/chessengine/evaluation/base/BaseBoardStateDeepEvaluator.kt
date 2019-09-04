package org.jeto.chessengine.evaluation.base

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.evaluation.BoardStateDeepEvaluator
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece
import kotlin.math.min

abstract class BaseBoardStateDeepEvaluator(protected val legalMovesAnalyzer: LegalMovesAnalyzer) : BoardStateDeepEvaluator {
	override fun findBestMove(boardState: BoardState, depth: Int, greediness: Int?): Pair<Move, Float>? {
		val legalMoves = legalMovesAnalyzer.getLegalMoves(boardState)

		val movesEvaluations: Map<Move, Float> = when (greediness) {
			null -> legalMoves.map { it to evaluate(boardState + it, depth) }.toMap()
			else -> legalMoves
				.map { it to evaluate(boardState + it, 1) }
				.run {
					when (boardState.turnColor) {
						Piece.Color.WHITE -> sortedByDescending { (_, evaluation) -> evaluation }
						Piece.Color.BLACK -> sortedBy { (_, evaluation) -> evaluation }
					}
					.take(min(greediness, size))
					.map { (move, _) -> move to evaluate(boardState + move, depth - 1) }
					.toMap()
				}
		}

		return movesEvaluations.run {
			when (boardState.turnColor) {
				Piece.Color.WHITE -> filter { (_, evaluation) -> evaluation == maxBy { it.value }?.value }
				Piece.Color.BLACK -> filter { (_, evaluation) -> evaluation == minBy { it.value }?.value }
			}.toList().shuffled().first()
		}
	}
}