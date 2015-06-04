package pong

import com.softwaremill.quicklens._
object LensTransforms {
  val updateBallPos = modify(_: PongState)(_.ball.position)
  val updateBallVel = modify(_: PongState)(_.ball.velocity)
  val updateBall = modify(_: PongState)(_.ball)

  val updateP1Score = modify(_: PongState)(_.score.playerOne)
  val updateP2Score = modify(_: PongState)(_.score.playerTwo)

  val updateP1PaddlePos = modify(_: PongState)(_.paddles.playerOne)
  val updateP2PaddlePos = modify(_: PongState)(_.paddles.playerTwo)
}
