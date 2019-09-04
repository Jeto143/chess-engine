package org.jeto.chessengine.util

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.Move

/**
 * Provides a move's string "complete" representation (with check/checkmate info, explicit position if required, etc.).
 */
class MovePrinter(private val threatAnalyzer: ThreatAnalyzer, private val legalMovesAnalyzer: LegalMovesAnalyzer) {
	fun getMoveCode(boardState: BoardState, move: Move): String {
		var moveCode = move.getCode(
			boardState.isPositionOccupied(move.toPosition),
			if (move.requiresExplicitPosition()) getExplicitPositionType(boardState, move) else null
		)

		val futureBoardState = boardState + move
		moveCode += when {
			legalMovesAnalyzer.isInCheckmate(futureBoardState) -> '#'
			threatAnalyzer.isInCheck(futureBoardState) -> '+'
			else -> ""
		}

		return moveCode
	}

	private fun getExplicitPositionType(boardState: BoardState, move: Move): Move.ExplicitPositionType? {
		val otherMove = legalMovesAnalyzer.getLegalMoves(boardState).find { it !== move && it.piece::class == move.piece::class && it.toPosition == move.toPosition }
		return if (otherMove == null) null else when {
			otherMove.fromPosition.col != move.fromPosition.col -> Move.ExplicitPositionType.COL
			else -> Move.ExplicitPositionType.ROW
		}
	}
}