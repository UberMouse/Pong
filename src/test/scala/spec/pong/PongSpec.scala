package spec.pong

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter
import pong._
import LensTransforms._

class PongSpec extends FunSpec with BeforeAndAfter {
  val BALL_SIZE = Size(16, 16)
  val PADDLE_SIZE = Size(16, 64)
  val ROOM_SIZE = Size(512, 256)
  val PONG = new Pong(BALL_SIZE,
                      PADDLE_SIZE,
                      ROOM_SIZE)

  describe("Pong") {

    var initialState: PongState = null
    before {
      initialState = PongState.build(BALL_SIZE, PADDLE_SIZE, ROOM_SIZE)
    }

    describe("ball") {
      it("updates its velocity every step") {
        val expectedState = updateBallPos(initialState).using(_.update(initialState.ball.velocity))

        assert(PONG.updateBallPosition(initialState) == expectedState)
      }

      it("Inverts x velocity when it contacts a paddle") {
        val beforeCollisionState = updateBall(initialState).using(_.copy(
          position = initialState.paddles.playerOne.update(Velocity(PADDLE_SIZE.width / 2, 0)),
          velocity = Velocity(-16, 0)
        ))
        val afterCollisionState = updateBallVel(beforeCollisionState).using(_ => Velocity(16, 0))

        assert(PONG.ballCollision(beforeCollisionState) == afterCollisionState)
      }
    }
  }
}
