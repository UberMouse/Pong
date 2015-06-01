package pong

import com.softwaremill.quicklens._
/**
 * Initializes a new pong instance
 * @param ballSize height, width of ball. Must be even
 * @param paddleSize height, width of paddle. Height/width must be
 *                   evenly divisible by ball height/width
 * @param roomSize height, width of room. Height/width must be
 *                   evenly divisible by ball height/width
 */
class Pong(ballSize: Size, paddleSize: Size, roomSize: Size) {
  assert(ballSize.width % 2 == 0, "Ball width must be even")
  assert(ballSize.height % 2 == 0, "Ball height must be even")
  assert(paddleSize.width >= ballSize.width, "Paddle width must be greater than or equal to ball width")
  assert(paddleSize.height >= ballSize.height, "Paddle height must be greater than or equal to ball height")
  assert(paddleSize.width % ballSize.width == 0, "Paddle width must be evenly divisible by ball width")
  assert(paddleSize.height % ballSize.height == 0, "Paddle height must be evenly divisible by ball height")
  assert(roomSize.width % ballSize.width == 0, "Room height must be evenly divisible by ball height")
  assert(roomSize.height % ballSize.height == 0, "Room width must be evenly divisible by ball width")

  val TRANSFORMS = List(
    updateBallPosition _
  )

  def step(state: PongState, playerOneInput: Option[PlayerInput], playerTwoInput: Option[PlayerInput]): PongState = {
    TRANSFORMS.foldLeft(state)((state, transform) => transform(state))
  }

  def updateBallPosition(s: PongState): PongState = {
    val transform = modify(_: PongState)(_.ball.position).using(_.update(s.ball.velocity))
    transform(s)
  }
}
