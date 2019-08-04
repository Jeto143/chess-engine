package org.jeto.chessengine.old

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.CheckState
import org.jeto.chessengine.Piece

interface CheckStateAnalyzer {
	fun getCheckState(boardState: BoardState, sideColor: Piece.Color = boardState.turnColor): CheckState
}