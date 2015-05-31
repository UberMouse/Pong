package pong

/**
 * Initializes a new pong instance
 * @param ballSize height, width of ball. Must be even
 * @param paddleSize height, width of paddle. Height/width must be
 *                   evenly divisible by ball height/width
 * @param roomSize height, width of room. Height/width must be
 *                   evenly divisible by ball height/width
 */
class Pong(ballSize: Vector2, paddleSize: Vector2, roomSize: Vector2) {
  assert(ballSize.x % 2 == 0, "Ball width must be even")
  assert(ballSize.y % 2 == 0, "Ball height must be even")
  assert(ballSize.x % paddleSize.x == 0, "Paddle width must be evenly divisible by ball width")
  assert(ballSize.y % paddleSize.y == 0, "Paddle height must be evenly divisible by ball height")
  assert(ballSize.x % roomSize.x == 0, "Room height must be evenly divisible by ball height")
  assert(ballSize.y % roomSize.y == 0, "Room width must be evenly divisible by ball width")

  def step(state: PongState, playerOneInput: PlayerInput, playerTwoInput: PlayerInput): PongState = {
    state
  }
}
