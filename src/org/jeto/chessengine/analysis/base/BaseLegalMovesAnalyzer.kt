package org.jeto.chessengine.analysis.base

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

/**
 * Provides base behavior for a [LegalMovesAnalyzer]. Implementations should extend this.
 */
abstract class BaseLegalMovesAnalyzer : LegalMovesAnalyzer, BasePieceLegalMovesAnalyzer() {
	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> = getLegalMoves(boardState).filter { it.piece == piece }
	override fun isMoveLegal(boardState: BoardState, move: Move, sideColor: Piece.Color): Boolean = move in getLegalMoves(boardState, sideColor)
	override fun isInCheckmate(boardState: BoardState, sideColor: Piece.Color): Boolean = getLegalMoves(boardState).isEmpty()
}