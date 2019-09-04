package org.jeto.chessengine.extension

import com.sun.javafx.util.Utils

fun lerp(from: Double, to: Double, weight: Double): Double = from + (to - from) * weight
fun inverseLerp(from: Double, to: Double, value: Double): Double = (value - from) / (to - from)
fun rangeLerp(value: Double, inputStart: Double, inputStop: Double, outputStart: Double, outputStop: Double, clamped: Boolean = false): Double {
	val result = lerp(outputStart, outputStop, inverseLerp(inputStart, inputStop, value))
	return if (clamped) Utils.clamp(outputStart, result, outputStop) else result
}