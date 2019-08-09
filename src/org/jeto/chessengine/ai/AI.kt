package org.jeto.chessengine.ai

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move

interface AI {
	fun findBestMove(boardState: BoardState, depth: Int): Move
}