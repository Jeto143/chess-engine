package org.jeto.chessengine.evaluation

import org.jeto.chessengine.BoardState

interface BoardStateEvaluator {
	fun evaluate(boardState: BoardState): Int
}