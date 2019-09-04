package org.jeto.chessengine

data class Position(val x: Int, val y: Int) {
	companion object {
		fun fromCode(code: String): Position {
			val match = Regex("^([a-h])([1-8])$").matchEntire(code) ?: throw IllegalArgumentException("Invalid position code: $code.")
			val (row, col) = match.destructured
			return Position(row[0] - 'a' + 1, col[0] - '1' + 1)
		}
	}

	val col: Char get() = 'a' - 1 + x
	val row: Int = y

	operator fun plus(increment: Pair<Int, Int>): Position = Position(x + increment.first, y + increment.second)
	override fun equals(other: Any?): Boolean = other is Position && other.x == x && other.y == y
	override fun hashCode(): Int = 31 * x + y
	override fun toString(): String = "" + col + ('0' + y)
}