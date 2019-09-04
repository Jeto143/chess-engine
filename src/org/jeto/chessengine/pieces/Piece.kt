package org.jeto.chessengine.pieces

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class Piece(val color: Color) {
	data class MoveDirection(val xDelta: Int, val yDelta: Int, val factor: Int)

	enum class Color {
		WHITE, BLACK;
		fun getOpposite(): Color = if (this == WHITE) BLACK else WHITE
	}

	companion object {
		private val symbolToTypeAndColor = mapOf(
			'♔' to Pair(King::class, Color.WHITE),
			'♚' to Pair(King::class, Color.BLACK),
			'♕' to Pair(Queen::class, Color.WHITE),
			'♛' to Pair(Queen::class, Color.BLACK),
			'♖' to Pair(Rook::class, Color.WHITE),
			'♜' to Pair(Rook::class, Color.BLACK),
			'♗' to Pair(Bishop::class, Color.WHITE),
			'♝' to Pair(Bishop::class, Color.BLACK),
			'♘' to Pair(Knight::class, Color.WHITE),
			'♞' to Pair(Knight::class, Color.BLACK),
			'♙' to Pair(Pawn::class, Color.WHITE),
			'♟' to Pair(Pawn::class, Color.BLACK)
		)
		private val typeAndColorToSymbol = symbolToTypeAndColor.map { (symbol, typeAndColor) -> typeAndColor to symbol }.toMap()

		private val codeToType = mapOf(
			'R' to Rook::class,
			'N' to Knight::class,
			'B' to Bishop::class,
			'K' to King::class,
			'Q' to Queen::class,
			null to Pawn::class
		)
		private val typeToCode = codeToType.map { (code, type) -> type to code }.toMap()

		fun fromSymbol(symbol: Char): Piece = symbolToTypeAndColor.getValue(symbol).let { (pieceClass, color) -> pieceClass.primaryConstructor!!.call(color) }
		fun getTypeFromCode(code: Char?): KClass<out Piece> = codeToType[code] ?: error("No such piece code: $code.")
		fun getCodeFromType(type: KClass<out Piece>): Char = typeToCode[type] ?: error("No such piece type: $type.")
	}

	abstract fun toCode(): Char?

	fun isWhite() = color == Color.WHITE
	fun isBlack() = color == Color.BLACK

	abstract fun getMoveDirections(boardState: BoardState): List<MoveDirection>
	open fun getTakeDirections(boardState: BoardState): List<MoveDirection> = getMoveDirections(boardState)

	fun computeTargetMovePositions(
		boardState: BoardState,
		moveDirection: MoveDirection,
		includeFinalObstacle: Boolean = false,
		filter: (position: Position) -> Boolean = { true }
	): List<Position> =
		mutableListOf<Position>().apply {
			var currentPosition: Position = boardState.getPiecePosition(this@Piece)

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
						add(position)
					}
				}
				if (positionOccupied) {
					break
				}
				currentPosition = position
			}
		}

	override fun toString(): String = typeAndColorToSymbol.getValue(Pair(javaClass.kotlin, color)).toString()
}