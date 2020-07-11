package com.example.demo.view

import com.example.demo.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.BorderPane
import javafx.scene.text.FontWeight
import tornadofx.*

class MainView : View() {

    override val root = BorderPane()

    val history = mutableListOf<BinaryCal>().observable()

    val controller:MyController by inject()
    val text_input1b0 = SimpleStringProperty()
    val text_input1b1 = SimpleStringProperty()
    val text_input1b2 = SimpleStringProperty()
    val text_input1b3 = SimpleStringProperty()
    val text_input2b0 = SimpleStringProperty()
    val text_input2b1 = SimpleStringProperty()
    val text_input2b2 = SimpleStringProperty()
    val text_input2b3 = SimpleStringProperty()
    val decimal_input1 = SimpleStringProperty()
    val decimal_input2 = SimpleStringProperty()

    val text_output = SimpleStringProperty()
    val decimal_output = SimpleStringProperty()

    init{
        text_input1b0.value = "0"
        text_input1b1.value = "0"
        text_input1b2.value = "0"
        text_input1b3.value = "0"
        text_input2b0.value = "0"
        text_input2b1.value = "0"
        text_input2b2.value = "0"
        text_input2b3.value = "0"
        text_output.value = "0000"
        decimal_input1.value = "0"
        decimal_input2.value = "0"
        decimal_output.value = "0"

        with(root){


            left{
                style {
                    padding = box(30.px)
                }
                vbox(10) {
                    hbox(10) {
                        text("Input1"){
                            style{
                                fontSize = 18.px
                                fontWeight = FontWeight.BOLD
                            }
                        }
                        button(text_input1b3){
                            action{
                                text_input1b3.value = controller.toggle(text_input1b3.value)
                                controller.InputToDecimal()
                            }
                        }
                        button(text_input1b2){
                            action{
                                text_input1b2.value = controller.toggle(text_input1b2.value)
                                controller.InputToDecimal()
                            }

                        }
                        button(text_input1b1){
                            action{
                                text_input1b1.value = controller.toggle(text_input1b1.value)
                                controller.InputToDecimal()
                            }
                        }
                        button(text_input1b0){
                            action{
                                text_input1b0.value = controller.toggle(text_input1b0.value)
                                controller.InputToDecimal()
                            }
                        }
                        text(decimal_input1){
                            style{
                                fontSize = 18.px
                            }
                        }

                    }
                    hbox(10){
                        text("Input2"){
                            style{
                                fontSize = 18.px
                                fontWeight = FontWeight.BOLD
                            }
                        }
                        button(text_input2b3){
                            action{
                                text_input2b3.value = controller.toggle(text_input2b3.value)
                                controller.InputToDecimal()
                            }
                        }
                        button(text_input2b2){
                            action{
                                text_input2b2.value = controller.toggle(text_input2b2.value)
                                controller.InputToDecimal()
                            }
                        }
                        button(text_input2b1){
                            action{
                                text_input2b1.value = controller.toggle(text_input2b1.value)
                                controller.InputToDecimal()
                            }
                        }
                        button(text_input2b0){
                            action{
                                text_input2b0.value = controller.toggle(text_input2b0.value)
                                controller.InputToDecimal()
                            }
                        }
                        text(decimal_input2){
                            style{
                                fontSize = 18.px
                            }
                        }
                    }
                    button("Add") {
                        action {

                            val input1 = text_input1b3.value + text_input1b2.value + text_input1b1.value + text_input1b0.value
                            val input2 = text_input2b3.value + text_input2b2.value + text_input2b1.value + text_input2b0.value

                            println("$input1 $input2")
                            text_output.value = controller.Add(input1, input2)
                            decimal_output.value = controller.changeToDecimal(text_output.value)

                            history += BinaryCal(input1,input2,text_output.value," + ")
                        }
                    }
                    hbox(10) {
                        text("Output"){
                            style{
                                fontSize = 18.px
                                fontWeight = FontWeight.BOLD
                            }
                        }
                        text(text_output) {

                            style {
                                fontSize = 18.px
                            }
                        }

                        text(decimal_output){
                            style{
                                fontSize = 18.px
                            }
                        }

                    }
                }
            }
            center{
                listview(history){
                    style{
                        padding = box(20.px)
                    }
                    cellCache {
                        label("${it.showString()} ")
                    }
                }
            }
        }
    }
}

class MyController : Controller(){

    val mainview : MainView by inject()
    fun toggle(num:String):String{
        var result = ""
        if(num == "0"){
            result = "1"
        }
        else if (num == "1"){
            result = "0"
        }
        return result
    }

    fun Add(_input1:String,_input2:String):String{
        var result = ""
        val length = _input1.length - 1
        var extra_number = 0
        for (index in length downTo 0){
            val add_total = _input1[index].toString().toInt() + _input2[index].toString().toInt() + extra_number
            if (add_total == 0){
                result += "0"
                extra_number =0
            }
            else if(add_total == 1){
                result += "1"
                extra_number = 0
            }
            else if(add_total == 2){
                result += "0"
                extra_number =1
            }
            else{
                result += "1"
                extra_number = 1
            }
        }
        if (extra_number == 1){
            result += 1
        }
        return result.reversed()
    }

    fun changeToDecimal(_input:String):String{

        var digit = _input.length - 1
        var total = 0

        for (num in _input){
            val number = num.toString().toInt()
            total += number * (Math.pow(2.0,digit.toDouble())).toInt()
            digit -= 1
        }

        return total.toString()
    }

    fun InputToDecimal(){
        val input1 = mainview.text_input1b3.value + mainview.text_input1b2.value + mainview.text_input1b1.value + mainview.text_input1b0.value
        val input2 = mainview.text_input2b3.value + mainview.text_input2b2.value + mainview.text_input2b1.value + mainview.text_input2b0.value

        mainview.decimal_input1.value = changeToDecimal(input1)
        mainview.decimal_input2.value = changeToDecimal(input2)
    }
}

class BinaryCal(_input1:String,_input2:String,_output:String,_operator:String){
    val input1 = _input1
    val input2 = _input2
    val output = _output
    val operator = _operator
    init{
        println("$_input1 $_operator $_input2 = $_output")
    }
    fun showString():String{
        val result = input1 + operator + input2 + " = " + output
        return result
    }
}