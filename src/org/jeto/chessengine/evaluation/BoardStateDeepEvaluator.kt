package org.jeto.chessengine.evaluation

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move

/**
 * Provides the means to evaluate a board position, optionally recursively (depth > 1).
 */
interface BoardStateDeepEvaluator {
	companion object {
		const val CHECKMATE_EVALUATION = 9999f
	}

	/**
	 * Evaluates the given board state at a depth of [depth].
	 * @see findBestMove for [greediness].
	 */
	fun evaluate(boardState: BoardState, depth: Int = 1, greediness: Int? = null): Float

	/**
	 * Computes the best move of the current turn's player, using a [depth]-deep evaluation.
	 * @param greediness How many of the best moves should be considered at each computation level (null = infinite).
	 * @return The [Move] and its evaluation score.
	 */
	fun findBestMove(boardState: BoardState, depth: Int = 1, greediness: Int? = null): Pair<Move, Float>?
}