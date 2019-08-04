package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.moves.Move

interface LegalSpecialMovesAnalyzer {
	fun computeLegalSpecialMoves(boardState: BoardState, piece: Piece): List<Move>
}