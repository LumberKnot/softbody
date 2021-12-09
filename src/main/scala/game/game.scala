package game

import softbody.*
import utilities.Vector2

object Game:

    val optimalSpringLength : Double = 50

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

    
    var softbodys  : Vector[Softbody] = Vector()  
    var colliders  : Vector[Collider] = Vector()
    
    //Not acttualy a buffer at all :)
    def drawBuffer : Vector[drawObject] = softbodys ++ colliders
    
    gameLoop(stopWhen = state == Quitting)

    override def onKeyDown(key: String): Unit = 
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
            else if key == "d" then
                drawBuffer.foreach(_.debug)
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
                        softbodys = softbodys :+ Softbody(firstPoint , vectorPos)
                        firstPointSelected=false


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
            case SoftbodyDrawing =>
                if(softbodys.nonEmpty) then 
                    bufferClearAreas += softbodys.last.getBoundingBox()
                    softbodys = softbodys.take(softbodys.length - 1)
                
            case ColliderDrawing =>
                if colliders.nonEmpty then
                    bufferClearAreas += colliders.last.getVectorBounds()
                    colliders = colliders.take(colliders.length-1)
                
    

    override def gameLoopAction() : Unit =
        if state == Simulating && !isPaused then

            softbodys.foreach(_.forces)
            softbodys.foreach(_.collisions(colliders))
            softbodys.foreach(_.move)

            softbodys.foreach(bufferClearAreas += _.getBoundingBox())

