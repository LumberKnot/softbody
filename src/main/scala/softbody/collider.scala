package softbody


import utilities.Vector2

/**Inmutable tuple of positions*/
class Collider(corners : (Vector2,Vector2)) extends drawObject:

    /**x_min, y_min, x_max,  y_max */
    lazy val bounds : Vector[Double] = Vector(
        corners._1.x min corners._2.x,
        corners._1.y min corners._2.y,
        corners._1.x max corners._2.x,
        corners._1.y max corners._2.y)

    override def draw : Unit = ???

    override def erase : Unit = ???

    lazy val midPoint  : Vector2 = corners._1 + ((corners._1 -> corners._2)/2)
    lazy val maxLengthSquared : Double  = ((corners._1 -> corners._2)/2).length * ((corners._1 -> corners._2)/2).length 

    /**More optimized method to rule out most colliders*/
    def isNear(point : Vector2) : Boolean =
        (point -> midPoint).lengthSquared <= maxLengthSquared
    
    def isIn(point : Vector2) : Boolean =
        (bounds(0) <= point.x && bounds(1) <= point.y && bounds(2) >= point.x && bounds(3) >= point.y)
