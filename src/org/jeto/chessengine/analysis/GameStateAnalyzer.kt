package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.GameState

/**
 * Provides game state (such as whether a board is in check, checkmate, draw, etc.) related data.
 */
interface GameStateAnalyzer {
	/**
	 * Retrieves the [GameState] of the given [boardState].
	 */
	fun getGameState(boardState: BoardState): GameState
}