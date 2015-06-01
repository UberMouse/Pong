package spec.pong

import org.scalatest.FunSpec
import pong.{PongState, Size, Pong}

class PongSpec extends FunSpec {
  describe("Pong") {
    val BALL_SIZE = Size(16, 16)
    val PADDLE_SIZE = Size(16, 64)
    val ROOM_SIZE = Size(512, 256)
    val PONG = new Pong(BALL_SIZE,
                        PADDLE_SIZE,
                        ROOM_SIZE)
    describe("ball") {
      it("updates its velocity every step") {
        val initialState = PongState.build(BALL_SIZE, PADDLE_SIZE, ROOM_SIZE)
        val expectedState = initialState.copy(
          ball = initialState.ball.copy(
            position = initialState.ball.position.update(initialState.ball.velocity)
          )
        )

        assert(PONG.step(initialState, None, None) == expectedState)
      }
    }
  }
}
