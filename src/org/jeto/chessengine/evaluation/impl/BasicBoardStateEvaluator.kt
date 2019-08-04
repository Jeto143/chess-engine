package org.jeto.chessengine.evaluation.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.evaluation.BoardStateEvaluator

class BasicBoardStateEvaluator : BoardStateEvaluator {
	override fun evaluate(boardState: BoardState): Int {
		return 0
	}
}