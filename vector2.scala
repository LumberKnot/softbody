package utilities

case class Vector2(x: Float, y: Float):

    def +(other: Vector2) : Vector2 =
        Vector2(x + other.x, y + other.y)
    
    def *(f :Float) : Vector2 =
        Vector2(x * f, y * f)

    