package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Pawn

class DoubleForwardMove(pawn: Pawn, fromPosition: Position, toPosition: Position) : BasicMove(pawn, fromPosition, toPosition) {
	private val pawn = piece as Pawn

	override fun perform(boardState: BoardState): BoardState = super.perform(boardState).setEnPassantTakeablePawn(pawn)
}