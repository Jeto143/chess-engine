package org.jeto.chessengine.moves

import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Piece

class ShortCastlingMove(king: King) : CastlingMove(
		king,
		if (king.color == Piece.Color.WHITE) Position.fromCode("e1") else Position.fromCode("e8"),
		if (king.color == Piece.Color.WHITE) Position.fromCode("g1") else Position.fromCode("g8")
) {
	override fun getRookStartPosition(): Position = if (piece.isWhite()) Position.fromCode("h1") else Position.fromCode("h8")
	override fun getRookEndPosition(): Position = if (piece.isWhite()) Position.fromCode("f1") else Position.fromCode("f8")
	override fun getCode(taking: Boolean, explicitPositionType: ExplicitPositionType?): String = "O-O"
}