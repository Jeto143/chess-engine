package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.analysis.BoardStateAnalyzer
import org.jeto.chessengine.analysis.GameStateAnalyzer
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer

class DefaultBoardStateAnalyzer(
	gameStateAnalyzer: GameStateAnalyzer,
	threatAnalyzer: ThreatAnalyzer,
	legalMovesAnalyzer: LegalMovesAnalyzer
) : BoardStateAnalyzer, GameStateAnalyzer by gameStateAnalyzer, ThreatAnalyzer by threatAnalyzer,
	LegalMovesAnalyzer by legalMovesAnalyzer