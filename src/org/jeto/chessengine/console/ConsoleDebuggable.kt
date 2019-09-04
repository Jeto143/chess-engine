package org.jeto.chessengine.console

interface ConsoleDebuggable {
	fun setDebugEnabled(enabled: Boolean): ConsoleDebuggable
	fun debug(vararg things: Any)
}