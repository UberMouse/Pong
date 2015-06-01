package pong

trait PlayerInput
case class Up() extends PlayerInput
case class Down() extends PlayerInput

case class Size(width: Int, height: Int)
case class Position(x: Int, y: Int) {
  def update(velocity: Velocity) = {
    Position(x + velocity.x, y + velocity.y)
  }
}
case class Velocity(x: Int, y: Int)

trait State
case class Score(playerOne: Int, playerTwo: Int) extends State
case class Ball(position: Position, velocity: Velocity) extends State
case class Paddles(playerOne: Position, playerTwo: Position) extends State

case class PongState(score: Score, ball: Ball, paddles: Paddles)
object PongState {
  def build(ballSize: Size, paddleSize: Size, roomSize: Size) = {
    val MIDDLE = Position(roomSize.width / 2, roomSize.height / 2)
    val P1_PADDLE = Position(MIDDLE.y, ballSize.width * 4)
    val P2_PADDLE = Position(MIDDLE.y, roomSize.width - (ballSize.width * 4))
    val INITIAL_VELOCITY = Velocity(ballSize.width, ballSize.height)

    PongState(
      ball = Ball(
        position = MIDDLE,
        velocity = INITIAL_VELOCITY
      ),
      paddles = Paddles(
        playerOne = P1_PADDLE,
        playerTwo = P2_PADDLE
      ),
      score = Score(0, 0)
    )
  }
}

