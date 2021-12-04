package game

import java.awt.Color

abstract class Engine(
  val title: String = "Softbody",
  val dim: (Int, Int) = (800, 500),
  val background: Color = Color.black,
  var framesPerSecond: Int = 50,
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
    * It draws only updated blocks aiming at the desired frame rate.
    * It calls each `onXXX` method if a corresponding event is detected.
    */
  protected def gameLoop(stopWhen: => Boolean): Unit = while !stopWhen do
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