package softbody

import utilities.Vector2
import game.Engine

object Spring:
    val lineColor : java.awt.Color = java.awt.Color.red

    def apply(atached : Vector[Masspoint])(using ctx : Engine) : Spring =
        if (atached.length == 2) then (); new Spring(atached)(ctx)    
    //WTF varför funkar det bara med en unit där??
    
    val k = 0.1 //hookes lag, k-> mindre soft (k-> inf är rigidbody)

class Spring(val atached: Vector[Masspoint])(engine : Engine )extends drawObject:
    import Spring.*

    override def draw : Unit = 
        engine.drawLine(atached(0).pos , atached(1).pos , lineColor)

    override def debug : Unit = ()

    //Startlängd bestämmer hur den ska bete sig i framtiden
    val untensionedLength = getLenght

    def getLenght : Double =
        (atached(0).pos -> atached(1).pos).length 

    override def toString =
        s"Spring between ${atached(0)} , ${atached(1)} with length $untensionedLength"

    /**Aplies force to both maspoints*/
    def applyForce : Unit =
        //F = k * delta_legnth
        val deltaL = (getLenght - untensionedLength)

        //gör samma för båda punkterna
        for (masspoint <- atached) do

            val directionalVector = masspoint.pos -> atached.filterNot(_ == masspoint)(0).pos //få en vektor i rätt riktning

            //omvandlar vektorn till en med storlek 1 och sedan multiplicerar med k * deltaL
            masspoint.addForce(directionalVector.normalized* (k * deltaL))

        
    
    
    