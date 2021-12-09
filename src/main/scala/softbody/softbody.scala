package softbody

import softbody.*
import utilities.*
import game.Engine

object Softbody:
    val drawMasspoints  : Boolean = false
    val drawSprings     : Boolean = true
    val drawBoundingBox : Boolean = false

    val boxColor = java.awt.Color.green

    val optimalSpringLength = 50

    /**Fariksmetod för att skapa en softbody*/
    def apply(first : Vector2 , second : Vector2)(using engine : Engine) : Softbody =

        var masspoints : Vector[Masspoint] = Vector()
        var springs    : Vector[Spring]    = Vector()

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
            
            //lägger till snea fjädrar <- 
            if (id >= cols +1 && id % cols != 0) then
                springs = springs :+ Spring(Vector(masspoints(id), masspoints(id-cols -1)))
            
            //läger till snea fjädrar ->
            if(id >= cols && (id + 1)% cols != 0) then
                springs = springs :+ Spring(Vector(masspoints(id), masspoints(id-cols + 1)))
            
        new Softbody(masspoints, springs)(engine)



class Softbody(masspoints : Vector[Masspoint], springs : Vector[Spring])(engine : Engine) extends drawObject:

    import Softbody.*

    override def draw : Unit =
        if drawMasspoints  then masspoints.foreach(_.draw)
        if drawSprings     then springs.foreach(_.draw)
        if drawBoundingBox then engine.drawEdges(getBoundingBox()._1 , getBoundingBox()._2 , boxColor)


    override def debug : Unit =()

    def forces : Unit =
        //applicerar alla krafter som inte har med kollision att göra
        masspoints.foreach(_.clearForce)
        masspoints.foreach(_.gravity)
        springs.foreach(_.applyForce)
    
    def collisions(colliders : Vector[Collider]) : Unit =
        //kolliderar med alla colliders
        masspoints.foreach(
            point => colliders.foreach(_.collide(point))
            )
    def move : Unit =
        masspoints.foreach(_.move)


    /**Returns two vectors representing the boundingbox of the softbody*/
    def getBoundingBox() : (Vector2, Vector2) =
        var x_min = masspoints(0).pos.x
        var x_max = masspoints(0).pos.x
        var y_min = masspoints(0).pos.y
        var y_max = masspoints(0).pos.y

        masspoints.drop(1).foreach(point =>
            
            if point.pos.x < x_min then x_min = point.pos.x
            if point.pos.x > x_max then x_max = point.pos.x
            if point.pos.y < y_min then y_min = point.pos.y
            if point.pos.y > y_max then y_max = point.pos.y
            )

        (Vector2(x_min,y_min) , Vector2(x_max,y_max))


  

