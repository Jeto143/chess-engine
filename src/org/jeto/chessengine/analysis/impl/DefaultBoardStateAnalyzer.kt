package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.CheckState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.analysis.BoardStateAnalyzer
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer

class DefaultBoardStateAnalyzer(legalMovesAnalyzer: LegalMovesAnalyzer, threatAnalyzer: ThreatAnalyzer) :
	BoardStateAnalyzer, LegalMovesAnalyzer by legalMovesAnalyzer, ThreatAnalyzer by threatAnalyzer {
	override fun getCheckState(boardState: BoardState, sideColor: Piece.Color): CheckState {
		return when {
			isInCheckMate(boardState, sideColor) -> CheckState.CHECKMATE
			isInCheck(boardState) -> CheckState.CHECK
			else -> CheckState.NONE
		}
	}
}