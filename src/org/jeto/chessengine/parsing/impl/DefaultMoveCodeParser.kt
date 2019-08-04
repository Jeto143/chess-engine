package org.jeto.chessengine.parsing.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.exceptions.InvalidMoveException
import org.jeto.chessengine.moves.LongCastleMove
import org.jeto.chessengine.moves.ShortCastleMove
import org.jeto.chessengine.parsing.MoveCodeParser
import org.jeto.chessengine.pieces.King
import java.lang.Exception

class DefaultMoveCodeParser(private val legalMovesAnalyzer: LegalMovesAnalyzer) : MoveCodeParser {
	override fun parseMoveCode(boardState: BoardState, code: String): Move {
		val codeRegex: Regex = "^(?:(O-O(?:-O)?)|([RNBQK]?)([a-h1-8]?)(x?)([a-h])([1-8]))([+#]?)$".toRegex(RegexOption.IGNORE_CASE)
		require(code.matches(codeRegex))  // FIXME: specific move parse exception?

		val (castleCode, pieceCode, piecePosition, takesFlag, col, row, modifier) = codeRegex.find(code)!!.destructured

		if (castleCode.isNotEmpty()) {
			val king: King = boardState.getPieces(
				color = boardState.turnColor,
				type = King::class
			).first() as King

			val castleMove = if (castleCode == "O-O-O") LongCastleMove(king) else ShortCastleMove(king)
			if (legalMovesAnalyzer.isMoveLegal(boardState, castleMove)) {
				return castleMove
			}
		}
		else {
			val targetPosition: Position = Position.fromCode(col + row)
			val takes: Boolean = takesFlag == "x"

			var pieces = boardState.getPieces(
				color = boardState.turnColor,
				type = Piece.getTypeFromCode(if (pieceCode.isNotEmpty()) pieceCode[0] else null)
			)

			// TODO: hard to reread, put into a function and name it well
			// FIXME: not sure that "is" check is good at all
			pieces = pieces.filter { piece ->
				for (move: Move in legalMovesAnalyzer.getLegalMoves(boardState, piece)) {
					if (move.toPosition == targetPosition && takes == boardState.isPositionOccupied(targetPosition)) {
						return@filter true
					}
				}
				return@filter false
			}

			if (pieces.isEmpty()) {
				throw InvalidMoveException(code)
			}

			// TODO: handle piecePosition

			val piece = pieces.first()
			return Move(piece, boardState.getPiecePosition(piece), targetPosition)
		}

		throw Exception("fail")
	}
}