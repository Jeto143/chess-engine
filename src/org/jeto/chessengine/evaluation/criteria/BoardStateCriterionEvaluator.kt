package org.jeto.chessengine.evaluation.criteria

import org.jeto.chessengine.BoardState

interface BoardStateCriterionEvaluator {
	fun evaluate(boardState: BoardState): Float
}