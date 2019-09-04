package org.jeto.chessengine

import org.jeto.chessengine.analysis.impl.DefaultBoardStateAnalyzer
import org.jeto.chessengine.analysis.impl.DefaultGameStateAnalyzer
import org.jeto.chessengine.analysis.impl.DefaultLegalMovesAnalyzer
import org.jeto.chessengine.analysis.impl.DefaultThreatAnalyzer
import org.jeto.chessengine.console.ConsolePlayer
import org.jeto.chessengine.evaluation.impl.BasicBoardStateDeepEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.PiecesPositionsEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.PiecesValuesEvaluator
import org.jeto.chessengine.evaluation.impl.criteria.ThreatEvaluator
import org.jeto.chessengine.parsing.impl.DefaultMoveCodeParser
import org.jeto.chessengine.pieces.Piece
import org.jeto.chessengine.util.MovePrinter
import java.util.*

fun pp(vararg stuff: Any) {
	print(stuff.joinToString("\t") + System.lineSeparator())
}

fun main() {
	val threatAnalyzer = DefaultThreatAnalyzer()
	val legalMovesAnalyzer = DefaultLegalMovesAnalyzer(threatAnalyzer)
	val gameStateAnalyzer = DefaultGameStateAnalyzer(threatAnalyzer, legalMovesAnalyzer)
	val boardStateAnalyzer = DefaultBoardStateAnalyzer(gameStateAnalyzer, threatAnalyzer, legalMovesAnalyzer)
	val moveCodeParser = DefaultMoveCodeParser(legalMovesAnalyzer)
	val movePrinter = MovePrinter(threatAnalyzer, legalMovesAnalyzer)

	val boardStateEvaluator = BasicBoardStateDeepEvaluator(
		legalMovesAnalyzer,
		mapOf(
			PiecesValuesEvaluator() to 4.0f,
			PiecesPositionsEvaluator() to 1.0f,
			ThreatEvaluator(threatAnalyzer) to 2.0f
		))

	var boardState = //BoardState.DEFAULT
		BoardState.fromString(
		"8\t|\t♜\t|\t♞\t|\t♝\t|\t♛\t|\t♜\t|\t\t|\t♚\t|\t\t |\n" +
				"7\t|\t♟\t|\t♟\t|\t♟\t|\t\t|\t♝\t|\t♟\t|\t♟\t|\t♟\t |\n" +
				"6\t|\t\t|\t\t|\t\t|\t\t|\t\t|\t\t|\t\t|\t\t |\n" +
				"5\t|\t\t|\t\t|\t\t|\t♟\t|\t\t|\t\t|\t♕\t|\t\t |\n" +
				"4\t|\t\t|\t\t|\t\t|\t♙\t|\t\t|\t\t|\t♞\t|\t\t |\n" +
				"3\t|\t\t|\t\t|\t♘\t|\t\t|\t\t|\t♘\t|\t\t|\t\t |\n" +
				"2\t|\t♙\t|\t♙\t|\t♙\t|\t\t|\t\t|\t♙\t|\t♙\t|\t♙\t |\n" +
				"1\t|\t♖\t|\t\t|\t♗\t|\t\t|\t♔\t|\t♗\t|\t\t|\t♖\t |\n" +
				"\t\tA\t\tB\t\tC\t\tD\t\tE\t\tF\t\tG\t\tH\t", Piece.Color.WHITE
	)

	val consolePlayer = ConsolePlayer(boardStateAnalyzer, boardStateEvaluator, moveCodeParser, movePrinter, 3, 10)
	consolePlayer.runCommand(boardState, "board")

	legalMovesAnalyzer.clearCache()
	threatAnalyzer.clearCache()

	val reader = Scanner(System.`in`)
	var command = reader.nextLine()
	while (command.isNotEmpty()) {
		legalMovesAnalyzer.clearCache()
		threatAnalyzer.clearCache()
		boardState = consolePlayer.runCommand(boardState, command)
		command = reader.nextLine()
	}
}