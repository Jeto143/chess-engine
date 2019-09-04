package org.jeto.chessengine.analysis.impl.specialmoves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.base.BasePieceLegalMovesAnalyzer
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.moves.PromotionMove
import org.jeto.chessengine.pieces.Pawn
import org.jeto.chessengine.pieces.Piece

class PromotionMovesAnalyzer : BasePieceLegalMovesAnalyzer() {
	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> =
		when (piece) {
			is Pawn -> computePotentialPromotionMoves(boardState, piece)
			else -> listOf()
	}

	private fun computePotentialPromotionMoves(boardState: BoardState, pawn: Pawn) =
		mutableListOf<PromotionMove>().apply {
			val requiredRow = if (pawn.isWhite()) 7 else 2
			val directionY = if (pawn.isWhite()) 1 else -1

			val pawnPosition = boardState.getPiecePosition(pawn)
			if (pawnPosition.row == requiredRow) {
				for (targetMovePosition in pawn.computeTargetMovePositions(boardState, Piece.MoveDirection(0, directionY, 1), includeFinalObstacle = false)) {
					for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
						add(PromotionMove(pawn, pawnPosition, targetMovePosition, promotionType))
					}
				}
				for (takeDirection in arrayOf(Piece.MoveDirection(-1, directionY, 1), Piece.MoveDirection(+1, directionY, 1))) {
					for (targetTakePosition in pawn.computeTargetMovePositions(boardState, takeDirection, includeFinalObstacle = true) { (boardState[it]?.color ?: pawn.color) != pawn.color }) {
						for (promotionType in PromotionMove.ALLOWED_PROMOTION_TYPES) {
							add(PromotionMove(pawn, pawnPosition, targetTakePosition, promotionType))
						}
					}
				}
			}
		}
}