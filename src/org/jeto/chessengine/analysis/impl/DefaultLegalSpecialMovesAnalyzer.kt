package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalSpecialMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.LongCastleMove
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.moves.ShortCastleMove
import org.jeto.chessengine.pieces.King

class DefaultLegalSpecialMovesAnalyzer(private val threatAnalyzer: ThreatAnalyzer) : LegalSpecialMovesAnalyzer {
	override fun computeLegalSpecialMoves(boardState: BoardState, piece: Piece): List<Move> {
		val specialMoves = mutableListOf<Move>()

		when (piece) {
			is King -> {
				if (isShortCastleLegal(boardState, piece)) {
					specialMoves += ShortCastleMove(piece)
				}
				if (isLongCastleLegal(boardState, piece)) {
					specialMoves += LongCastleMove(piece)
				}
			}
		}

		return specialMoves
	}

	private fun isShortCastleLegal(boardState: BoardState, piece: Piece): Boolean {
		return isCastleLegal(
			boardState, piece, if (piece.isWhite())
				arrayOf(Position.fromCode("f1"), Position.fromCode("g1"))
			else arrayOf(Position.fromCode("f8"), Position.fromCode("g8"))
		)
	}

	private fun isLongCastleLegal(boardState: BoardState, piece: Piece): Boolean {
		return isCastleLegal(
			boardState, piece, if (piece.isWhite())
				arrayOf(Position.fromCode("b1"), Position.fromCode("c1"), Position.fromCode("d1"))
			else arrayOf(Position.fromCode("b8"), Position.fromCode("c8"), Position.fromCode("d8"))
		)
	}

	private fun isCastleLegal(boardState: BoardState, piece: Piece, positionsInBetween: Array<Position>): Boolean {
		val castleAvailable = if (piece.isWhite()) boardState.whiteCastleAvailable else boardState.blackCastleAvailable
		return castleAvailable && positionsInBetween.all { position ->
			!boardState.isPositionOccupied(position) && !threatAnalyzer.isUnderAttack(boardState, position)
		}
	}
}