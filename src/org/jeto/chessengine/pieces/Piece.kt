package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import kotlin.reflect.KClass

abstract class Piece(val color: Color) {
	data class MoveDirection(val xDelta: Int, val yDelta: Int, val factor: Int)

	enum class Color {
		WHITE, BLACK;
		fun getOpposite(): Color = if (this == WHITE) BLACK else WHITE
	}

	companion object {
		private val codeToType = mapOf(
			'R' to Rook::class,
			'N' to Knight::class,
			'B' to Bishop::class,
			'K' to King::class,
			'Q' to Queen::class,
			null to Pawn::class
		)
		private val typeToCode = codeToType.map { (code, type) -> type to code }.toMap()

		fun getTypeFromCode(code: Char?): KClass<out Piece> = codeToType[code] ?: error("No such piece code: %s".format(code))
		fun getCodeFromType(type: KClass<out Piece>): Char = typeToCode[type] ?: error("No such piece type: %s".format(type))
	}

	abstract fun toCode(): Char?

	fun isWhite() = color == Color.WHITE
	fun isBlack() = color == Color.BLACK

	abstract fun getMoveDirections(boardState: BoardState): List<Piece.MoveDirection>
	open fun getTakeDirections(boardState: BoardState): List<Piece.MoveDirection> = getMoveDirections(boardState)

	fun computeTargetMovePositions(
		boardState: BoardState,
		moveDirection: Piece.MoveDirection,
		includeFinalObstacle: Boolean = false,
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
			val positionOccupied = boardState.isPositionOccupied(position)
			if (!positionOccupied || includeFinalObstacle) {
				if (filter(position)) {
					positions += position
				}
			}
			if (positionOccupied) {
				break
			}
			currentPosition = position
		}

		return positions
	}
}