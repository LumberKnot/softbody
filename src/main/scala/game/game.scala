package game
import softbody.*

class Game(dim : (Int,Int),
masspoints : Vector[Masspoint], 
springs : Vector[Spring],
colliders : Vector[Collider],
) extends Engine(dim = dim):

    //Not acttualy a buffer at all :)
    val drawBuffer : Vector[drawObject] = masspoints ++ springs ++ colliders


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


