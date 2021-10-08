package softbody

import utilities.*

object Masspoint:
    val mass = 1

    
class Masspoint(var pos :Vector2):
        
    override def toString =
        s"Masspoint at (${pos.x},${pos.y})"

    var force :Vector2 = Vector2(0,0)
    var velocity : Vector2 = Vector2(0,0)

    def move : Unit =
        //uptade velocity and then position
        velocity = velocity + force
        pos = pos + velocity
    
    def addForce(_force : Vector2) : Unit =
        force = force + _force
    
    def clearForce : Unit =
        force = Vector2(0,0)

    def clearVelocity : Unit =
        velocity = Vector2(0,0)


