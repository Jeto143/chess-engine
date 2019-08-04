package org.jeto.chessengine

import org.jeto.chessengine.pieces.*
import kotlin.reflect.KClass

abstract class Piece(val color: Color) {
	data class MoveDirection(val xDelta: Int, val yDelta: Int, val factor: Int)

	enum class Color {
		WHITE, BLACK;
		fun getOpposite(): Color = if (this == WHITE) BLACK else WHITE
	}

	companion object {
		fun getTypeFromCode(code: Char?): KClass<out Piece> {
			return when (code) {
				'R' -> Rook::class
				'N' -> Knight::class
				'B' -> Bishop::class
				'K' -> King::class
				'Q' -> Queen::class
				null -> Pawn::class
				else -> throw IllegalArgumentException("No such piece code: %s.".format(code))
			}
		}
	}

	abstract fun toCode(): Char?

	fun isWhite() = color == Color.WHITE
	fun isBlack() = color == Color.BLACK

	abstract fun getMoveDirections(boardState: BoardState): List<MoveDirection>
	open fun getTakeDirections(boardState: BoardState): List<MoveDirection> = getMoveDirections(boardState)

	fun computeTargetMovePositions(
		boardState: BoardState,
		moveDirection: MoveDirection,
		includeObstacles: Boolean = false,
		filter: (position: Position) -> Boolean = { true }
	): List<Position> {
		val positions: MutableList<Position> = mutableListOf()
		var currentPosition: Position = boardState.getPiecePosition(this)

		for (times in 1..(if (moveDirection.factor == 0) Int.MAX_VALUE else moveDirection.factor)) {
			val newX = currentPosition.x + moveDirection.xDelta
			val newY = currentPosition.y + moveDirection.yDelta
			if (!boardState.isPositionValid(newX, newY)) {
				break
			}
			val position = Position(newX, newY)
			if (!includeObstacles && boardState.isPositionOccupied(position)) {
				break
			}
			currentPosition = position
			if (filter(position)) {
				positions += currentPosition
			}
		}

		return positions
	}
}