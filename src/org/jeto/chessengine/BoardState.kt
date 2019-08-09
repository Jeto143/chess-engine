package org.jeto.chessengine

import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Pawn
import org.jeto.chessengine.pieces.Piece
import kotlin.reflect.KClass

class BoardState(
	private val pieces: List<Piece?>,
	val turnColor: Piece.Color = Piece.Color.WHITE,
	val whiteCastlingAvailable: Boolean = true,
	val blackCastlingAvailable: Boolean = true,
	val enPassantTakeablePawn: Pawn? = null
) {
	companion object {
		const val SIZE: Int = 8
	}

	private val piecesPositions: MutableMap<Piece, Position> = mutableMapOf()

	init {
		require(pieces.size == SIZE * SIZE) { "Board size must be %s.".format(SIZE * SIZE) }
		for (x in 1..SIZE) {
			for (y in 1..SIZE) {
				val position = Position(x, y)
				val piece = getPiece(position)
				if (piece != null) {
					piecesPositions[piece] = position
				}
			}
		}
	}

	operator fun plus(move: Move): BoardState = move.perform(this)
	operator fun get(position: Position): Piece? = getPiece(position)
	operator fun get(x: Int, y: Int): Piece? = getPiece(Position(x, y))
	operator fun contains(piece: Piece): Boolean = piece in pieces

	fun getPieces(color: Piece.Color? = null, type: KClass<out Piece>? = null): List<Piece> {
		var filteredPieces: List<Piece> = pieces.filterNotNull()
		filteredPieces = filteredPieces.filter { piece ->
			when (true) {
				color != null && piece.color != color -> false
				type != null && !type.isInstance(piece) -> false
				else -> true
			}
		}

		return filteredPieces
	}
	fun getPiece(position: Position): Piece? = pieces[getIndexFromPosition(position)]
	fun getPiecePosition(piece: Piece): Position = piecesPositions[piece]!!
	fun setPiece(position: Position, piece: Piece?): BoardState {
		val newPieces: MutableList<Piece?> = pieces.toMutableList()
		newPieces[getIndexFromPosition(position)] = piece
		return BoardState(newPieces, turnColor, whiteCastlingAvailable, blackCastlingAvailable)
	}
	fun movePiece(piece: Piece, fromPosition: Position, toPosition: Position, swapTurnColor: Boolean = true): BoardState {
		val newPieces: MutableList<Piece?> = pieces.toMutableList()
		newPieces[getIndexFromPosition(fromPosition)] = null
		newPieces[getIndexFromPosition(toPosition)] = piece

		return BoardState(newPieces, if (swapTurnColor) turnColor.getOpposite() else turnColor, whiteCastlingAvailable, blackCastlingAvailable)
	}

	fun isPositionValid(x: Int, y: Int) = x in 1..SIZE && y in 1..SIZE
	fun isPositionOccupied(position: Position): Boolean = pieces[getIndexFromPosition(position)] != null

	fun disableWhiteCastling() = BoardState(pieces, turnColor, false, blackCastlingAvailable)
	fun disableBlackCastling() = BoardState(pieces, turnColor, whiteCastlingAvailable, false)
	fun disableCastling(color: Piece.Color) = if (color == Piece.Color.WHITE) disableWhiteCastling() else disableBlackCastling()
	fun setEnPassantTakeablePawn(pawn: Pawn?) = BoardState(pieces, turnColor, whiteCastlingAvailable, blackCastlingAvailable, pawn)

	override fun toString(): String {
		var output = ""
		for (y in SIZE downTo 1) {
			val row: MutableList<Piece?> = mutableListOf()
			for (x in 1..SIZE) {
				row.add(getPiece(Position(x, y)))
			}
			output += ('0' + y) + "\t|" + row.joinToString("|") { piece -> "\t" + (piece?.toString() ?: "") + "\t" } + " |" + System.lineSeparator()
		}
		output += "\t\t" + arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H').joinToString("\t\t") + "\t"

		output += System.lineSeparator()
		output += """
			White castling: ${if (whiteCastlingAvailable) "Available" else "Unavailable"}
			Black castling: ${if (blackCastlingAvailable) "Available" else "Unavailable"}
			En passeant takeable pawn: ${if (enPassantTakeablePawn !== null) getPiecePosition(enPassantTakeablePawn).toString() else "None"}
		""".trimIndent()

		return output
	}

	private fun getIndexFromPosition(position: Position): Int = (position.y - 1) * SIZE + position.x - 1
}