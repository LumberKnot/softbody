package softbody

import utilities.*

object Masspoint:
    val mass = 1

    
class Masspoint(var startPos :Vector2) extends movable:
        
    override def toString =
        s"Masspoint at (${pos.x},${pos.y})"

    var force    : Vector2 = Vector2(0,0)
    var velocity : Vector2 = Vector2(0,0)
    var pos      : Vector2 = startPos

    override def draw : Unit = ???

    override def erase : Unit = ???

    override def update : Unit = ???

    override def reset : Unit = ???

    def move : Unit =
        //updade velocity and then position
        velocity = velocity + force
        pos = pos + velocity
    
    def addForce(_force : Vector2) : Unit =
        force = force + _force
    
    def clearForce : Unit =
        force = Vector2(0,0)

    def clearVelocity : Unit =
        velocity = Vector2(0,0)


