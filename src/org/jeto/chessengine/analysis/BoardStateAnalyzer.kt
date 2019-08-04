package org.jeto.chessengine.analysis

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.CheckState
import org.jeto.chessengine.Piece

interface BoardStateAnalyzer : LegalMovesAnalyzer, ThreatAnalyzer {
	fun getCheckState(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): CheckState
}