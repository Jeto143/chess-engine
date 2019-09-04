package org.jeto.chessengine.moves

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class PromotionMove(pawn: Pawn, fromPosition: Position, toPosition: Position, private val promotionType: KClass<out Piece>) : BasicMove(pawn, fromPosition, toPosition) {
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

	override fun equals(other: Any?): Boolean = super.equals(other) && promotionType === (other as PromotionMove).promotionType
	override fun hashCode(): Int = 31 * super.hashCode() + promotionType.hashCode()
	override fun getCode(taking: Boolean, explicitPositionType: ExplicitPositionType?): String =
		super.getCode(taking, explicitPositionType) + "=${Piece.getCodeFromType(promotionType)}"
}


