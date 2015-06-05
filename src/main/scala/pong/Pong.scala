package pong

import LensTransforms._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/**
 * Initializes a new pong instance
 * @param ballSize height, width of ball. Must be even
 * @param paddleSize height, width of paddle. Height/width must be
 *                   evenly divisible by ball height/width
 * @param roomSize height, width of room. Height/width must be
 *                   evenly divisible by ball height/width
 */
@JSExport
class Pong(ballSize: Size, paddleSize: Size, roomSize: Size, ballInitialVelocity: Velocity, paddleMovementPerFrame: Int) {
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
    handleInput _,
    ballCollision _
    //updateRoundStatus _
  )

  // ==== Public API ====

  @JSExport
  def step(state: PongState, playerOneInput: Option[PlayerInput], playerTwoInput: Option[PlayerInput]): PongState = {
    TRANSFORMS.foldLeft(state)((state, transform) => transform(
      state,
      playerOneInput.getOrElse(NoInput()),
      playerTwoInput.getOrElse(NoInput())
    ))
  }

  @JSExport
  def jsStep(state: PongState, playerOneInput: js.UndefOr[PlayerInput], playerTwoInput: js.UndefOr[PlayerInput]): PongState = {
    step(state, playerOneInput.toOption, playerTwoInput.toOption)
  }

  @JSExport
  def generate = {
    val MIDDLE = Position(roomSize.width / 2, roomSize.height / 2)
    val P1_PADDLE = Position(ballSize.width * 4, MIDDLE.y)
    val P2_PADDLE = Position(roomSize.width - (ballSize.width * 4), MIDDLE.y)

    PongState(
      ball = Ball(
        position = MIDDLE,
        velocity = ballInitialVelocity
      ),
      paddles = Paddles(
        playerOne = P1_PADDLE,
        playerTwo = P2_PADDLE
      ),
      score = Score(0, 0)
    )
  }

  // ==== Transforms ====

  def updateBallPosition(s: PongState, p1Input: PlayerInput, p2Input: PlayerInput): PongState = {
    updateBallPos(s).using(_.update(s.ball.velocity))
  }

  def ballCollision(s: PongState, p1Input: PlayerInput, p2Input: PlayerInput): PongState = {
    def paddleCollision(s: PongState): Boolean = {
      val paddles = List(s.paddles.playerOne, s.paddles.playerTwo)
      paddles.exists(paddle => intersects(s.ball.position, ballSize, paddle, paddleSize))
    }
    def wallCollision(s: PongState): Boolean = {
      val ballPos = s.ball.position
      ballPos.x > roomSize.width ||
      ballPos.x < 0 ||
      ballPos.y > roomSize.height ||
      ballPos.y < 0
    }

    if(paddleCollision(s))
      updateBallVel(s).using(vel => vel.copy(x = -vel.x))
    else if(wallCollision(s))
      updateBallVel(s).using(vel => vel.copy(y = -vel.y))
    else
      s
  }

  def updateRoundStatus(s: PongState, p1Input: PlayerInput, p2Input: PlayerInput): PongState = {
    val initialState = generate
    val ballPosition = s.ball.position
    List(
      (ballPosition.x <= 0, updateP2Score(s).using(current => current + 1)),
      (ballPosition.x >= roomSize.width, updateP1Score(s).using(current => current + 1))
    ).filter(_._1)
     .map(_._2)
     .map(_.copy(
          ball = initialState.ball,
          paddles = initialState.paddles
        )
     )
     .headOption
     .getOrElse(s)
  }

  def handleInput(s: PongState, p1Input: PlayerInput, p2Input: PlayerInput): PongState = {
    // Couldn't figure out how to get something like this working, just went the shit route
    //    List(
    //      (p1Input, updateP1PaddlePos(s)),
    //      (p2Input, updateP2PaddlePos(s))
    //    ).foldLeft(s)((state, inputInformation) => {
    //       val state = inputInformation._2
    //       inputInformation._1 match {
    //         case Up() =>
    //           state.using(pos => pos.copy(y = pos.y + paddleMovementPerFrame))
    //         case Down() =>
    //           state.using(pos => pos.copy(y = pos.y - paddleMovementPerFrame))
    //         case NoInput() => state.using(x => x)
    //       }
    //      }
    //    )
    val updatedState = p1Input match {
      case Up() =>
        updateP1PaddlePos(s).using(pos => pos.copy(y = pos.y - paddleMovementPerFrame))
      case Down() =>
        updateP1PaddlePos(s).using(pos => pos.copy(y = pos.y + paddleMovementPerFrame))
      case _ => s
    }
    p2Input match {
      case Up() =>
        updateP2PaddlePos(updatedState).using(pos => pos.copy(y = pos.y - paddleMovementPerFrame))
      case Down() =>
        updateP2PaddlePos(updatedState).using(pos => pos.copy(y = pos.y + paddleMovementPerFrame))
      case _ => updatedState
    }
  }

  // ==== Utility ====

  def intersects(positionOne: Position, sizeOne: Size, positionTwo: Position, sizeTwo: Size): Boolean = {
    case class Rectangle(x1: Int, x2: Int, y1: Int, y2: Int)
    def getBoundingBox(position: Position, size: Size): Rectangle = {
      Rectangle(
        position.x - (size.width / 2),
        position.x + (size.width / 2),
        position.y - (size.height / 2),
        position.y + (size.height / 2)
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
