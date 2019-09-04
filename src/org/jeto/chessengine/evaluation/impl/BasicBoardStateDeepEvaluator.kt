package org.jeto.chessengine.evaluation.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.evaluation.BoardStateDeepEvaluator
import org.jeto.chessengine.evaluation.BoardStateEvaluator
import org.jeto.chessengine.evaluation.base.BaseBoardStateDeepEvaluator
import org.jeto.chessengine.pieces.Piece

class BasicBoardStateDeepEvaluator(
	legalMovesAnalyzer: LegalMovesAnalyzer,
	private val criteriaEvaluators: Map<BoardStateEvaluator, Float>
) : BaseBoardStateDeepEvaluator(legalMovesAnalyzer) {
	override fun evaluate(boardState: BoardState, depth: Int, greediness: Int?): Float = when {
			legalMovesAnalyzer.isInCheckmate(boardState) -> BoardStateDeepEvaluator.CHECKMATE_EVALUATION * if (boardState.turnColor == Piece.Color.WHITE) 1 else -1
			depth > 1 -> findBestMove(boardState, depth - 1, greediness)!!.second
			// Weighted average
			else -> criteriaEvaluators
				.map { (criterionEvaluator, weight) -> criterionEvaluator.evaluate(boardState) * weight }
				.sum()
				.div(criteriaEvaluators.map { (_, weight) -> weight }.sum())
	}
}