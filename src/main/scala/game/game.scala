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

    
    var masspoints : Vector[Masspoint]       = Vector()
    var springs    : Vector[Spring]          = Vector()
    var colliders  : Vector[Collider]        = Vector()
    
    //Not acttualy a buffer at all :)
    def drawBuffer : Vector[drawObject] = masspoints ++ springs ++ colliders
    
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
                masspoints.foreach(_.debug)
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
                        if masspoints.isEmpty then createSoftbody(firstPoint , vectorPos)
                        else print(masspoints.length)
                        firstPointSelected=false


                    case ColliderDrawing => 
                        colliders = colliders :+ Collider(firstPoint , vectorPos)
                        firstPointSelected =false


    /**Generates a softbody if and only if there exists none*/
    def createSoftbody(first : Vector2 , second : Vector2) : Unit = 
        //rita ett nät på papper

        /**x_min, y_min, x_max,  y_max */
        val bounds : Vector[Double] = Vector(
            first.x min second.x,
            first.y min second.y,
            first.x max second.x,
            first.y max second.y)
        
        val start : Vector2 = Vector2(bounds(0), bounds(1))
        val end : Vector2 = Vector2(bounds(2), bounds(3))
        
        val cols : Int = ((bounds(2) - bounds(0))/optimalSpringLength).round.toInt
        val rows : Int = ((bounds(3) - bounds(1))/optimalSpringLength).round.toInt

        val horizontalChange : Vector2 = Vector2((bounds(2) - bounds(0))/cols,0)
        val verticalChange   : Vector2 = Vector2(0,(bounds(3) - bounds(1))/rows)

        for (id <- 0 to (rows*cols)-1) do
            //lägger till en masspoint
            masspoints = masspoints :+ Masspoint(first 
            + (id % cols) *: horizontalChange + math.floor(id/cols) *: verticalChange
            )

            //lägger till sido fjädrar
            if (id % cols != 0) then
                springs = springs :+ Spring(Vector(masspoints(id), masspoints(id-1)))
            
            //lägger till uppåtfjädrar
            if (id >= cols) then
                springs = springs :+ Spring(Vector(masspoints(id), masspoints(id- cols)))


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
                masspoints = Vector()
                springs    = Vector()
                
            case ColliderDrawing =>
                if colliders.nonEmpty then
                    colliders = colliders.reverse.drop(1).reverse
                
    
    override def gameLoopAction() : Unit =
        if state == Simulating && !isPaused then
            //applicerar alla krafter som inte har med kollision att göra
            masspoints.foreach(_.clearForce)
            masspoints.foreach(_.gravity)
            springs.foreach(_.applyForce)
            
            //kolliderar med alla colliders
            masspoints.foreach(
                point => colliders.foreach(_.collide(point))
                )
                
            masspoints.foreach(_.move)


