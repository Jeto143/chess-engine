package org.jeto.chessengine.analysis

import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Piece

typealias ThreatMap = Map<Position, Map<Piece.Color, Int>>
typealias MutableThreatMap = MutableMap<Position, MutableMap<Piece.Color, Int>>