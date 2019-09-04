package org.jeto.chessengine.analysis.impl.specialmoves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.base.BasePieceLegalMovesAnalyzer
import org.jeto.chessengine.moves.DoubleForwardMove
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Pawn
import org.jeto.chessengine.pieces.Piece

class DoubleForwardAnalyzer : BasePieceLegalMovesAnalyzer() {
	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> = when {
		piece is Pawn && isDoubleForwardLegal(boardState, piece) ->
			listOf(DoubleForwardMove(piece, boardState.getPiecePosition(piece), computeDoubleForwardTargetPosition(boardState, piece)))
		else ->
			listOf()
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

	private fun computeDoubleForwardTargetPosition(boardState: BoardState, pawn: Pawn): Position =
		boardState.getPiecePosition(pawn) + (0 to if (pawn.isWhite()) 2 else -2)
}