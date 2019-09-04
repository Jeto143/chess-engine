package org.jeto.chessengine

import org.jeto.chessengine.BoardState.Companion.SIZE
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.*
import kotlin.reflect.KClass

/**
 * An unique, immutable representation of a chess board's state.
 *
 * The board is [SIZE] * [SIZE], contains [pieces], it's [turnColor]'s turn to play.
 * Castling availability is determined by [whiteCastlingAvailable] / [blackCastlingAvailable].
 * If a pawn can be "en passant'd" in this turn, it's referenced by [enPassantTakeablePawn].
 */
class BoardState(
	private val pieces: List<Piece?>,
	val turnColor: Piece.Color = Piece.Color.WHITE,
	val whiteCastlingAvailable: Boolean = true,
	val blackCastlingAvailable: Boolean = true,
	val enPassantTakeablePawn: Pawn? = null
) {
	private val piecesPositions: Map<Piece, Position>

	companion object {
		const val SIZE: Int = 8

		val DEFAULT: BoardState = BoardState(
			listOf(
				Rook(Piece.Color.WHITE), Knight(Piece.Color.WHITE), Bishop(Piece.Color.WHITE), Queen(Piece.Color.WHITE), King(Piece.Color.WHITE), Bishop(Piece.Color.WHITE), Knight(Piece.Color.WHITE), Rook(Piece.Color.WHITE),
				Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE),
				null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK),
				Rook(Piece.Color.BLACK), Knight(Piece.Color.BLACK), Bishop(Piece.Color.BLACK), Queen(Piece.Color.BLACK), King(Piece.Color.BLACK), Bishop(Piece.Color.BLACK), Knight(Piece.Color.BLACK), Rook(Piece.Color.BLACK)
			)
		)

		fun fromString(
			boardDescription: String, turnColor: Piece.Color = Piece.Color.WHITE,
			whiteCastlingAvailable: Boolean = true,
			blackCastlingAvailable: Boolean = true,
			enPassantTakeablePawn: Pawn? = null
		): BoardState {
			val pieces = boardDescription
				.split('|')
				.filter { !it.matches(Regex(".*[A-Z0-9].*", RegexOption.DOT_MATCHES_ALL)) }
				.map { it.replace(Regex("\\s+"), "") }
				.map { if (it.isEmpty()) null else Piece.fromSymbol(it[0]) }
				.chunked(SIZE)
				.reversed()
				.flatten()

			return BoardState(pieces, turnColor, whiteCastlingAvailable, blackCastlingAvailable, enPassantTakeablePawn)
		}
	}

	init {
		require(pieces.size == SIZE * SIZE) { "Board size must be ${SIZE * SIZE}." }
		piecesPositions = mutableMapOf()
		for (x in 1..SIZE) {
			for (y in 1..SIZE) {
				val position = Position(x, y)
				getPiece(position)?.let { piecesPositions[it] = position }
			}
		}
	}

	operator fun plus(move: Move): BoardState = move.perform(this)
	operator fun get(position: Position): Piece? = getPiece(position)
	operator fun get(x: Int, y: Int): Piece? = getPiece(Position(x, y))
	operator fun contains(piece: Piece): Boolean = piece in pieces

	fun getPieces(color: Piece.Color? = null, type: KClass<out Piece>? = null): List<Piece> {
		return pieces.filterNotNull().filter { piece ->
			when {
				color != null && piece.color != color -> false
				type != null && !type.isInstance(piece) -> false
				else -> true
			}
		}
	}
	fun getPiece(position: Position): Piece? = pieces[getIndexFromPosition(position)]
	fun getPiecePosition(piece: Piece): Position = piecesPositions.getValue(piece)
	fun setPiece(position: Position, piece: Piece?): BoardState {
		val newPieces = pieces.toMutableList()
		newPieces[getIndexFromPosition(position)] = piece
		return BoardState(newPieces, turnColor, whiteCastlingAvailable, blackCastlingAvailable)
	}
	fun movePiece(piece: Piece, fromPosition: Position, toPosition: Position, swapTurnColor: Boolean = true): BoardState {
		val newPieces = pieces.toMutableList()
		newPieces[getIndexFromPosition(fromPosition)] = null
		newPieces[getIndexFromPosition(toPosition)] = piece
		return BoardState(newPieces, if (swapTurnColor) turnColor.getOpposite() else turnColor, whiteCastlingAvailable, blackCastlingAvailable)
	}

	fun isPositionValid(x: Int, y: Int) = x in 1..SIZE && y in 1..SIZE
	fun isPositionOccupied(position: Position) = pieces[getIndexFromPosition(position)] != null

	fun isCastlingAvailable(color: Piece.Color) = if (color == Piece.Color.WHITE) whiteCastlingAvailable else blackCastlingAvailable
	fun disableWhiteCastling(): BoardState = BoardState(pieces, turnColor, false, blackCastlingAvailable)
	fun disableBlackCastling(): BoardState = BoardState(pieces, turnColor, whiteCastlingAvailable, false)
	fun disableCastling(color: Piece.Color): BoardState = if (color == Piece.Color.WHITE) disableWhiteCastling() else disableBlackCastling()
	fun setEnPassantTakeablePawn(pawn: Pawn?): BoardState = BoardState(pieces, turnColor, whiteCastlingAvailable, blackCastlingAvailable, pawn)

	override fun toString(): String {
		return StringBuilder().run {
			for (y in SIZE downTo 1) {
				val row: MutableList<Piece?> = mutableListOf()
				for (x in 1..SIZE) {
					row.add(getPiece(Position(x, y)))
				}
				append('0' + y, "\t|", row.joinToString("|") { piece -> "\t" + (piece?.toString() ?: "") + "\t" }, " |", System.lineSeparator())
			}
			append("\t\t", arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H').joinToString("\t\t") + "\t", System.lineSeparator())
			append("""
				White castling: ${if (whiteCastlingAvailable) "Available" else "Unavailable"}
				Black castling: ${if (blackCastlingAvailable) "Available" else "Unavailable"}
				En passeant takeable pawn: ${if (enPassantTakeablePawn != null) getPiecePosition(enPassantTakeablePawn).toString() else "None"}
			""".trimIndent())
		}.toString()
	}

	private fun getIndexFromPosition(position: Position): Int = (position.y - 1) * SIZE + position.x - 1
}