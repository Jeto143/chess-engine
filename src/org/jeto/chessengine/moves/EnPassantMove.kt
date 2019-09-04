package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Pawn

class EnPassantMove(pawn: Pawn, fromPosition: Position, toPosition: Position) : BasicMove(pawn, fromPosition, toPosition) {
	override fun perform(boardState: BoardState): BoardState =
		super.perform(boardState)
			.setPiece(boardState.getPiecePosition(boardState.enPassantTakeablePawn!!), null)
			.setEnPassantTakeablePawn(null)
}