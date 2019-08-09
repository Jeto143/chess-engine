package org.jeto.chessengine.evaluation.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.evaluation.criteria.BoardStateCriterionEvaluator
import org.jeto.chessengine.evaluation.BoardStateEvaluator
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

fun <T> printAndReturnLast(vararg something: T): T {
	something.forEach { println(it) }
	return something.last()
}

class BasicBoardStateEvaluator(
	private val criteriaEvaluators: Map<BoardStateCriterionEvaluator, Float>,
	private val legalMovesAnalyzer: LegalMovesAnalyzer
) : BoardStateEvaluator {
	override fun evaluate(boardState: BoardState, depth: Int): Float {
		if (legalMovesAnalyzer.isInCheckmate(boardState)) {
			return Float.MAX_VALUE
		}

		if (depth > 1) {
			val bestMoveWithScore = findBestMove(boardState, depth - 1)
			if (bestMoveWithScore !== null) {
				val (_, evaluation) = bestMoveWithScore
				return evaluation
			}
		}
		return criteriaEvaluators
			.map { (criterionEvaluator, weight) -> criterionEvaluator.evaluate(boardState) * weight }
			.sum()
	}

	override fun findBestMove(boardState: BoardState, depth: Int): Pair<Move, Float>? {
		val legalMoves = legalMovesAnalyzer.getLegalMoves(boardState)

		val nextMoveEvaluations = legalMoves
			.map { move -> move to evaluate(boardState + move, depth) }
			.toMap()

		println(nextMoveEvaluations)

		if (nextMoveEvaluations.isEmpty()) {
			return null
		}

		return when (boardState.turnColor) {
			Piece.Color.WHITE -> nextMoveEvaluations.maxBy { (_, evaluation) -> evaluation }!!.toPair()
			Piece.Color.BLACK -> nextMoveEvaluations.minBy { (_, evaluation) -> evaluation }!!.toPair()
		}
	}
}