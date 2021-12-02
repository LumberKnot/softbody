package softbody

import utilities.*

object Masspoint:
    val g = 0.5

    
class Masspoint(var startPos : Vector2) extends drawObject:
    import Masspoint.*
        
    override def toString =
        s"Masspoint at (${pos.x},${pos.y})"

    var force    : Vector2 = Vector2(0,0)
    var velocity : Vector2 = Vector2(0,0)
    var pos      : Vector2 = startPos

    override def draw : Unit = ???

    override def erase : Unit = ???
        
    /**updade velocity and then position*/
    def move : Unit =
        velocity = velocity + force
        pos = pos + velocity

    def gravity : Unit =
        addForce(Vector2(0,-g))
    
    def addForce(_force : Vector2) : Unit =
        force = force + _force
    
    def clearForce : Unit =
        force = Vector2(0,0)

    def clearVelocity : Unit =
        velocity = Vector2(0,0)
    

    
/*Ordning
clear force
gravity
springs.addForce
move
Coliders?
*/



