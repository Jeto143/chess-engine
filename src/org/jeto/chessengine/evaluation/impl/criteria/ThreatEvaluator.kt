package org.jeto.chessengine.evaluation.impl.criteria

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.analysis.ThreatMap
import org.jeto.chessengine.console.ConsoleDebuggable
import org.jeto.chessengine.console.impl.ConsoleDebugger
import org.jeto.chessengine.evaluation.BoardStateEvaluator
import org.jeto.chessengine.extension.rangeLerp
import org.jeto.chessengine.pieces.*
import org.jeto.chessengine.pp
import kotlin.math.min

class ThreatEvaluator(private val threatAnalyzer: ThreatAnalyzer) : BoardStateEvaluator, ConsoleDebuggable by ConsoleDebugger() {
	companion object {
		val values = mapOf(
			Pawn::class to 1f,
			Knight::class to 3.2f,
			Bishop::class to 3.3f,
			Rook::class to 5f,
			Queen::class to 9f,
			King::class to 20f
		)

		const val MAX_SCORE = 100.0
	}

	override fun evaluate(boardState: BoardState): Float {
		val threatMap = threatAnalyzer.getThreatMap(boardState)

		var evaluation = 0.0
		for (x in 1..BoardState.SIZE) {
			for (y in 1..BoardState.SIZE) {
				val position = Position(x, y)

				evaluation += getPositionScore4(boardState, threatMap, position)
			}
		}

		debug("SCORE: ", evaluation)
		val toFloat = rangeLerp(evaluation, -MAX_SCORE,
			MAX_SCORE, -10.0, 10.0, clamped = false).toFloat()
		debug("lerped: ", toFloat)
		return toFloat
	}

	private fun getPositionScore4(boardState: BoardState, threatMap: ThreatMap, position: Position): Float {
		val piece = boardState[position]

		debug()
		debug(position, piece ?: "(none)")

		val threateningPieces = threatMap.getValue(position).filter { piece !is King || it.color != piece.color }
		debug("threateningPieces", threateningPieces)

		val targetValue: Float = if (piece != null) values.getValue(piece.javaClass.kotlin).toFloat() else 0.1f
		debug("targetValue", targetValue)

		val attackingPieces = threateningPieces.filter { piece == null || it.color != piece.color }
		debug("attackingPieces", attackingPieces)
		val attackingPiecesValues = attackingPieces
			.map { values.getValue(it.javaClass.kotlin) }
			.sortedBy { it }
		val defendingPieces = threateningPieces.filter { piece != null && it.color == piece.color }
		val defendingPiecesValues = defendingPieces
			.map { values.getValue(it.javaClass.kotlin) }
			.sortedBy { it }
		debug("defendingPieces", defendingPieces)

		if (attackingPieces.isNotEmpty()) {
			var bestDiff: Float? = null
			var diff: Float
			var win = targetValue
			var loss = 0f
			val exchangeLength = min(attackingPieces.size, defendingPieces.size)
			if (exchangeLength > 0) {
				for (i in 0 until exchangeLength) {
					loss += attackingPiecesValues[i]
					diff = win - loss
					if (bestDiff == null || diff > bestDiff) {
						bestDiff = diff
					}
					win += defendingPiecesValues[i]
				}
			}
			else {
				bestDiff = win
			}

			val attackInitiativeFactor = if (piece != null && piece.color != boardState.turnColor) 10 else 1
			debug("attackInitiativeFactor", attackInitiativeFactor)

			if (bestDiff!! > 0) {
				return (5 * bestDiff * attackInitiativeFactor * (if (attackingPieces.first().isWhite()) 1 else -1)).also { debug("final", it) }
			}
		}

		return threateningPieces
			.map {
				val reversedPieceValue = 0.1f * (20f - values.getValue(it.javaClass.kotlin))
				reversedPieceValue * 0.2f * (if (it.isWhite()) 1 else -1)
			}
			.sum()
			.also { debug("finalV", it) }
	}

//	private fun getPositionScore3(boardState: BoardState, threatMap: ThreatMap, position: Position): Float {
//		val piece = boardState[position]
//
//		debug(position, if (piece != null) piece else "(none)")
//
//		val threateningPieces = threatMap.getValue(position)
//		val attackingPieces = threateningPieces.filter { piece == null || it.color != piece.color }
//		val defendingPieces = threateningPieces.filter { piece != null && it.color == piece.color }
//
//		val targetValue: Float = if (piece != null) values.getValue(piece.javaClass.kotlin).toFloat() else 0.1f
//		val attackingPiecesValues = attackingPieces.map { it to values.getValue(it.javaClass.kotlin) }.sortedBy { (_, value) -> value }
//		val defendingPiecesValues = defendingPieces.map { it to values.getValue(it.javaClass.kotlin) }.sortedBy { (_, value) -> value }
//
//		val exchangeLength = min(attackingPieces.size, defendingPieces.size)
//
//		val tmp1 = attackingPiecesValues.subList(0, exchangeLength)
//		val tmp2 = defendingPiecesValues.subList(0, exchangeLength)
//
//		val offense = tmp1.map { (piece, _) -> values.getValue(piece.javaClass.kotlin) * (if (piece.isWhite()) 1 else -1) }
//		val defense = tmp2.map { (piece, _) -> values.getValue(piece.javaClass.kotlin) * (if (piece.isWhite()) 1 else -1) }
//
//		return 3f
//	}
//
//	private fun getPositionScore2(boardState: BoardState, threatMap: ThreatMap, position: Position): Float {
//		val pieceAtPosition = boardState[position]
//
//		debug(position, if (pieceAtPosition != null) pieceAtPosition else "(none)")
//
//		val attackeeValue: Float = if (pieceAtPosition != null) values.getValue(pieceAtPosition.javaClass.kotlin).toFloat() else 0.1f
//		val threateningPieces = threatMap.getValue(position)
//		val attackingPieces = threateningPieces.filter { pieceAtPosition != null && it.color !== pieceAtPosition.color }
//		val defendingPieces = threateningPieces.filter { pieceAtPosition === null || it.color === pieceAtPosition.color }
//		val minFoo = min(attackingPieces.size, defendingPieces.size)
//
//		debug("attackingPieces", attackingPieces)
//		debug("defendingPieces", defendingPieces)
//
//		val defendingValue = defendingPieces.map { (2f - values.getValue(it.javaClass.kotlin)) * (if (it.isWhite()) 1 else -1) }.sum()
//		debug("defendingValue", defendingValue)
//		val attackingValue = attackingPieces.map { (2f - values.getValue(it.javaClass.kotlin)) * (if (it.isWhite()) 1 else -1) }.sum() + attackeeValue
//		debug("attackingValue", attackingValue)
//
//		return when (pieceAtPosition) {
//			null -> defendingValue * 0.1f
//			else -> defendingValue + attackingValue
//		}.also { debug("final", it) }
//	}
//
//	private fun getPositionScore(boardState: BoardState, threatMap: ThreatMap, position: Position): Float {
//		val pieceAtPosition = boardState[position]
//debug(position, if (pieceAtPosition != null) pieceAtPosition else "(none)")
//
//		val attackeeValue: Float = if (pieceAtPosition != null) values.getValue(pieceAtPosition.javaClass.kotlin).toFloat() else 0.1f
//debug("attackeeValue", attackeeValue)
//
//		val attackingPieces = threatMap.getValue(position).filter { pieceAtPosition != null && it.color !== pieceAtPosition.color }
//debug("attackingPieces", attackingPieces)
//		val attackingPiecesValues = attackingPieces
//			.map { values.getValue(it.javaClass.kotlin) * (if (it.isWhite()) 1 else -1) }
//			.sortedBy { it }
//
//		val attackingPiecesAbsValues = attackingPiecesValues.map { abs(it) }
//
//		val defendingPieces = threatMap.getValue(position).filter { pieceAtPosition === null || it.color === pieceAtPosition.color }
//debug("defendingPieces", defendingPieces)
//		val defendingPiecesValues = defendingPieces
//			.map { values.getValue(it.javaClass.kotlin) * (if (it.isWhite()) 1 else -1) }
//			.sortedBy { it }
//
//		val defendingPiecesAbsValues = defendingPiecesValues.map { abs(it) }
//
//		val minFoo = min(attackingPieces.size, defendingPieces.size)
//
//		val attackingValue= when {
//			attackingPieces.isNotEmpty() -> ((listOf(attackeeValue) + if (minFoo > 0) defendingPiecesAbsValues.subList(0, minFoo - 1) else listOf()).sum()) / attackingPiecesAbsValues.sum()
//			else -> 0f
//		}
//debug("attackingValue", attackingValue)
//
//		val defendingValue = when {
//			attackingPieces.isNotEmpty() && defendingPieces.isNotEmpty() -> defendingPiecesValues.subList(0, minFoo).sum()
//			defendingPieces.isNotEmpty() -> defendingPieces.map { if (it.isWhite()) 0.1f else -0.1f }.sum()
//			else -> 0f
//		}
//debug("defendingValue", defendingValue)
//
//		val finalValue = attackingValue + defendingValue
//
//debug("FINAL", finalValue)
//
//		return finalValue
//	}
}