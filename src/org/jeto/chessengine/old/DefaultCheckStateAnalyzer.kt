package org.jeto.chessengine.old

import org.jeto.chessengine.*
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.King

class DefaultCheckStateAnalyzer(private val threatAnalyzer: ThreatAnalyzer) :
	CheckStateAnalyzer {
	private var checkStateCache: MutableMap<BoardState, CheckState> = mutableMapOf()

	override fun getCheckState(boardState: BoardState, sideColor: Piece.Color): CheckState {
		if (boardState !in checkStateCache) {
			checkStateCache[boardState] = computeCheckState(boardState, sideColor)
		}

		return checkStateCache[boardState]!!
	}

	private fun computeCheckState(boardState: BoardState, sideColor: Piece.Color): CheckState {
		val king = boardState.getPieces(color = sideColor, type = King::class).first()
		if (!threatAnalyzer.isUnderAttack(boardState, king)) {
			return CheckState.NONE
		}
		val kingPosition = boardState.getPiecePosition(king)
		for (moveDirection: Piece.MoveDirection in king.getMoveDirections(boardState)) {    // FIXME: include takeDirections (in case of a mode that enables different take directions)
			for (targetPosition in king.computeTargetMovePositions(
				boardState,
				moveDirection,
				includeObstacles = true
			) { boardState[it]?.color != king.color }) {
				if (!threatAnalyzer.isUnderAttack(boardState + Move(
						king,
						kingPosition,
						targetPosition
					), targetPosition)
				) {
					return CheckState.CHECK
				}
			}
		}

		return CheckState.CHECKMATE
	}
}