package com.example.rockscissorpaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var player1choice:String
    lateinit var player2choice:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_rock.setOnClickListener {
            selectChoice("Rock")
        }
        button_scissor.setOnClickListener {
            selectChoice("Scissor")
        }
        button_paper.setOnClickListener {
            selectChoice("Paper")
        }

        button_submit.setOnClickListener{
            if(player_id.text =="Player 1" && text_choice.text != "Please Select Your choice" ){
                player1choice = text_choice.text as String
                player_id.text = "Player 2"
                text_choice.text = "Please Select Your choice"
            }
            else if (player_id.text == "Player 2" && text_choice.text != "Please Select Your choice" && text_result.text == "Unknown") {
                player2choice = text_choice.text as String
                checkResult(player1choice,player2choice)
            }
        }

        button_restart.setOnClickListener {
            text_choice.text = "Please Select Your choice"
            player_id.text = "Player 1"
            text_result.text = "Unknown"
            player1choice =""
            player2choice =""
        }
    }

    fun selectChoice(choice:String){
        if(player_id.text =="Player 1"){
            text_choice.text = choice
        }
        else if (player_id.text == "Player 2") {
            text_choice.text = choice
        }
    }

    fun checkResult(c1:String,c2:String){
        if(c1 == c2){
            text_result.text = "Draw"
        }

        else if((c1 == "Rock" && c2 == "Scissor") ||(c1 == "Paper" && c2 == "Rock")||(c1 == "Scissor" && c2 == "Paper")){
            text_result.text = "Player 1 Win"
        }

        else{
            text_result.text = "Player 2 Win"
        }
    }


}
