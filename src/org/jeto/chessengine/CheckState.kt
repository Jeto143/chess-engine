package org.jeto.chessengine

import org.jeto.chessengine.moves.Move

enum class CheckState {
	NONE,
	CHECK,
	CHECKMATE;

	fun toMoveModifier(): Move.Modifier? = when (this) {
		CHECK -> Move.Modifier.CHECK
		CHECKMATE -> Move.Modifier.CHECKMATE
		else -> null
	}
}