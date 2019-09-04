package org.jeto.chessengine.analysis.base

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Piece

/**
 * Provides base behavior for a [ThreatAnalyzer]. Implementations should extend this.
 */
abstract class BaseThreatAnalyzer : ThreatAnalyzer {
	override fun isUnderAttack(boardState: BoardState, position: Position, byColor: Piece.Color) = getThreatMap(boardState)[position]?.find { it.color === byColor } != null
	override fun isUnderAttack(boardState: BoardState, piece: Piece) = isUnderAttack(boardState, boardState.getPiecePosition(piece), piece.color.getOpposite())
	override fun isInCheck(boardState: BoardState, sideColor: Piece.Color) = isUnderAttack(boardState, boardState.getPieces(type = King::class, color = sideColor).first())
}