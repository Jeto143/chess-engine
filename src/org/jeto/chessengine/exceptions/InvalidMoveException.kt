package org.jeto.chessengine.exceptions

class InvalidMoveException(moveCode: String): Exception("Invalid move: %s.".format(moveCode)) {
}