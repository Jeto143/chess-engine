package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.moves.Move

interface LegalMovesAnalyzer {
	fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move>
	fun isMoveLegal(boardState: BoardState, move: Move): Boolean
	fun isInCheckMate(boardState: BoardState, color: Piece.Color = boardState.turnColor): Boolean
}