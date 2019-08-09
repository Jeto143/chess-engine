package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Pawn

class DoubleForwardMove(pawn: Pawn, fromPosition: Position, toPosition: Position, modifier: Modifier = Modifier.NONE) : Move(
	pawn,
	fromPosition,
	toPosition,
	modifier
) {
	private val pawn = piece as Pawn

	override fun perform(boardState: BoardState): BoardState {
		return super.perform(boardState)
			.setEnPassantTakeablePawn(pawn)
	}
}