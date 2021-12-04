package softbody

import utilities.Vector2

object Spring:
    def apply(atached : Vector[Masspoint]) =
        if (atached.length == 2) then println("works");new spring(atached)
    
    val k = 0.01 //hookes lag, k-> mindre soft (k-> inf är rigidbody)

class Spring(val atached: Vector[Masspoint]) extends drawObject:
    import spring.*

    override def draw : Unit = ???

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

        
    
    
    