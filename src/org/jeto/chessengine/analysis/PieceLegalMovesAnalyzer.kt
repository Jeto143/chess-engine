package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

/**
 * Provides per-piece legal moves retrieval.
 */
interface PieceLegalMovesAnalyzer {
	/**
	 * Retrieves [piece]'s legal moves on the given [boardState].
	 *
	 * Note: this doesn't have to return *all* legal moves on the board, it may depend on the implementation.
	 */
	fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move>

	/**
	 * Retrieves whether [piece]'s [move] is legal on the given [boardState].
	 */
	fun isMoveLegal(boardState: BoardState, piece: Piece, move: Move): Boolean
}