package com.example.demo.view
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainView : View() {

    var history = listOf<BinaryCal>()
    val controller: MyController by inject()
    val input1 = SimpleStringProperty()
    val input2 = SimpleStringProperty()
    val decimal_input1 = SimpleStringProperty()
    val decimal_input2 = SimpleStringProperty()
    val binary_output = SimpleStringProperty()
    val decimal_output = SimpleStringProperty()
    var history_output = FXCollections.observableArrayList<String>()

    override val root = BorderPane()

    init {
        with(root) {
            left {
                form{
                    fieldset {
                        field("Input1") {
                            textfield(input1)
                            textfield(decimal_input1) {
                                isEditable = false
                            }
                        }
                        field("Input2") {
                            textfield(input2)
                            textfield(decimal_input2) {
                                isEditable = false
                            }
                        }
                        field("Output") {
                            textfield(binary_output) {
                                isEditable = false
                            }
                            textfield(decimal_output) {
                                isEditable = false
                            }
                        }
                        button("Add") {
                            useMaxWidth = true
                            action {
                                decimal_input1.value = controller.changeToDecimal((input1.value))
                                decimal_input2.value = controller.changeToDecimal((input2.value))
                                binary_output.value = controller.Add(input1.value, input2.value)
                                decimal_output.value = controller.changeToDecimal((binary_output.value))
                                history += BinaryCal(input1.value, input2.value, binary_output.value, "+")
                            }
                        }
                    }
                }
            }
            right{
                form{
                    fieldset{
                        field{
                            vbox{
                                listview(values = history_output)
                            }
                        }
                        button("Show history"){
                            useMaxWidth = true
                            action{
                                history_output.clear()
                                val resultlist = FXCollections.observableArrayList<String>()
                                for (obj in history) resultlist.add(obj.showString())
                                history_output.addAll(resultlist)
                            }
                        }
                    }
                }
            }
        }
    }
}

class MyController : Controller(){
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
        val result = input1 + operator + input2 + "=" + output
        return result
    }
}

