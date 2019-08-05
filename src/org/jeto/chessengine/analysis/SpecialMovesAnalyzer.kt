package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.moves.Move

interface SpecialMovesAnalyzer {
	fun computePotentialSpecialMoves(boardState: BoardState, piece: Piece): List<Move>
}