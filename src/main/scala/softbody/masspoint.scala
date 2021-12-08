package softbody

import utilities.*
import game.Engine

object Masspoint:
    val g = 0.1
    val color = java.awt.Color(153, 51, 51)

    def apply(startPos : Vector2)(using engine : Engine) : Masspoint = 
        new Masspoint(startPos)(engine)

    
class Masspoint(val startPos : Vector2)(engine : Engine) extends drawObject:
    import Masspoint.*
        
    override def toString =
        s"Masspoint at (${pos.x},${pos.y})"

    override def debug : Unit =
        println(toString)

    var force    : Vector2 = Vector2(0,0)
    var velocity : Vector2 = Vector2(0,0)
    var pos      : Vector2 = startPos

    override def draw : Unit = 
        engine.drawDot(pos,color,10)
            
    /**updade velocity and then position*/
    def move : Unit =
        velocity = velocity + force
        pos = pos + velocity

    def gravity : Unit =
        addForce(Vector2(0,g))
    
    def addForce(_force : Vector2) : Unit =
        force = force + _force
    
    def clearForce : Unit =
        force = Vector2(0,0)

    def clearVelocity : Unit =
        velocity = Vector2(0,0)
    
    def clearVerticalVelocity : Unit = 
        velocity = Vector2(velocity.x,0)
    
    def clearHorizontalVelocity : Unit =
        velocity = Vector2(0,velocity.y)
    
    def setPos(_pos : Vector2) =
        pos = _pos

    




