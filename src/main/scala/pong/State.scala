package pong

import scala.scalajs.js.annotation.{JSExportAll, JSExportDescendentClasses, JSExport}

@JSExportDescendentClasses
trait PlayerInput
case class Up() extends PlayerInput
case class Down() extends PlayerInput

@JSExport
case class Size(width: Int, height: Int)
@JSExport
case class Position(x: Int, y: Int) {
  def update(velocity: Velocity) = {
    Position(x + velocity.x, y + velocity.y)
  }
}
@JSExport
case class Velocity(x: Int, y: Int)

@JSExportDescendentClasses
trait State
case class Score(playerOne: Int, playerTwo: Int) extends State
case class Ball(position: Position, velocity: Velocity) extends State
case class Paddles(playerOne: Position, playerTwo: Position) extends State

@JSExport
case class PongState(score: Score, ball: Ball, paddles: Paddles)
@JSExportAll
object PongState {
}

