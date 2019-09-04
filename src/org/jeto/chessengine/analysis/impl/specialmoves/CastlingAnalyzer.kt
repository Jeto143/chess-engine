package org.jeto.chessengine.analysis.impl.specialmoves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.analysis.base.BasePieceLegalMovesAnalyzer
import org.jeto.chessengine.moves.LongCastlingMove
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.moves.ShortCastlingMove
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.pieces.Rook

class CastlingAnalyzer(private val threatAnalyzer: ThreatAnalyzer) : BasePieceLegalMovesAnalyzer() {
	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> =
		mutableListOf<Move>().apply {
			if (piece is King) {
				if (isShortCastlingLegal(boardState, piece)) {
					add(ShortCastlingMove(piece))
				}
				if (isLongCastlingLegal(boardState, piece)) {
					add(LongCastlingMove(piece))
				}
			}
		}

	private fun isShortCastlingLegal(boardState: BoardState, king: King): Boolean =
		when (king.color) {
			Piece.Color.WHITE -> isCastlingLegal(
				boardState,
				king,
				Position.fromCode("e1"),
				Position.fromCode("h1"),
				arrayOf(Position.fromCode("f1"), Position.fromCode("g1"))
			)
			Piece.Color.BLACK -> isCastlingLegal(
				boardState,
				king,
				Position.fromCode("e8"),
				Position.fromCode("h8"),
				arrayOf(Position.fromCode("f8"), Position.fromCode("g8"))
			)
		}

	private fun isLongCastlingLegal(boardState: BoardState, king: King): Boolean =
		when (king.color) {
			Piece.Color.WHITE -> isCastlingLegal(
				boardState,
				king,
				Position.fromCode("e1"),
				Position.fromCode("a1"),
				arrayOf(Position.fromCode("b1"), Position.fromCode("c1"), Position.fromCode("d1"))
			)
			Piece.Color.BLACK -> isCastlingLegal(
				boardState,
				king,
				Position.fromCode("e8"),
				Position.fromCode("a8"),
				arrayOf(Position.fromCode("b8"), Position.fromCode("c8"), Position.fromCode("d8"))
			)
	}

	private fun isCastlingLegal(boardState: BoardState, king: King, kingRequiredPosition: Position, rookRequiredPosition: Position, positionsInBetween: Array<Position>): Boolean =
		boardState.isCastlingAvailable(king.color)
				&& boardState[kingRequiredPosition] == king
				&& boardState[rookRequiredPosition] is Rook
				&& boardState[rookRequiredPosition]!!.color === king.color
				&& !threatAnalyzer.isInCheck(boardState)
				&& positionsInBetween.all { !boardState.isPositionOccupied(it) && !threatAnalyzer.isUnderAttack(boardState, it, king.color.getOpposite()) }
}