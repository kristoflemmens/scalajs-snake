package client.infrastructure

import monix.execution.Cancelable
import monix.reactive.{Observable, OverflowStrategy}
import org.scalajs.dom.raw.{HTMLElement, KeyboardEvent}
import shared.model
import shared.protocol.{ChangeDirection, GameCommand, GameRequest}

object InputControl {
  def captureEvents(element: HTMLElement): Observable[GameRequest] = {
    Observable.create(OverflowStrategy.Unbounded) { sync =>
      element.addEventListener[KeyboardEvent]("keydown", (ev: KeyboardEvent) => {
        val keyToCmd: PartialFunction[Int, GameCommand] =  {
          case 37 => ChangeDirection(model.Left)
          case 38 => ChangeDirection(model.Up)
          case 39 => ChangeDirection(model.Right)
          case 40 => ChangeDirection(model.Down)
        }

        if (keyToCmd.isDefinedAt(ev.keyCode))
          sync.onNext(GameRequest(keyToCmd(ev.keyCode)))

      }, true)

      Cancelable(() => {
        sync.onComplete()
      })
    }
  }
}