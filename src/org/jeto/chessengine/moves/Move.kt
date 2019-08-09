package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position

open class Move(val piece: Piece, val fromPosition: Position, val toPosition: Position, val modifier: Modifier = Modifier.NONE) {
	enum class Modifier(val flag: Int) {
		NONE(0),
		TAKES(1),
		CHECK(2),
		CHECKMATE(3);

		companion object {
			private val valuesByFlag: Map<Int, Modifier> = values().associateBy(Modifier::flag)
			private fun fromFlag(flag: Int): Modifier = valuesByFlag[flag] ?: error("Invalid move modifier flag: %d.".format(flag))
		}

		operator fun plus(modifier: Modifier?) =
			fromFlag(flag or (modifier?.flag ?: 0))
		operator fun contains(modifier: Modifier): Boolean = modifier.flag and this.flag == modifier.flag
	}

	open fun perform(boardState: BoardState): BoardState = boardState.movePiece(piece, fromPosition, toPosition)

	operator fun plus(addedModifier: Modifier?): Move = Move(piece, fromPosition, toPosition, modifier + addedModifier)
	override operator fun equals(other: Any?): Boolean =
		other is Move && piece == other.piece && fromPosition == other.fromPosition && toPosition == other.toPosition
	override fun hashCode(): Int {
		var result = piece.hashCode()
		result = 31 * result + fromPosition.hashCode()
		result = 31 * result + toPosition.hashCode()
		result = 31 * result + modifier.hashCode()

		return result
	}

	override fun toString(): String {
		val pieceCode = piece.toCode()
		var code = "" + (pieceCode ?: "")

		if (Modifier.TAKES in modifier) {
			if (pieceCode == null) {
				code += fromPosition.col
			}
			code += "x"
		}
		code += toPosition

		when {
			modifier.contains(Modifier.CHECK) -> code += "+"
			modifier.contains(Modifier.CHECKMATE) -> code += "#"
		}

		return code
	}
}