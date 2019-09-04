package org.jeto.chessengine.analysis.impl.specialmoves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.base.BasePieceLegalMovesAnalyzer
import org.jeto.chessengine.moves.EnPassantMove
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Pawn
import org.jeto.chessengine.pieces.Piece
import kotlin.math.abs

class EnPassantAnalyzer : BasePieceLegalMovesAnalyzer() {
	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> = when {
		piece is Pawn && isEnPassantLegal(boardState, piece) ->
			listOf(EnPassantMove(piece, boardState.getPiecePosition(piece), computeEnPassantTargetPosition(boardState)))
		else ->
			listOf()
	}

	private fun isEnPassantLegal(boardState: BoardState, pawn: Pawn): Boolean = when {
		boardState.enPassantTakeablePawn == null -> false
		boardState.enPassantTakeablePawn.color === pawn.color -> false
		boardState.getPiecePosition(boardState.enPassantTakeablePawn).y != boardState.getPiecePosition(pawn).y -> false
		abs(boardState.getPiecePosition(boardState.enPassantTakeablePawn).x - boardState.getPiecePosition(pawn).x) != 1 -> false
		else -> true
	}

	private fun computeEnPassantTargetPosition(boardState: BoardState): Position =
		boardState.getPiecePosition(boardState.enPassantTakeablePawn!!) + (0 to if (boardState.enPassantTakeablePawn.isWhite()) -1 else 1)
}