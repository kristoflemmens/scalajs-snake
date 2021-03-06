package shared.protocol

import shared.core.IdentifiedGameInput
import shared.model.{Apple, Snake}
import shared.physics.AABB

import scala.collection.mutable

sealed trait GameResponse

case class GameState(snakes: Seq[Snake], apples: Set[Apple], seqNo: Int) extends GameResponse {
  def increaseSeqNo: GameState = this.copy(seqNo = seqNo + 1)

  private val emptyTable = mutable.HashMap.empty[AABB, Boolean]

  def isEmpty(point: AABB): Boolean = {
    emptyTable.getOrElse(point, {
      val notEmpty =
        snakes.exists(snk => snk.body.exists(_.collided(point))) || apples.exists(_.position.collided(point))
      emptyTable.put(point, !notEmpty)
      !notEmpty
    })
  }

  def hasSnake(id: String): Boolean = snakes.exists(_.id == id)
}

case class AssignedID(id: String) extends GameResponse

case class GameStateDelta(inputs: Seq[IdentifiedGameInput], seqNo: Int) extends GameResponse

object GameState { def init = GameState(Seq.empty, Set.empty, 0) }
