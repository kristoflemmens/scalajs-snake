import shared.model._
import shared.physics.Vec2

package object shared {
  val terrainX = 100
  val terrainY = 75
  lazy val terrainVec = Vec2(terrainX, terrainY)

  val snakeBodyUnitSize = Vec2(1, 1)
  val snakeBodyInitLength = 5

  val defaultSpeed = 0.5

  val areaOccupiedPerPosition = 1 * 1

  val fps = 30

  val serverBufferFrameSize = 2 * fps   // 2 seconds

  val millisNeededPerUpdate = 1000 / fps

  def unitPerDirection(dir: Direction): Vec2 = dir match {
    case Up => Vec2(0, -1)
    case Down => Vec2(0, 1)
    case model.Right => Vec2(1, 0)
    case model.Left => Vec2(-1, 0)
  }
}
