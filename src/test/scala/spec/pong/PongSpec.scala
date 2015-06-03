package spec.pong

import utest._
import utest.ExecutionContext.RunNow
import pong._
import LensTransforms._

object PongSpec extends TestSuite {
  val BALL_SIZE = Size(16, 16)
  val PADDLE_SIZE = Size(16, 64)
  val ROOM_SIZE = Size(512, 256)
  val PONG = new Pong(BALL_SIZE,
                      PADDLE_SIZE,
                      ROOM_SIZE)
  val initialState = PONG.generate

  val tests = TestSuite {
    'Pong {


      'ball {
        "updates its velocity every step" - {
          val expectedState = updateBallPos(initialState).using(_.update(initialState.ball.velocity))

          assert(PONG.updateBallPosition(initialState) == expectedState)
        }

        "Inverts x velocity when it contacts a paddle" - {
          val beforeCollisionState = updateBall(initialState).using(_ => Ball(
            position = initialState.paddles.playerOne.update(Velocity(PADDLE_SIZE.width / 2, 0)),
            velocity = Velocity(-16, 0)
          ))
          val afterCollisionState = updateBallVel(beforeCollisionState).using(_ => Velocity(16, 0))

          assert(PONG.ballCollision(beforeCollisionState) == afterCollisionState)
        }

        "inverts y velocity when it contacts a wall" - {
          val beforeCollisionState = updateBall(initialState).using(_ => Ball(
            position = Position(ROOM_SIZE.width / 2, ROOM_SIZE.height + (BALL_SIZE.height / 2)),
            velocity = Velocity(16, 16)
          ))
          val afterCollisionState = updateBallVel(beforeCollisionState).using(_ => Velocity(16, -16))

          assert(PONG.ballCollision(beforeCollisionState) == afterCollisionState)
        }
      }
      'ballCollision {
        "doesn't detect any collisions on an initial game state" - {
          assert(PONG.ballCollision(initialState) == initialState)
        }
      }
    }
  }
}
