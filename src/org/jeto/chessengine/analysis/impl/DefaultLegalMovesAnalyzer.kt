package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.LegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.moves.Move

class DefaultLegalMovesAnalyzer(private val threatAnalyzer: ThreatAnalyzer, private val legalSpecialMovesAnalyzer: DefaultSpecialMovesAnalyzer) : LegalMovesAnalyzer {
	private val legalMovesCache: MutableMap<BoardState, MutableMap<Piece, List<Move>>> = mutableMapOf()

	override fun getLegalMoves(boardState: BoardState): List<Move> {
		return boardState.getPieces(color = boardState.turnColor).map { piece -> getLegalMoves(boardState, piece) }.flatten()
	}

	override fun getLegalMoves(boardState: BoardState, piece: Piece): List<Move> {
		if (boardState !in legalMovesCache) {
			legalMovesCache[boardState] = mutableMapOf()
		}
		if (piece !in legalMovesCache[boardState]!!) {
			legalMovesCache[boardState]!![piece] = computeLegalMoves(boardState, piece)
		}

		return legalMovesCache[boardState]!![piece]!!
	}

	override fun isInCheckmate(boardState: BoardState, sideColor: Piece.Color): Boolean = boardState.getPieces(color = sideColor).all { getLegalMoves(boardState, it).isEmpty() }

	private fun computeLegalMoves(boardState: BoardState, piece: Piece): List<Move> {
		var legalMoves = (
				computePotentialBasicNonTakingMoves(boardState, piece)
				+ computePotentialBasicTakingMoves(boardState, piece)
				+ legalSpecialMovesAnalyzer.computePotentialSpecialMoves(boardState, piece))
			.filter { move -> !threatAnalyzer.isInCheck(boardState + move, move.piece.color) }

		legalMoves = legalMoves
			.map { move ->
				val otherMove = legalMoves.find { it !== move && it.piece::class == move.piece::class && it.toPosition == move.toPosition }
				return@map if (otherMove === null) move else when {
					otherMove.fromPosition.col != move.fromPosition.col -> move + Move.Modifier.EXPLICIT_COL
					else -> move + Move.Modifier.EXPLICIT_ROW
				}
			}
//			.map { move -> fillMoveCheckModifiers(boardState, move) }

		return legalMoves
	}

	private fun computePotentialBasicNonTakingMoves(boardState: BoardState, piece: Piece): List<Move> = sequence {
		for (moveDirection in piece.getMoveDirections(boardState)) {
			for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = false)) {
				yield(Move(piece, boardState.getPiecePosition(piece), targetPosition))
			}
		}
	}.toList()

	private fun computePotentialBasicTakingMoves(boardState: BoardState, piece: Piece): List<Move> = sequence {
		for (moveDirection in piece.getTakeDirections(boardState)) {
			for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = true) { squareIsFreeOrHostile(boardState, it, piece) }) {
				yield(Move(piece, boardState.getPiecePosition(piece), targetPosition, Move.Modifier.TAKES))
			}
		}
	}.toList()

	private fun fillMoveCheckModifiers(boardState: BoardState, move: Move): Move {
		val newBoardState = boardState + move

		return when {
			isInCheckmate(newBoardState) -> move + Move.Modifier.CHECKMATE
			threatAnalyzer.isInCheck(newBoardState) -> move + Move.Modifier.CHECK
			else -> move
		}
	}

	private fun squareIsFreeOrHostile(boardState: BoardState, position: Position, perspectivePiece: Piece) =
		(boardState[position]?.color ?: perspectivePiece.color) != perspectivePiece.color
}