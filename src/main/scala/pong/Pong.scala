package pong

import LensTransforms._
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
    updateBallPosition _,
    ballCollision _
  )

  def step(state: PongState, playerOneInput: Option[PlayerInput], playerTwoInput: Option[PlayerInput]): PongState = {
    TRANSFORMS.foldLeft(state)((state, transform) => transform(state))
  }

  def updateBallPosition(s: PongState): PongState = {
    updateBallPos(s).using(_.update(s.ball.velocity))
  }
  def ballCollision(s: PongState): PongState = {
    def paddleCollision(s: PongState): Boolean = {
      val paddles = List(s.paddles.playerOne, s.paddles.playerTwo)
      paddles.exists(paddle => intersects(s.ball.position, ballSize, paddle, paddleSize))
    }
    
    if(paddleCollision(s))
      updateBallVel(s).using(vel => vel.copy(x = -vel.x))
    else
      s
  }

  def intersects(positionOne: Position, sizeOne: Size, positionTwo: Position, sizeTwo: Size): Boolean = {
    case class Rectangle(x1: Int, x2: Int, y1: Int, y2: Int)
    def getBoundingBox(position: Position, size: Size): Rectangle = {
      Rectangle(
        positionOne.x - (sizeOne.width / 2),
        positionOne.x + (sizeOne.width / 2),
        positionOne.y - (sizeOne.height / 2),
        positionOne.y + (sizeOne.height / 2)
      )
    }
    val rectA = getBoundingBox(positionOne, sizeOne)
    val rectB = getBoundingBox(positionTwo, sizeTwo)

    val noOverlap =  rectA.x1 > rectB.x2 ||
                     rectB.x1 > rectA.x2 ||
                     rectA.y1 > rectB.y2 ||
                     rectB.y1 > rectA.y2
    !noOverlap
  }
}
