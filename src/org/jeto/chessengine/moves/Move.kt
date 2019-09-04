package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Piece

/**
 * Represents a chess move where [piece] moves from [fromPosition] to [toPosition].
 */
abstract class Move(val piece: Piece, val fromPosition: Position, val toPosition: Position) {
	enum class ExplicitPositionType { COL, ROW }

	open fun perform(boardState: BoardState): BoardState = boardState.movePiece(piece, fromPosition, toPosition)
	open fun requiresExplicitPosition(): Boolean = false
	abstract fun getCode(taking: Boolean = false, explicitPositionType: ExplicitPositionType? = null): String

	override operator fun equals(other: Any?): Boolean =
		other is Move && piece == other.piece && fromPosition == other.fromPosition && toPosition == other.toPosition
	override fun hashCode(): Int {
		var result = piece.hashCode()
		result = 31 * result + fromPosition.hashCode()
		result = 31 * result + toPosition.hashCode()
		return result
	}
}