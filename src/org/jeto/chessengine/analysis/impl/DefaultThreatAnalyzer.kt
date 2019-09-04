package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.MutableThreatMap
import org.jeto.chessengine.analysis.ThreatMap
import org.jeto.chessengine.analysis.base.BaseThreatAnalyzer
import org.jeto.chessengine.pp

class DefaultThreatAnalyzer : BaseThreatAnalyzer() {
	private val threatMapCache: MutableMap<BoardState, ThreatMap> = mutableMapOf()

	override fun getThreatMap(boardState: BoardState): ThreatMap {
		if (boardState !in threatMapCache) {
			threatMapCache[boardState] = computeThreatMap(boardState)
		}
		return threatMapCache[boardState]!!
	}

	private fun computeThreatMap(boardState: BoardState): ThreatMap {
		val threatMap = initThreatMapCache()

		for (piece in boardState.getPieces()) {
			for (takeDirection in piece.getTakeDirections(boardState)) {
				for (targetPosition in piece.computeTargetMovePositions(
					boardState,
					takeDirection,
					includeFinalObstacle = true
				)) {
					threatMap[targetPosition]!!.add(piece)
				}
			}
		}

		return threatMap
	}

	private fun initThreatMapCache(): MutableThreatMap {
		val threatMap: MutableThreatMap = mutableMapOf()

		for (x in 1..BoardState.SIZE) {
			for (y in 1..BoardState.SIZE) {
				threatMap[Position(x, y)] = mutableListOf()
			}
		}

		return threatMap
	}

	fun clearCache() {
		threatMapCache.clear()
	}
}