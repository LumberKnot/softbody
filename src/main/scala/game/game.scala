package game

import softbody.*
import utilities.Vector2

object Game:

    val optimalSpringLength : Double = 15

    enum State:
        case Drawing, Simulating, Quitting

    enum Drawingmode:
        case SoftbodyDrawing, ColliderDrawing
    
    export State.*, Drawingmode.*

class Game(dim : (Int,Int) = (800,500)
) extends Engine(dim = dim):

    import Game.*
    
    given Engine = this

    protected var state : State       = Drawing
    protected var mode  : Drawingmode = ColliderDrawing
    private var isPaused = false

    private var firstPointSelected = false
    private var firstPoint : Vector2 = Vector2(0,0)

    
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
            if key == "r" then 
                enterSimulatingState()
            else if key == "m" then
                mode = Drawingmode.fromOrdinal((mode.ordinal + 1) % 2 )
                println(s"Changing to $mode")
            else if key == "Backspace" then
                deleteLast

        case Simulating =>
            if key == "Esc" then
                println(s"Toggle pause: isPaused == $isPaused")
                isPaused = !isPaused
            else if key == "r" then
                println("Restarting")
                enterDrawingState()
            else if key == "Backspace" then
                enterQuittingState()

        case _ =>

    
    override def onMouseDown(pos : (Int,Int)) : Unit =
        if state == Drawing then
            val vectorPos = Vector2(pos._1, pos._2)
            if !firstPointSelected then
                firstPoint = vectorPos
                firstPointSelected=true
            else 
                mode match
                    case SoftbodyDrawing => 
                        print("oh boy")


                    case ColliderDrawing => 
                        colliders = colliders :+ Collider(firstPoint , vectorPos)
                        firstPointSelected =false


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
    
    def deleteLast : Unit =
        mode match
            case SoftbodyDrawing => ???
                
            case ColliderDrawing =>
                if colliders.nonEmpty then
                    println("vist en fuking member")
                    colliders = colliders.reverse.drop(1).reverse
                
    
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


