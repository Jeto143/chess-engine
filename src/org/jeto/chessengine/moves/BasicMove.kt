package org.jeto.chessengine.moves

import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Piece

/**
 * Represents a basic move, i.e. a move that depend on [piece]'s basic move/take directions.
 */
open class BasicMove(piece: Piece, fromPosition: Position, toPosition: Position): Move(piece, fromPosition, toPosition) {
	override fun requiresExplicitPosition(): Boolean = true

	override fun getCode(taking: Boolean, explicitPositionType: ExplicitPositionType?): String {
		val pieceCode = piece.toCode()
		var code = "" + (pieceCode ?: "")

		when (explicitPositionType) {
			ExplicitPositionType.COL -> code += fromPosition.col
			ExplicitPositionType.ROW -> code += fromPosition.row
		}

		if (taking) {
			if (pieceCode == null) {
				code += fromPosition.col
			}
			code += "x"
		}
		code += toPosition

		return code
	}
}