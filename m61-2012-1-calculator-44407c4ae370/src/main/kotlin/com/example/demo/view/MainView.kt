package com.example.demo.view
import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainView : View() {

    val controller: MyController by inject()
    val input1 = SimpleStringProperty()
    val input2 = SimpleStringProperty()
    val input3 = SimpleStringProperty()
    val output = SimpleStringProperty()

    override val root = BorderPane()

    init {
        with(root) {
            left {
                form{
                    fieldset{
                        field("Input1") {
                            textfield(input1)
                        }
                        field("Input2"){
                            textfield(input2)
                        }
                        field("Input3"){
                            textfield(input3)
                        }
                        field("Output"){
                            textfield(output){
                                isEditable = false
                            }
                        }
                        button("Add") {
                            useMaxWidth = true
                            action {
                                val result = controller.Add(input1.value, input2.value, input3.value)
                                output.value = result
                            }
                        }
                    }
                }
            }
        }
    }
}

class MyController : Controller(){
    fun Add(_input1:String,_input2:String,_input3:String):String {
        val total = _input1.toFloat() + _input2.toFloat() + _input3.toFloat()
        return total.toString()
    }
}

