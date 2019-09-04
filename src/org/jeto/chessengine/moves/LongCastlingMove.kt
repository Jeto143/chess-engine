package org.jeto.chessengine.moves

import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Piece

class LongCastlingMove(king: King) : CastlingMove(
		king,
		if (king.color == Piece.Color.WHITE) Position.fromCode("e1") else Position.fromCode("e8"),
		if (king.color == Piece.Color.WHITE) Position.fromCode("c1") else Position.fromCode("c8")
) {
	override fun getRookStartPosition(): Position = if (piece.isWhite()) Position.fromCode("a1") else Position.fromCode("a8")
	override fun getRookEndPosition(): Position = if (piece.isWhite()) Position.fromCode("d1") else Position.fromCode("d8")
	override fun getCode(taking: Boolean, explicitPositionType: ExplicitPositionType?): String = "O-O-O"
}