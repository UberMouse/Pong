package pong

import com.softwaremill.quicklens._
object LensTransforms {
  val updateBallPos = modify(_: PongState)(_.ball.position)
  val updateBallVel = modify(_: PongState)(_.ball.velocity)
  val updateBall = modify(_: PongState)(_.ball)
}
