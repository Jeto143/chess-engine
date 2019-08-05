package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position

interface ThreatAnalyzer {
	fun getThreatMap(boardState: BoardState): ThreatMap
	fun isUnderAttack(boardState: BoardState, position: Position, byColor: Piece.Color): Boolean
	fun isUnderAttack(boardState: BoardState, piece: Piece): Boolean
	fun isInCheck(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): Boolean
}