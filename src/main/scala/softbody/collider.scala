package softbody


import utilities.Vector2
import game.Engine

/**Companionsobj*/
object Collider:
    val edgeColor = java.awt.Color(0,51,0)
    val fillColor = java.awt.Color(0,255,0)

    def apply(corners : (Vector2,Vector2))(using engine : Engine) =
        new Collider(corners)(engine)

/**Inmutable tuple of positions*/
class Collider(corners : (Vector2,Vector2))(engine : Engine) extends drawObject:
    import Collider.*

    /**x_min, y_min, x_max,  y_max */
    lazy val bounds : Vector[Double] = Vector(
        corners._1.x min corners._2.x,
        corners._1.y min corners._2.y,
        corners._1.x max corners._2.x,
        corners._1.y max corners._2.y)

    override def draw : Unit = 
        engine.drawBoxWithEdges(corners._1 , corners._2 ,edgeColor, fillColor)
    
    lazy val midPoint  : Vector2 = corners._1 + ((corners._1 -> corners._2)/2)
    lazy val maxLengthSquared : Double  = ((corners._1 -> corners._2)/2).length * ((corners._1 -> corners._2)/2).length 

    /**More optimized method to rule out most colliders*/
    def isNear(point : Vector2) : Boolean =
        (point -> midPoint).lengthSquared <= maxLengthSquared
    
    def isIn(point : Vector2) : Boolean =
        (bounds(0) <= point.x && bounds(1) <= point.y && bounds(2) >= point.x && bounds(3) >= point.y)
    
    
    /**Corrects any masspoints that have entered the collider
     * 
     * currently only in y
    */
    def collide(point : Masspoint) : Unit =
        if isIn(point.pos) then
            //above
            if point.pos.y > midPoint.y then
                point.clearVerticalVelocity
                point.setPos(Vector2(point.pos.x,bounds(3)))
            //Below add if when x becomes relevant
            else
                point.clearVerticalVelocity
                point.setPos(Vector2(point.pos.x , bounds(1)))
                




