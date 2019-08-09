package org.jeto.chessengine

import org.jeto.chessengine.analysis.impl.*
import org.jeto.chessengine.evaluation.criteria.impl.PiecesValuesEvaluator
import org.jeto.chessengine.evaluation.impl.BasicBoardStateEvaluator
import org.jeto.chessengine.exceptions.InvalidMoveCodeException
import org.jeto.chessengine.parsing.impl.DefaultMoveCodeParser
import java.util.*

fun main() {
	val board = Board()
//	board.performMove(Move(board.state.getPiece(Position.fromCode("D2"))!!, Position.fromCode("D4")))

//	println(Arrays.deepToString(board.getAvailableMoves(board.state.getPiece(Position.fromCode("B1"))!!).toTypedArray()))
//	println(Arrays.deepToString(board.getAvailableMoves(board.state.getPiece(Position.fromCode("C1"))!!).toTypedArray()))

//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("Nc3"))
//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("d5"))
//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("Nxd5"))
//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("E6"))
//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("d4"))
//	board.performMove(MoveCodeParser(board.state, BoardStateAnalyzer(board.state)).parseMoveCode("Bd6"))
	val threatAnalyzer = DefaultThreatAnalyzer()
//		val checkStateAnalyzer = DefaultCheckStateAnalyzer(threatAnalyzer)
	val legalMovesAnalyzer = DefaultLegalMovesAnalyzer(threatAnalyzer, DefaultSpecialMovesAnalyzer(threatAnalyzer))
	val boardStateAnalyzer = DefaultBoardStateAnalyzer(legalMovesAnalyzer, threatAnalyzer)
	val moveCodeParser = DefaultMoveCodeParser(legalMovesAnalyzer)
	val piecesValuesEvaluator = PiecesValuesEvaluator()
	val boardStateEvaluator = BasicBoardStateEvaluator(mapOf(piecesValuesEvaluator to 1.0f), legalMovesAnalyzer)

	println(board.state)
	println(boardStateEvaluator.findBestMove(board.state, 1))

	val reader = Scanner(System.`in`)
	var moveCode: String = reader.next()
	while (moveCode.isNotEmpty()) {
		try {
			board.performMove(moveCodeParser.parseMoveCode(board.state, moveCode))
			println(board.state)
			println(boardStateAnalyzer.getCheckState(board.state))
			println(boardStateEvaluator.findBestMove(board.state, 2))
		} catch (e: InvalidMoveCodeException) {
			println(e)
		}

		moveCode = reader.next()
	}

	println(board.state)


//	println(board.squares[2][5])
}