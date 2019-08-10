package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.SpecialMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.*
import org.jeto.chessengine.pieces.*
import kotlin.math.abs

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
				if (isDoubleForwardLegal(boardState, piece)) {
					specialMoves += DoubleForwardMove(piece, boardState.getPiecePosition(piece), computeDoubleForwardTargetPosition(boardState, piece))
				}
				if (isEnPassantLegal(boardState, piece)) {
					specialMoves += EnPassantMove(piece, boardState.getPiecePosition(piece), computeEnPassantTargetPosition(boardState), Move.Modifier.TAKES)
				}
				specialMoves += computePotentialPromotionMoves(boardState, piece)
			}
		}

		return specialMoves
	}

	private fun isShortCastlingLegal(boardState: BoardState, king: King): Boolean {
		return when (king.color) {
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
	}

	private fun isLongCastlingLegal(boardState: BoardState, king: King): Boolean {
		return when (king.color) {
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
	}

	private fun isCastlingLegal(boardState: BoardState, king: King, kingRequiredPosition: Position, rookRequiredPosition: Position, positionsInBetween: Array<Position>): Boolean {
		return (if (king.isWhite()) boardState.whiteCastlingAvailable else boardState.blackCastlingAvailable)
				&& boardState[kingRequiredPosition] == king
				&& boardState[rookRequiredPosition] is Rook
				&& boardState[rookRequiredPosition]!!.color === king.color
				&& positionsInBetween.all { position ->
					!boardState.isPositionOccupied(position) && !threatAnalyzer.isUnderAttack(
						boardState,
						position,
						king.color.getOpposite()
					)
				}
	}

	private fun isEnPassantLegal(boardState: BoardState, pawn: Pawn): Boolean {
		return when {
			boardState.enPassantTakeablePawn === null -> false
			boardState.enPassantTakeablePawn.color === pawn.color -> false
			boardState.getPiecePosition(boardState.enPassantTakeablePawn).y != boardState.getPiecePosition(pawn).y -> false
			abs(boardState.getPiecePosition(boardState.enPassantTakeablePawn).x - boardState.getPiecePosition(pawn).x) != 1 -> false
			else -> true
		}
	}

	private fun computeEnPassantTargetPosition(boardState: BoardState): Position {
		val targetPawnPosition = boardState.getPiecePosition(boardState.enPassantTakeablePawn!!)

		return when (boardState.enPassantTakeablePawn.color) {
			Piece.Color.WHITE -> Position(targetPawnPosition.x, targetPawnPosition.y - 1)
			Piece.Color.BLACK -> Position(targetPawnPosition.x, targetPawnPosition.y + 1)
		}
	}

	private fun isDoubleForwardLegal(boardState: BoardState, pawn: Pawn): Boolean {
		val pawnPosition = boardState.getPiecePosition(pawn)

		return when (pawn.color) {
			Piece.Color.WHITE -> pawnPosition.row == 2
					&& !boardState.isPositionOccupied(Position(pawnPosition.x, pawnPosition.y + 1))
					&& !boardState.isPositionOccupied(Position(pawnPosition.x, pawnPosition.y + 2))
			Piece.Color.BLACK -> pawnPosition.row == 7
					&& !boardState.isPositionOccupied(Position(pawnPosition.x, pawnPosition.y - 1))
					&& !boardState.isPositionOccupied(Position(pawnPosition.x, pawnPosition.y - 2))
		}
	}

	private fun computeDoubleForwardTargetPosition(boardState: BoardState, pawn: Pawn): Position {
		val pawnPosition = boardState.getPiecePosition(pawn)

		return when (pawn.color) {
			Piece.Color.WHITE -> Position(pawnPosition.x, pawnPosition.y + 2)
			Piece.Color.BLACK -> Position(pawnPosition.x, pawnPosition.y - 2)
		}
	}

	private fun computePotentialPromotionMoves(boardState: BoardState, pawn: Pawn) = sequence {
		val requiredRow = if (pawn.isWhite()) 7 else 2

		val pawnPosition = boardState.getPiecePosition(pawn)
		val directionY = if (pawn.isWhite()) 1 else -1

		if (pawnPosition.row == requiredRow) {
			for (targetMovePosition in pawn.computeTargetMovePositions(boardState, Piece.MoveDirection(0, directionY, 1), includeFinalObstacle = false)) {
				for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
					yield(PromotionMove(pawn, pawnPosition, targetMovePosition, promotionType))
				}
			}
			for (moveDirection in arrayOf(Piece.MoveDirection(-1, directionY, 1), Piece.MoveDirection(+1, directionY, 1))) {
				for (targetMovePosition in pawn.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = true) { (boardState[it]?.color ?: pawn.color) != pawn.color }) {
					for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
						yield(PromotionMove(pawn, pawnPosition, targetMovePosition, promotionType, Move.Modifier.TAKES))
					}
				}
			}
		}
	}.toList()
}