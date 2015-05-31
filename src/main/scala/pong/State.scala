package pong

trait PlayerInput
case class Up() extends PlayerInput
case class Down() extends PlayerInput

case class Position(override val x: Int, override val y: Int) extends Vector2(x, y)
case class Velocity(override val x: Int, override val y: Int) extends Vector2(x, y)


trait State
case class Score(playerOne: Int, playerTwo: Int) extends State
case class Ball(position: Position, velocity: Velocity) extends State
case class Paddles(playerOne: Position, playerTwo: Position) extends State
case class PongState(score: Score, ball: Ball, paddles: Paddles)
