package org.jeto.chessengine.ai.impl

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.ai.AI
import org.jeto.chessengine.evaluation.impl.BasicBoardStateEvaluator
import org.jeto.chessengine.moves.Move

class BasicAI(private val boardStateEvaluator: BasicBoardStateEvaluator) : AI {
	override fun findBestMove(boardState: BoardState, depth: Int): Move {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}