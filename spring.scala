package softbody

import utilities.Vector2

object spring:
    def apply(atached : Vector[Masspoint]) =
        if (atached.length == 2) then println("works");new spring(atached)
    
    val k = 0.01 //hookes lag, k-> mindre soft (k-> inf är rigidbody)

class spring(val atached: Vector[Masspoint]):
    import spring.*

    //Startlängd bestämmer hur den ska bete sig i framtiden
    val length = getLenght

    def getLenght : Double =
        (atached(0).pos - atached(1).pos).length

    override def toString =
        s"Spring between ${atached(0)} , ${atached(1)} with length $length"

    def applyForce : Unit =
        //F = k * delta_legnth
        val deltaL = (getLenght - length)

        //gör samma för båda punkterna
        for (id <- 0 to 1) do
            val vec = atached(id).pos -> atached((id+1)%2).pos //få en vektor i rätt riktning

            //omvandlar vektorn till en med storlek 1 och sedan multiplicerar med k * deltaL
            atached(id).addForce((vec/ vec.length.toFloat) * (k*deltaL).toFloat) 

        
    
    
    