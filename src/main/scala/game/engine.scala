package game

import java.awt.Color
import utilities.Vector2

abstract class Engine(
  val title: String = "Softbody",
  val dim: (Int, Int) = (800, 500),
  val background: Color = Color.black,
  var framesPerSecond: Int = 30,
):
  import introprog.PixelWindow

  /** Called when a key is pressed. Override if you want non-empty action.
    * @param key is a string representation of the pressed key
    */
  def onKeyDown(key: String): Unit = ()

  /** Called when a key is released. Override if you want non-empty action.
    * @param key is a string representation of the released key
    */
  def onKeyUp(key: String): Unit = ()

  /** Called when mouse is pressed. Override if you want non-empty action.
    * @param pos the mouse position in underlying `pixelWindow` coordinates
    */
  def onMouseDown(pos: (Int, Int)): Unit = ()

  /** Called when mouse is released. Override if you want non-empty action.
    * @param pos the mouse position in underlying `pixelWindow` coordinates
    */
  def onMouseUp(pos: (Int, Int)): Unit = ()

  /** Called when window is closed. Override if you want non-empty action. */
  def onClose(): Unit = ()

  /** Called in each `gameLoop` iteration. Override if you want non-empty action. */
  def gameLoopAction(): Unit = ()

  /**Overide to add graphics logic*/
  def draw() : Unit = ()

  /** Called if no time is left in iteration to keep frame rate.
    * Default action is to print a warning message.
    */
  def onFrameTimeOverrun(elapsedMillis: Long): Unit =
    println(s"Warning: Unable to handle $framesPerSecond fps. Loop time: $elapsedMillis ms")

  /** Returns the gameLoop delay in ms implied by `framesPerSecond`.*/
  def gameLoopDelayMillis: Int = (1000.0 / framesPerSecond).round.toInt

  /** The underlying window used for drawing*/
  protected val pixelWindow: PixelWindow =
    new PixelWindow(
      width = dim._1, 
      height = dim._2,
      title, 
      background
    )

    /** Max time for awaiting events from underlying window in ms. */
  protected val MaxWaitForEventMillis = 1

  /** The game loop that continues while not `stopWhen` is true.
    * It calls each `onXXX` method if a corresponding event is detected.
    */
  protected def gameLoop(stopWhen: => Boolean): Unit = 
    while !stopWhen do
      import PixelWindow.Event
      val t0 = System.currentTimeMillis
      pixelWindow.awaitEvent(MaxWaitForEventMillis.toLong)
      while pixelWindow.lastEventType != PixelWindow.Event.Undefined do
        pixelWindow.lastEventType match
          case Event.KeyPressed    => onKeyDown(pixelWindow.lastKey)
          case Event.KeyReleased   => onKeyUp(pixelWindow.lastKey)
          case Event.WindowClosed  => onClose()
          case Event.MousePressed  => onMouseDown(pixelWindow.lastMousePos)
          case Event.MouseReleased => onMouseUp(pixelWindow.lastMousePos)
          case _ =>
        pixelWindow.awaitEvent(1)
      pixelWindow.clear()
      draw()
      gameLoopAction()
      val elapsed = System.currentTimeMillis - t0
      if (gameLoopDelayMillis - elapsed) < MaxWaitForEventMillis then
        onFrameTimeOverrun(elapsed)
      Thread.sleep((gameLoopDelayMillis - elapsed) max 0)



    /**Draws pixelwindow.line with Vector2s*/
  def drawLine(a: Vector2, b : Vector2, color : Color) : Unit =
    pixelWindow.line(a.x.toInt ,a.y.toInt ,b.x.toInt , b.y.toInt , color)
  
  def drawBoxWithEdges(a : Vector2, b : Vector2 , edgeColor : java.awt.Color, fillColor : java.awt.Color) : Unit =
    
    pixelWindow.fill(
      (a.x min b.x).toInt,
      (a.y min b.y).toInt,
      width = math.abs(a.x - b.x).toInt,
      height = math.abs(a.y - b.y).toInt,
      color = fillColor
    )

    pixelWindow.line(a.x.toInt , a.y.toInt , b.x.toInt , a.y.toInt , edgeColor,5)
    pixelWindow.line(a.x.toInt , a.y.toInt , a.x.toInt , b.y.toInt , edgeColor,5)

    pixelWindow.line(b.x.toInt , b.y.toInt , b.x.toInt , a.y.toInt , edgeColor,5)
    pixelWindow.line(b.x.toInt , b.y.toInt , a.x.toInt , b.y.toInt , edgeColor,5)


  
  /**Draws a square atm
   * 
   * might try to fix l8ter
  */
  def drawDot(pos : Vector2 , color : java.awt.Color, size : Int = 10) =
    pixelWindow.fill((pos.x - size/2).toInt , (pos.y -size/2).toInt , size , size , color)
