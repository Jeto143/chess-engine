package org.jeto.chessengine.analysis.base

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.PieceLegalMovesAnalyzer
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

/**
 * Provides base behavior for a [PieceLegalMovesAnalyzer]. Implementations should extend this.
 */
abstract class BasePieceLegalMovesAnalyzer : PieceLegalMovesAnalyzer {
	override fun isMoveLegal(boardState: BoardState, piece: Piece, move: Move): Boolean = move in getLegalMoves(boardState, piece)
}