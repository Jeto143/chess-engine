package org.jeto.chessengine.parsing.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.exceptions.InvalidMoveCodeException
import org.jeto.chessengine.moves.LongCastlingMove
import org.jeto.chessengine.moves.PromotionMove
import org.jeto.chessengine.moves.ShortCastlingMove
import org.jeto.chessengine.parsing.MoveCodeParser
import org.jeto.chessengine.pieces.King
import org.jeto.chessengine.pieces.Pawn

class DefaultMoveCodeParser(private val legalMovesAnalyzer: LegalMovesAnalyzer) : MoveCodeParser {
	override fun parseMoveCode(boardState: BoardState, code: String): Move {
		val codeRegex: Regex = "^(?:(O-O(?:-O)?)|([RNBQK]?)([a-h1-8]?)(x?)([a-h])([1-8]))(?:=([RNBQ]))?([+#]?)$".toRegex()
		if (!code.matches(codeRegex)) {
			throw InvalidMoveCodeException(code)
		}

		val (castleCode, pieceCode, piecePosition, takesFlag, col, row, promotionType, modifier) = codeRegex.find(code)!!.destructured

		if (castleCode.isNotEmpty()) {
			val king: King = boardState.getPieces(
				color = boardState.turnColor,
				type = King::class
			).first() as King

			val castleMove = if (castleCode == "O-O-O") LongCastlingMove(king) else ShortCastlingMove(king)
			if (castleMove in legalMovesAnalyzer.getLegalMoves(boardState, king)) {
				return castleMove
			}
		}
		else if (promotionType.isNotEmpty()) {
			val targetPosition: Position = Position.fromCode(col + row)

			val pawns = boardState.getPieces(
				color = boardState.turnColor,
				type = Pawn::class
			)
			for (pawn in pawns) {
				val promotionMove = PromotionMove(pawn as Pawn, boardState.getPiecePosition(pawn), targetPosition, Piece.getTypeFromCode(promotionType[0]))
				if (promotionMove in legalMovesAnalyzer.getLegalMoves(boardState, pawn)) {
					return promotionMove
				}
			}
		}
		else {
			val targetPosition: Position = Position.fromCode(col + row)
			val takes: Boolean = takesFlag == "x"

			val pieces = boardState.getPieces(
				color = boardState.turnColor,
				type = Piece.getTypeFromCode(if (pieceCode.isNotEmpty()) pieceCode[0] else null)
			)

			val move = findCorrespondingMove(boardState, pieces, targetPosition, takes)

			if (move === null) {
				throw InvalidMoveCodeException(code)
			}

			// TODO: handle piecePosition

			return move
		}

		throw InvalidMoveCodeException(code)
	}

	private fun findCorrespondingMove(boardState: BoardState, pieces: List<Piece>, targetPosition: Position, takes: Boolean): Move? {
		for (piece in pieces) {
			for (move: Move in legalMovesAnalyzer.getLegalMoves(boardState, piece)) {
				if (move.toPosition == targetPosition && takes == move.modifier.contains(Move.Modifier.TAKES)) {
					return move
				}
			}
		}

		return null
	}
}