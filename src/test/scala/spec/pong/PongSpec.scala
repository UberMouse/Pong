package spec.pong

import org.scalatest.FunSpec
import org.scalatest.BeforeAndAfter
import pong.{Velocity, PongState, Size, Pong}
import com.softwaremill.quicklens._

class PongSpec extends FunSpec with BeforeAndAfter {

  describe("Pong") {
    val BALL_SIZE = Size(16, 16)
    val PADDLE_SIZE = Size(16, 64)
    val ROOM_SIZE = Size(512, 256)
    val PONG = new Pong(BALL_SIZE,
                        PADDLE_SIZE,
                        ROOM_SIZE)

    var initialState: PongState = null
    before {
      initialState = PongState.build(BALL_SIZE, PADDLE_SIZE, ROOM_SIZE)
    }

    describe("ball") {
      it("updates its velocity every step") {
        val stateTransform = modify(_:PongState)(_.ball.position).using(_.update(initialState.ball.velocity))

        assert(PONG.step(initialState, None, None) == stateTransform(initialState))
      }

      it("Inverts x velocity when it contacts a paddle") {
      }
    }
  }
}
