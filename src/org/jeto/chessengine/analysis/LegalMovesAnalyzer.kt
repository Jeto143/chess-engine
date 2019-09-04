package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

/**
 * Provides a way to compute legal moves on a board, either globally or per-piece.
 */
interface LegalMovesAnalyzer : PieceLegalMovesAnalyzer {
	/**
	 * Retrieves [sideColor]'s all legal moves on the given [boardState].
	 */
	fun getLegalMoves(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): List<Move>

	/**
	 * Retrieves whether [sideColor]'s [move] is legal on the given [boardState].
	 */
	fun isMoveLegal(boardState: BoardState, move: Move, sideColor: Piece.Color = boardState.turnColor): Boolean

	/**
	 * Retrieves whether [sideColor] is in checkmate on the given [boardState].
	 */
	fun isInCheckmate(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): Boolean
}