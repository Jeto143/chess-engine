package org.jeto.chessengine.analysis

/**
 * Provides the combination of all board-analyzing functionalities, plus addititonal utilities.
 */
interface BoardStateAnalyzer : GameStateAnalyzer, LegalMovesAnalyzer, ThreatAnalyzer