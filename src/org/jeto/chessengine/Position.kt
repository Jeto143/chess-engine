package org.jeto.chessengine

data class Position(val x: Int, val y: Int) {
	companion object {
		fun fromCode(code: String): Position {
			val codeNormalized = code.toUpperCase()
			val codeRegex: Regex = "^([A-H])([1-8])$".toRegex()
			require(codeNormalized.matches(codeRegex)) { "Invalid position code: %s.".format(code) }

			val (row, col) = codeRegex.find(codeNormalized)!!.destructured

			return Position(row[0] - 'A' + 1, col[0] - '1' + 1)
		}
	}

	val col: Char
		get() = 'a' - 1 + x
	val row: Int = y

	override fun toString() = "" + col + ('0' + y)
}

//fun Position(code: String): Position {
//
//}