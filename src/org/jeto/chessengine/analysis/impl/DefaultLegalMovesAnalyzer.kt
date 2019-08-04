package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.Move

class DefaultLegalMovesAnalyzer(private val threatAnalyzer: ThreatAnalyzer, private val legalSpecialMovesAnalyzer: DefaultLegalSpecialMovesAnalyzer) : LegalMovesAnalyzer {
	private val legalMovesCache: MutableMap<BoardState, MutableMap<Piece, List<Move>>> = mutableMapOf()

	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> {
		if (boardState !in legalMovesCache) {
			legalMovesCache[boardState] = mutableMapOf()
		}
		if (piece !in legalMovesCache[boardState]!!) {
			legalMovesCache[boardState]!![piece] = computeLegalMoves(boardState, piece)
		}

		return legalMovesCache[boardState]!![piece]!!
	}

	override fun isMoveLegal(boardState: BoardState, move: Move): Boolean = move in getLegalMoves(boardState, move.piece)
	override fun isInCheckMate(boardState: BoardState, sideColor: Piece.Color): Boolean = boardState.getPieces(color = sideColor).all { getLegalMoves(boardState, it).isEmpty() }

	private fun computeLegalMoves(boardState: BoardState, piece: Piece): List<Move> =
		computeLegalNonTakingMoves(boardState, piece) +
				computeLegalTakingMoves(boardState, piece) +
				legalSpecialMovesAnalyzer.computeLegalSpecialMoves(boardState, piece)

	private fun computeLegalNonTakingMoves(boardState: BoardState, piece: Piece) = sequence {
		for (moveDirection in piece.getMoveDirections(boardState)) {
			for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeObstacles = false)) {
				val move = checkAndFillMove(boardState, piece, Move(piece, boardState.getPiecePosition(piece), targetPosition))
				if (move != null) {
					yield(move!!)
				}
			}
		}
	}.toList()

	private fun computeLegalTakingMoves(boardState: BoardState, piece: Piece): List<Move> = sequence {
		for (moveDirection in piece.getMoveDirections(boardState)) {
			for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeObstacles = true) { squareIsFreeOrHostile(boardState, it, piece) }) {
				val move = checkAndFillMove(boardState, piece, Move(piece, boardState.getPiecePosition(piece), targetPosition))
				if (move != null) {
					yield(move!! + Move.Modifier.TAKES)
				}
			}
		}
	}.toList()

	private fun checkAndFillMove(boardState: BoardState, piece: Piece, move: Move): Move? {
		val newBoardState = boardState + move
		val wasInCheck = threatAnalyzer.isInCheck(boardState, piece.color)
		val isInCheck = threatAnalyzer.isInCheck(newBoardState, piece.color)

		if (wasInCheck && isInCheck) {
			return null;
		}

		return when (isInCheck) {
			true -> move + if (isInCheckMate(newBoardState)) Move.Modifier.CHECKMATE else Move.Modifier.CHECK
			false -> move
		}
	}

	private fun squareIsFreeOrHostile(boardState: BoardState, position: Position, perspectivePiece: Piece) =
		(boardState[position]?.color ?: perspectivePiece.color) != perspectivePiece.color
}