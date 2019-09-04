package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Piece

/**
 * Provides data akin to board threats, including a map of controlled/attacked positions.
 */
interface ThreatAnalyzer {
	/**
	 * Retrieves the [ThreatMap] of the given [boardState].
	 */
	fun getThreatMap(boardState: BoardState): ThreatMap

	/**
	 * Retrieves whether [position] is under attack by [byColor] on the given [boardState].
	 */
	fun isUnderAttack(boardState: BoardState, position: Position, byColor: Piece.Color): Boolean

	/**
	 * Retrieves whether [piece] is under attack on the given [boardState].
	 */
	fun isUnderAttack(boardState: BoardState, piece: Piece): Boolean

	/**
	 * Retrieves whether [sideColor] is in check on the given [boardState].
	 */
	fun isInCheck(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): Boolean
}