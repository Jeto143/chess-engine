package org.jeto.chessengine.parsing

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move

/**
 * Provides a way to parse a string move code into a [Move].
 */
interface MoveCodeParser {
	/**
	 * Parses [code] into a [Move].
	 * @throws org.jeto.chessengine.exceptions.InvalidMoveCodeException
	 */
	fun parseMoveCode(boardState: BoardState, code: String): Move
}