package client

import client.api.SnakeGame
import client.infrastructure._
import org.scalajs.dom._

import scala.scalajs.js._
import org.scalajs.dom.raw._
import shared.protocol._

import monix.execution.Scheduler.Implicits.global

object BrowserSnakeGame extends JSApp {

  private def onSubmitName(): Boolean = {
    val name = document.getElementById("username-input").asInstanceOf[HTMLInputElement].value

    if (name != null && name != "") {
      DefaultWSSource.request(JoinGame(name))
      true
    } else {
      false
    }
  }

  def initDom() = {
    JSFacade
      .JQueryStatic("#username-modal")
      .modal(Dynamic.literal(autofocus = true, onHide = () => onSubmitName()))
      .modal("setting", "closable", false)
      .modal("show")

    val nameInput = document.getElementById("username-input").asInstanceOf[HTMLInputElement]
    document.addEventListener("keydown", (ev: KeyboardEvent) => {
      if (ev.keyCode == 13) {
        JSFacade.JQueryStatic("#username-modal").modal("hide")
      }
    })
  }

  def setCanvasFullScreen(canvas: html.Canvas) = {
    canvas.width = (window.innerWidth * 0.8).toInt
    canvas.height = window.innerHeight.toInt
    canvas.style.height = s"${window.innerHeight}px"
    canvas.style.width = s"${window.innerWidth * 0.8}px"
  }

  @annotation.JSExport
  override def main(): Unit = {
    // dom related
    val canvas    = document.getElementById("canvas").asInstanceOf[html.Canvas]
    val canvasCtx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    // dependency instantiation
    val renderer = new CanvasRenderer {
      override val ctx = canvasCtx
    }

    val input        = new KeyboardInput(document.asInstanceOf[HTMLElement])
    val scoreBoardTB = document.getElementById("scoreboard").firstElementChild.asInstanceOf[HTMLTableElement]

    val game = new SnakeGame(DefaultWSSource, renderer, ClientPredictor, input, new DomScoreRenderer(scoreBoardTB))

    setCanvasFullScreen(canvas)

    game.startGame()
    initDom()
  }
}