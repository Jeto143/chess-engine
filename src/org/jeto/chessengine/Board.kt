package org.jeto.chessengine

import org.jeto.chessengine.moves.Move
import org.jeto.chessengine.pieces.*

class Board(var state: BoardState = DEFAULT_STATE) {
	companion object {
		val DEFAULT_STATE: BoardState = BoardState(
			listOf(
				Rook(Piece.Color.WHITE), null, null, null, King(Piece.Color.WHITE), null, null, Rook(Piece.Color.WHITE),
				Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE), Pawn(Piece.Color.WHITE),
				null, null, null, null, null, Rook(Piece.Color.WHITE), null, null,
				null, null, null, null, null, Queen(Piece.Color.WHITE), null, null,
				null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK), Pawn(Piece.Color.BLACK),
				Rook(Piece.Color.BLACK), Knight(Piece.Color.BLACK), Bishop(Piece.Color.BLACK), Queen(Piece.Color.BLACK), King(Piece.Color.BLACK), Bishop(Piece.Color.BLACK), Knight(Piece.Color.BLACK), Rook(Piece.Color.BLACK)
			),
			Piece.Color.WHITE,
			whiteCastleAvailable = true,
			blackCastleAvailable = true
		)
	}

	fun performMove(move: Move) {
		state += move
	}
}

