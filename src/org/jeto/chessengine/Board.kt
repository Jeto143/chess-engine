package org.jeto.chessengine

import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.*

class Board(var state: BoardState = DEFAULT_STATE) {
	companion object {
		val DEFAULT_STATE: BoardState = BoardState(
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
	}

	fun performMove(move: Move) {
		state += move
	}
}

