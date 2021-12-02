package softbody

@main
def run : Unit =
    val buttons = Seq("One", "Two", "Cancel")
    val selected =
    introprog.Dialog.select("Number of players?", buttons, "Snake")