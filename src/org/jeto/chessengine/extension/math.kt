package org.jeto.chessengine.extension

fun lerp(from: Double, to: Double, weight: Double): Double = from + (to - from) * weight
fun inverseLerp(from: Double, to: Double, value: Double): Double = (value - from) / (to - from)
fun rangeLerp(value: Double, inputStart: Double, inputStop: Double, outputStart: Double, outputStop: Double) =
	lerp(outputStart, outputStop, inverseLerp(inputStart, inputStop, value))