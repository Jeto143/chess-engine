package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.MutableThreatMap
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.analysis.ThreatMap
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Piece

class DefaultThreatAnalyzer : ThreatAnalyzer {
	private val threatMapCache: MutableMap<BoardState, MutableThreatMap> = mutableMapOf()

	override fun getThreatMap(boardState: BoardState): ThreatMap {
		if (boardState !in threatMapCache) {
			initThreatMapCache(boardState)

			for (piece in boardState.getPieces()) {
				for (takeDirection in piece.getTakeDirections(boardState)) {
					for (targetPosition in piece.computeTargetMovePositions(
						boardState,
						takeDirection,
						includeFinalObstacle = true
					) { boardState[it]?.color != piece.color }) {
						threatMapCache[boardState]?.get(targetPosition)?.set(piece.color,
							threatMapCache[boardState]?.get(targetPosition)?.get(piece.color)?.plus(1) as Int
						)
					}
				}
			}
		}

		return threatMapCache[boardState]!!
	}

	override fun isUnderAttack(boardState: BoardState, position: Position, byColor: Piece.Color) = getThreatMap(boardState)[position]?.get(byColor) ?: 0 > 0
	override fun isUnderAttack(boardState: BoardState, piece: Piece) = isUnderAttack(boardState, boardState.getPiecePosition(piece), piece.color.getOpposite())
	override fun isInCheck(boardState: BoardState, sideColor: Piece.Color) = isUnderAttack(boardState, boardState.getPieces(type = King::class, color = sideColor).first())

	private fun initThreatMapCache(boardState: BoardState) {
		val threatMap: MutableThreatMap = mutableMapOf()

		for (x in 1..BoardState.SIZE) {
			for (y in 1..BoardState.SIZE) {
				threatMap[Position(x, y)] = mutableMapOf(Piece.Color.WHITE to 0, Piece.Color.BLACK to 0)
			}
		}

		threatMapCache[boardState] = threatMap
	}
}