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
  val MIDDLE = Position(ROOM_SIZE.width/2, ROOM_SIZE.height/2)
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

        "touches player ones wall" - {
          val postCollision = PONG.step(updateBallPos(initialState).using(_ => Position(BALL_SIZE.width/2, MIDDLE.y)), None, None)

          "increments player twos score" - {
            assert(postCollision.score.playerTwo == 1)
          }
          "resets the round" - {
            assert(postCollision.paddles == initialState.paddles)
            assert(postCollision.ball == initialState.ball)
          }
        }
        "touches player twos wall" - {
          val postCollision = PONG.step(updateBallPos(initialState).using(_ => Position(ROOM_SIZE.width + BALL_SIZE.width, MIDDLE.y)), None, None)

          "increments player ones score" - {
            assert(postCollision.score.playerOne == 1)
          }
          "resets the round" - {
            assert(postCollision.paddles == initialState.paddles)
            assert(postCollision.ball == initialState.ball)
          }
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
