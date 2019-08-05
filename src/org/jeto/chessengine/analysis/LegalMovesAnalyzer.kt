package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece

interface LegalMovesAnalyzer {
	fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move>
	fun isInCheckmate(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): Boolean
}
