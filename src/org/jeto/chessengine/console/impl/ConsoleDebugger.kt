package org.jeto.chessengine.console.impl

import org.jeto.chessengine.console.ConsoleDebuggable

class ConsoleDebugger: ConsoleDebuggable {
	private var debugEnabled = false

	override fun setDebugEnabled(enabled: Boolean): ConsoleDebuggable {
		debugEnabled = enabled
		return this
	}

	override fun debug(vararg things: Any) {
		if (debugEnabled) {
			print(things.joinToString("\t") + System.lineSeparator())
		}
	}
}