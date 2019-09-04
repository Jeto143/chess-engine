package org.jeto.chessengine.console

import org.jeto.chessengine.BoardState
import org.jeto.chessengine.analysis.BoardStateAnalyzer
import org.jeto.chessengine.evaluation.BoardStateDeepEvaluator
import org.jeto.chessengine.evaluation.impl.BasicBoardStateDeepEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.PiecesPositionsEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.PiecesValuesEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.ThreatEvaluator
import org.jeto.chessengine.exceptions.InvalidMoveCodeException
import org.jeto.chessengine.parsing.MoveCodeParser
import org.jeto.chessengine.pp
import org.jeto.chessengine.util.MovePrinter

class ConsolePlayer(
	private val boardStateAnalyzer: BoardStateAnalyzer,
	private val boardStateEvaluator: BoardStateDeepEvaluator,
	private val moveCodeParser: MoveCodeParser,
	private val movePrinter: MovePrinter,
	private val evaluationDepth: Int,
	private val evaluationGreediness: Int
) {
	companion object {
		private const val EVAL_COMMAND_PATTERN = "eval(d?)(?: ([a-zA-Z]+))?(?: (\\d+))?(?: (\\d+))?"
	}

	fun runCommand(boardState: BoardState, command: String): BoardState {
		var currentBoardState = boardState

		when {
			command in arrayOf("board", "b") -> {
				pp(boardState)
				pp(boardStateAnalyzer.getGameState(boardState))
			}
			command in arrayOf("state") -> {
				pp(boardStateAnalyzer.getGameState(boardState))
			}
			command in arrayOf("best") -> {
				val bestMove = boardStateEvaluator.findBestMove(boardState, evaluationDepth, evaluationGreediness)
				pp(movePrinter.getMoveCode(currentBoardState, bestMove!!.first), bestMove.second)
			}
			command.matches(Regex(EVAL_COMMAND_PATTERN)) -> {
				pp(runEvalCommand(boardState, command))
			}
			else -> {
				try {
					currentBoardState += moveCodeParser.parseMoveCode(boardState, command)
					runCommand(currentBoardState,"board")
					runCommand(currentBoardState,"best")
				} catch (e: InvalidMoveCodeException) {
					pp(e)
				}
			}
		}

		return currentBoardState
	}
	
	private fun runEvalCommand(boardState: BoardState, command: String): Float {
		val (debug: String, evalType, depth, greediness) = Regex(EVAL_COMMAND_PATTERN).matchEntire(command)!!.destructured

		val withDebug = debug.isNotEmpty()

		val evaluator = setDebugEnabled(when (evalType) {
			"threat" -> BasicBoardStateDeepEvaluator(boardStateAnalyzer, mapOf(setDebugEnabled(ThreatEvaluator(boardStateAnalyzer), withDebug) to 1.0f))
			"position" -> BasicBoardStateDeepEvaluator(boardStateAnalyzer, mapOf(setDebugEnabled(PiecesPositionsEvaluator(), withDebug) to 1.0f))
			"value" -> BasicBoardStateDeepEvaluator(boardStateAnalyzer, mapOf(setDebugEnabled(PiecesValuesEvaluator(), withDebug) to 1.0f))
			else -> boardStateEvaluator
		}, withDebug)

		return evaluator.evaluate(boardState, depth.toIntOrNull() ?: 1, greediness.toIntOrNull())
	}

	private fun <T> setDebugEnabled(obj: T, withDebug: Boolean): T {
		if (obj is ConsoleDebuggable) {
			obj.setDebugEnabled(withDebug)
		}
		return obj
	}
}