package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*

class MainView : View("Rock Scissor Paper") {
    override val root = vbox(10)

    var text_player = SimpleStringProperty()
    var text_result = SimpleStringProperty()
    var text_choice = SimpleStringProperty()
    var player1choice =""
    var player2choice =""


    fun selectChoice(choice:String){
        if(text_player.value =="Player 1"&& text_result.value == "Unknown"){
            text_choice.value = choice
        }
        else if (text_player.value == "Player 2"&& text_result.value == "Unknown") {
            text_choice.value = choice
        }
    }

    fun checkResult(c1: String, c2: String) {
        if(c1 == c2){
            text_result.value = "Draw"
        }

        else if((c1 == "Rock" && c2 == "Scissor") ||(c1 == "Paper" && c2 == "Rock")||(c1 == "Scissor" && c2 == "Paper")){
            text_result.value = "Player 1 Win"
        }

        else{
            text_result.value = "Player 2 Win"
        }
    }

    init{
        text_player.value = "Player 1"
        text_result.value = "Unknown"
        text_choice.value = "Please Select Your choice"

        with(root){
            style{
                padding = box(30.px)
            }

            text(text_player){
                style{
                    fontSize = 30.px
                    fontWeight = FontWeight.BOLD
                }
            }
            text(text_choice){
                style{
                    fontSize = 24.px
                    fontWeight = FontWeight.MEDIUM
                }
            }
            button("Rock"){
                useMaxWidth = true
                style{
                    fontSize = 24.px
                }
                action{
                    selectChoice("Rock")
                }
            }
            button("Scissor"){
                useMaxWidth = true
                style{
                    fontSize = 24.px
                }
                action{
                    selectChoice("Scissor")
                }
            }
            button("Paper"){
                useMaxWidth = true
                style{
                    fontSize = 24.px
                }
                action{
                    selectChoice("Paper")
                }
            }
            button("Submit"){
                useMaxWidth = true
                vboxConstraints {
                    marginTop = 50.0
                }
                style{
                    fontSize = 24.px
                }
                action{
                    if(text_player.value =="Player 1" && text_choice.value != "Please Select Your choice" ){
                        player1choice = text_choice.value as String
                        text_player.value = "Player 2"
                        text_choice.value = "Please Select Your choice"
                    }
                    else if (text_player.value == "Player 2" && text_choice.value != "Please Select Your choice" && text_result.value == "Unknown") {
                        player2choice = text_choice.value as String
                        checkResult(player1choice,player2choice)
                    }
                }
            }
            text("Player 1 vs Player 2"){
                style{
                    fontSize = 30.px
                    fontWeight = FontWeight.BOLD
                }
            }
            hbox{
                text("Result: "){
                    style{
                        fontSize = 24.px
                    }

                }
                text(text_result){
                    style{
                        fontSize = 24.px
                    }
                }
            }

            button("Restart"){
                useMaxWidth = true
                style{
                    fontSize = 24.px
                }
                action{
                    text_choice.value = "Please Select Your choice"
                    text_player.value = "Player 1"
                    text_result.value = "Unknown"
                    player1choice =""
                    player2choice =""
                }
            }
        }
    }


}