package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.SpecialMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.*
import org.jeto.chessengine.pieces.*

class DefaultSpecialMovesAnalyzer(private val threatAnalyzer: ThreatAnalyzer) : SpecialMovesAnalyzer {
	override fun computePotentialSpecialMoves(boardState: BoardState, piece: Piece): List<Move> {
		val specialMoves = mutableListOf<Move>()

		when (piece) {
			is King -> {
				if (isShortCastlingLegal(boardState, piece)) {
					specialMoves += ShortCastlingMove(piece)
				}
				if (isLongCastlingLegal(boardState, piece)) {
					specialMoves += LongCastlingMove(piece)
				}
			}
			is Pawn -> {
				if (isEnPassantLegal(boardState, piece)) {
//					specialMoves += EnPassantMove(piece,
				}
				specialMoves += computePotentialPromotionMoves(boardState, piece)
			}
		}

		return specialMoves
	}

	private fun isShortCastlingLegal(boardState: BoardState, king: King): Boolean {
		return isCastlingLegal(
			boardState, king, if (king.isWhite())
				arrayOf(Position.fromCode("f1"), Position.fromCode("g1"))
			else arrayOf(Position.fromCode("f8"), Position.fromCode("g8"))
		)
	}

	private fun isLongCastlingLegal(boardState: BoardState, king: King): Boolean {
		return isCastlingLegal(
			boardState, king, if (king.isWhite())
				arrayOf(Position.fromCode("b1"), Position.fromCode("c1"), Position.fromCode("d1"))
			else arrayOf(Position.fromCode("b8"), Position.fromCode("c8"), Position.fromCode("d8"))
		)
	}

	private fun isCastlingLegal(boardState: BoardState, king: King, positionsInBetween: Array<Position>): Boolean {
		val castlingAvailable = if (king.isWhite()) boardState.whiteCastlingAvailable else boardState.blackCastlingAvailable
		return castlingAvailable && positionsInBetween.all { position ->
			!boardState.isPositionOccupied(position) && !threatAnalyzer.isUnderAttack(boardState, position, king.color.getOpposite())
		}
	}

//	private fun isEnPassantLegal(boardState: BoardState, pawn: Pawn): Boolean {
//
//	}

	private fun computePotentialPromotionMoves(boardState: BoardState, pawn: Pawn) = sequence {
		val requiredRow = if (pawn.isWhite()) 7 else 2

		val pawnPosition = boardState.getPiecePosition(pawn)
		if (pawnPosition.row == requiredRow) {
			for (moveDirection in pawn.getMoveDirections(boardState)) {
				for (targetMovePosition in pawn.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = false)) {
					for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
						yield(PromotionMove(pawn, pawnPosition, targetMovePosition, promotionType))
					}
				}
			}
			for (moveDirection in pawn.getTakeDirections(boardState)) {
				for (targetMovePosition in pawn.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = true
				) { (boardState[it]?.color ?: pawn.color) != pawn.color }) {
					for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
						yield(PromotionMove(pawn, pawnPosition, targetMovePosition, promotionType, Move.Modifier.TAKES))
					}
				}
			}
		}
	}.toList()
}