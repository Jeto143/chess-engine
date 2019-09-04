package org.jeto.chessengine.parsing.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.exceptions.InvalidMoveCodeException
import org.jeto.chessengine.moves.*
import org.jeto.chessengine.parsing.MoveCodeParser
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Pawn
import org.jeto.chessengine.pieces.Piece

class DefaultMoveCodeParser(private val legalMovesAnalyzer: LegalMovesAnalyzer) : MoveCodeParser {
	override fun parseMoveCode(boardState: BoardState, code: String): Move {
		val codeRegex = Regex("^(?:(O-O(?:-O)?)|([RNBQK]?)([a-h1-8]?)(x?)([a-h])([1-8]))(?:=([RNBQ]))?([+#]?)$")
		val match = codeRegex.matchEntire(code) ?: throw InvalidMoveCodeException(code)
		val (castleCode, pieceCode, pieceColOrRow, takesFlag, col, row, promotionType, modifier) = match.destructured
		val targetPosition: Position = Position.fromCode(col + row)
		val takes: Boolean = takesFlag == "x"

		return when {
			castleCode.isNotEmpty() -> parseCastlingMove(boardState, castleCode)
			promotionType.isNotEmpty() -> parsePromotionMove(boardState, promotionType, targetPosition)
			else -> parseBasicMove(boardState, pieceCode, pieceColOrRow, targetPosition, takes)
		} ?: throw InvalidMoveCodeException(code)
	}

	private fun parseCastlingMove(boardState: BoardState, castleCode: String): CastlingMove? {
		val king: King = boardState.getPieces(
			color = boardState.turnColor,
			type = King::class
		).first() as King

		val castlingMove = if (castleCode == "O-O-O") LongCastlingMove(king) else ShortCastlingMove(king)
		return if (legalMovesAnalyzer.isMoveLegal(boardState, king, castlingMove)) castlingMove else null
	}

	private fun parsePromotionMove(boardState: BoardState, promotionType: String, targetPosition: Position): PromotionMove? {
		val pawns = boardState.getPieces(
			color = boardState.turnColor,
			type = Pawn::class
		)

		for (pawn in pawns) {
			val promotionMove = PromotionMove(pawn as Pawn, boardState.getPiecePosition(pawn), targetPosition, Piece.getTypeFromCode(promotionType[0]))
			if (legalMovesAnalyzer.isMoveLegal(boardState, pawn, promotionMove)) {
				return promotionMove
			}
		}

		return null
	}

	private fun parseBasicMove(boardState: BoardState, pieceCode: String, pieceColOrRow: String, targetPosition: Position, takes: Boolean): Move? {
		val pieces = boardState.getPieces(
			color = boardState.turnColor,
			type = Piece.getTypeFromCode(if (pieceCode.isNotEmpty()) pieceCode[0] else null)
		)

		val piecePositionFilter = { piece: Piece ->
			when {
				pieceColOrRow.matches(Regex("[a-h]")) -> boardState.getPiecePosition(piece).col == pieceColOrRow[0]
				pieceColOrRow.matches(Regex("[1-8]")) -> boardState.getPiecePosition(piece).row == pieceColOrRow[0].toInt()
				else -> true
			}
		}

		for (piece in pieces) {
			for (move: Move in legalMovesAnalyzer.getLegalMoves(boardState).filter { it.piece === piece && piecePositionFilter(piece) }) {
				if (move.toPosition == targetPosition && takes == boardState.isPositionOccupied(targetPosition)) {
					return move
				}
			}
		}

		return null
	}
}