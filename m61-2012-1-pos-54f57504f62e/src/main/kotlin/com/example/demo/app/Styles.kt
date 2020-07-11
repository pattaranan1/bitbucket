package com.example.demo.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val greenbutton by cssclass()
        val entergreenbutton by cssclass()
        val clearbutton by cssclass()
        val enterclearbutton by cssclass()
        val numberCal by cssclass()
        val enternumberCal by cssclass()
        val status by cssclass()
    }

    init {
        greenbutton{
            fontSize = 16.px
            fontWeight = FontWeight.SEMI_BOLD
            textFill = Color.WHITE
            backgroundColor += c("#26B86E")
            padding = box(10.px,0.px)
        }
        entergreenbutton{
            fontSize = 16.px
            fontWeight = FontWeight.SEMI_BOLD
            textFill = Color.BLACK
            backgroundColor += c("#26B86E")
            padding = box(10.px,0.px)

        }
        clearbutton{

            padding = box(10.px)
            fontSize = 16.px
            fontWeight = FontWeight.SEMI_BOLD
            textFill = Color.RED
            backgroundColor += Color.WHITE
            borderColor += box(Color.LIGHTGRAY)
        }
        enterclearbutton{
            padding = box(10.px)
            fontSize = 16.px
            fontWeight = FontWeight.SEMI_BOLD
            textFill = Color.GRAY
            backgroundColor += Color.WHITE
            borderColor += box(Color.LIGHTGRAY)
            opacity = 0.5
        }
        numberCal{
            fontSize = 22.px
            fontWeight = FontWeight.SEMI_BOLD
            textFill = Color.WHITE
            backgroundColor += c("#979797")
            borderColor += box(Color.WHITE)
        }
        enternumberCal{
            textFill = c("#717171")
            fontSize = 22.px
            fontWeight = FontWeight.SEMI_BOLD
            backgroundColor += c("#979797")
            borderColor += box(Color.WHITE)
            opacity = 0.5
        }
        status{
            textFill = Color.RED
            fontSize = 16.px
            fontWeight = FontWeight.SEMI_BOLD
        }
    }
}