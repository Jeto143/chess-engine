package org.jeto.chessengine.analysis.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.Position
import org.jeto.chessengine.analysis.PieceLegalMovesAnalyzer
import org.jeto.chessengine.analysis.ThreatAnalyzer
import org.jeto.chessengine.analysis.base.BaseLegalMovesAnalyzer
import org.jeto.chessengine.analysis.impl.specialmoves.CastlingAnalyzer
import org.jeto.chessengine.analysis.impl.specialmoves.DoubleForwardAnalyzer
import org.jeto.chessengine.analysis.impl.specialmoves.EnPassantAnalyzer
import org.jeto.chessengine.analysis.impl.specialmoves.PromotionMovesAnalyzer
import org.jeto.chessengine.moves.BasicMove
import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.pp
import java.lang.Thread.yield

class DefaultLegalMovesAnalyzer(
	private val threatAnalyzer: ThreatAnalyzer,
	private val pieceSpecialLegalMovesAnalyzers: List<PieceLegalMovesAnalyzer> = listOf(
		DoubleForwardAnalyzer(),
		CastlingAnalyzer(threatAnalyzer),
		EnPassantAnalyzer(),
		PromotionMovesAnalyzer()
	)
) : BaseLegalMovesAnalyzer() {
	private val legalMovesCache: MutableMap<BoardState, List<Move>> = mutableMapOf()

	override fun getLegalMoves(boardState: BoardState, sideColor: Piece.Color): List<Move> {
		if (boardState !in legalMovesCache) {
			legalMovesCache[boardState] = computeLegalMoves(boardState, sideColor)
		}
		return legalMovesCache[boardState]!!
	}

	private fun computeLegalMoves(boardState: BoardState, sideColor: Piece.Color): List<Move> =
		boardState.getPieces(color = sideColor)
			.flatMap { piece -> (
					computePotentialBasicNonTakingMoves(boardState, piece)
				+ computePotentialBasicTakingMoves(boardState, piece)
				+ pieceSpecialLegalMovesAnalyzers.flatMap { it.getLegalMoves(boardState, piece) }
				).filter { move -> !threatAnalyzer.isInCheck(boardState + move, move.piece.color) }
			}

	private fun computePotentialBasicNonTakingMoves(boardState: BoardState, piece: Piece): List<BasicMove> =
		mutableListOf<BasicMove>().apply {
			for (moveDirection in piece.getMoveDirections(boardState)) {
				for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = false)) {
					add(BasicMove(piece, boardState.getPiecePosition(piece), targetPosition))
				}
			}
		}

	private fun computePotentialBasicTakingMoves(boardState: BoardState, piece: Piece): List<BasicMove> =
		mutableListOf<BasicMove>().apply {
			for (moveDirection in piece.getTakeDirections(boardState)) {
				for (targetPosition in piece.computeTargetMovePositions(boardState, moveDirection, includeFinalObstacle = true) { squareIsFreeOrHostile(boardState, it, piece) }) {
					add(BasicMove(piece, boardState.getPiecePosition(piece), targetPosition))
				}
			}
		}

	private fun squareIsFreeOrHostile(boardState: BoardState, position: Position, perspectivePiece: Piece): Boolean =
		(boardState[position]?.color ?: perspectivePiece.color) != perspectivePiece.color

	fun clearCache() {
		legalMovesCache.clear()
	}
}