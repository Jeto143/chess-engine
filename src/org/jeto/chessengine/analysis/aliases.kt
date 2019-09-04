package org.jeto.chessengine.analysis

import org.jeto.chessengine.Position
import org.jeto.chessengine.pieces.Piece

typealias ThreatMap = Map<Position, List<Piece>>
typealias MutableThreatMap = MutableMap<Position, MutableList<Piece>>