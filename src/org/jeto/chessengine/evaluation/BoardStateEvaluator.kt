package org.jeto.chessengine.evaluation

import org.jeto.chessengine.BoardState

/**
 * Provides the means to evaluate a board position.
 */
interface BoardStateEvaluator {
	/**
	 * Evaluates the given [boardState], regardless of any future move (current state only).
	 */
	fun evaluate(boardState: BoardState): Float
}