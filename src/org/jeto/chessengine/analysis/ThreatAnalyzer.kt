package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.Position

interface ThreatAnalyzer {
	fun getThreatMap(boardState: BoardState): Map<Position, Int>
	fun isUnderAttack(boardState: BoardState, position: Position): Boolean
	fun isUnderAttack(boardState: BoardState, piece: Piece): Boolean
	fun isInCheck(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): Boolean
}