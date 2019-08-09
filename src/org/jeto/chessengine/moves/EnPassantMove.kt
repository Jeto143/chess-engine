package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Pawn

class EnPassantMove(pawn: Pawn, fromPosition: Position, toPosition: Position, modifier: Modifier = Modifier.NONE) : Move(
	pawn,
	fromPosition,
	toPosition,
	modifier
) {
	override fun perform(boardState: BoardState): BoardState {
		val enPassantTakeablePawn = boardState.enPassantTakeablePawn!!

		return super.perform(boardState)
			.setPiece(boardState.getPiecePosition(enPassantTakeablePawn), null)
			.setEnPassantTakeablePawn(null)
	}
}