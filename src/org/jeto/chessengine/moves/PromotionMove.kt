package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class PromotionMove(pawn: Pawn, fromPosition: Position, toPosition: Position, val promotionType: KClass<out Piece>, modifier: Modifier = Modifier.NONE) : Move(
	pawn,
	fromPosition,
	toPosition,
	modifier
) {
	companion object {
		val ALLOWED_PROMOTION_TYPES = arrayOf(Queen::class, Rook::class, Knight::class, Bishop::class)
	}

	init {
		require(promotionType in ALLOWED_PROMOTION_TYPES) { "Invalid promotion type." }
	}

	override fun perform(boardState: BoardState): BoardState {
		return super.perform(boardState)
			.setPiece(toPosition, promotionType.primaryConstructor?.call(piece.color))
	}
}


