package org.jeto.chessengine.exceptions

class InvalidMoveCodeException(moveCode: String): Exception("Invalid move: %s.".format(moveCode))