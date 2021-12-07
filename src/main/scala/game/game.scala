package game
import softbody.*
import javax.management.modelmbean.ModelMBean

object Game:
    enum State:
        case Drawing, Simulating, Quitting

    enum Drawingmode:
        case Softbody, Collider
    
    export State.*, Drawingmode.*

class Game(dim : (Int,Int) = (800,500)
) extends Engine(dim = dim):

    import Game.*

    protected var state : State       = Drawing
    protected var mode  : Drawingmode = Softbody
    private var isPaused = false

    
    var masspoints : Vector[Masspoint]       = Vector()
    var springs    : Vector[Spring]          = Vector()
    var colliders  : Vector[Collider]        = Vector()
    
    //Not acttualy a buffer at all :)
    def drawBuffer : Vector[drawObject] = masspoints ++ springs ++ colliders
    
    gameLoop(stopWhen = state == Quitting)

    override def onKeyDown(key: String): Unit = 
        println(s"""key "$key" pressed""")
        state match 
        case Drawing => 
            if key == "R" then enterSimulatingState()
            else if key == "M" then
                mode = Drawingmode.fromOrdinal((mode.ordinal + 1) % 2 )
                println(s"Changing to $mode")

        case Simulating =>
            if key == "Esc" then
                println(s"Toggle pause: isPaused == $isPaused")
                isPaused = !isPaused
            else if key == "R" then
                println("Restarting")
                enterDrawingState()
            else if key == "Backspace" then
                enterQuittingState()

        case _ =>

    
    override def onMouseDown(pos : (Int,Int)) : Unit =
        if state == Drawing then
            mode match
                case Softbody => ???

                case Collider => ???


    def enterDrawingState() : Unit =
        state = Drawing

    def enterSimulatingState() : Unit = 
        state = Simulating
    
    def enterQuittingState(): Unit = 
        println("Goodbye!")
        pixelWindow.hide()
        state = Quitting
    
    
    override def onClose(): Unit = 
        println("Window Closed!")
        enterQuittingState()

    override def draw() : Unit = 
        drawBuffer.foreach(_.draw)
    
    override def gameLoopAction() : Unit =
        //applicerar alla krafter som inte har med kollision att göra
        masspoints.foreach(_.clearForce)
        masspoints.foreach(_.gravity)
        springs.foreach(_.applyForce)
        masspoints.foreach(_.move)

        //kolliderar med alla colliders
        masspoints.foreach(
            point => colliders.foreach(_.collide(point))
        )


