package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.GameState
import org.jeto.chessengine.analysis.GameStateAnalyzer
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer

class DefaultGameStateAnalyzer(
	private val threatAnalyzer: ThreatAnalyzer,
	private val legalMovesAnalyzer: LegalMovesAnalyzer
) : GameStateAnalyzer {
	override fun getGameState(boardState: BoardState): GameState = when {
		boardState.getPieces().size <= 2 -> GameState.DRAW
		legalMovesAnalyzer.getLegalMoves(boardState).isEmpty() -> if (threatAnalyzer.isInCheck(boardState)) GameState.CHECKMATE else GameState.DRAW
		else -> if (threatAnalyzer.isInCheck(boardState)) GameState.CHECK else GameState.ONGOING
	}
}