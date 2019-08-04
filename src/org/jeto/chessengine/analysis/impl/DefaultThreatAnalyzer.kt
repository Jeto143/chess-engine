package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.pieces.King

class DefaultThreatAnalyzer : ThreatAnalyzer {
	private val threatMapCache: MutableMap<BoardState, MutableMap<Position, Int>> = mutableMapOf()

	override fun getThreatMap(boardState: BoardState): Map<Position, Int> {
		if (boardState !in threatMapCache) {
			threatMapCache[boardState] = mutableMapOf()
			for (piece in boardState.getPieces()) {
				for (takeDirection in piece.getTakeDirections(boardState)) {
					for (targetPosition in piece.computeTargetMovePositions(
						boardState,
						takeDirection,
						includeObstacles = true
					) { (boardState[it]?.color ?: piece.color) != piece.color }) {
						threatMapCache[boardState]!![targetPosition] = threatMapCache[boardState]!![targetPosition] ?: 0 + 1
					}
				}
			}
		}

		return threatMapCache[boardState]!!
	}

	override fun isUnderAttack(boardState: BoardState, position: Position) = getThreatMap(boardState)[position] ?: 0 > 0
	override fun isUnderAttack(boardState: BoardState, piece: Piece) = isUnderAttack(boardState, boardState.getPiecePosition(piece))
	override fun isInCheck(boardState: BoardState, sideColor: Piece.Color) = isUnderAttack(boardState, boardState.getPieces(type = King::class, color = sideColor).first())
}