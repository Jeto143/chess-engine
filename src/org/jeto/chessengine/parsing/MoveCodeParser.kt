package org.jeto.chessengine.parsing

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move

interface MoveCodeParser {
	fun parseMoveCode(boardState: BoardState, code: String): Move
}