package utilities

case class Vector2(x: Double, y: Double):

    /**retunrns sum of two vectors as Vector2*/
    def +(other: Vector2) : Vector2 =
        Vector2(x + other.x, y + other.y)
    
    /**returns differarce of two vectors as Vector2*/
    def -(other: Vector2) : Vector2 =
        Vector2(x - other.x, y - other.y)

    /**@return Vector2 scaled by a Double*/
    def *(f :Double) : Vector2 =
        Vector2(x * f, y * f)
    
    /**@return Vector2 scaled by a Double
     * 
     * Made to mimic how vectors are writen in math
    */
    def *:(f :Double) : Vector2 =
        Vector2(x * f, y * f)
    
    /**@return Vector2 scaled by 1/Double*/
    def /(f :Double) : Vector2 =
        Vector2(x / f, y / f)
    
    /**Creates vector from point to point*/
    def -> (other: Vector2) : Vector2 =
        other - this
    
    /**The length of the vector*/
    lazy val length : Double = 
        math.sqrt(x*x + y*y)
    
    /**A vector in the same direction with lenght 1*/
    lazy val normalized : Vector2 =
        this / length


    