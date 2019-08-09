package org.jeto.chessengine.evaluation

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.moves.Move

interface BoardStateEvaluator {
	fun evaluate(boardState: BoardState, depth: Int = 1): Float
	fun findBestMove(boardState: BoardState, depth: Int = 1): Pair<Move, Float>?
}