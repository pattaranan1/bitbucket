package com.example.demo.view

import javafx.scene.paint.Color
import tornadofx.*
import java.util.concurrent.ThreadLocalRandom
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

import kotlin.math.abs

var listofballs = mutableListOf<LayerBall>()

class MainView : View("BallClicker") {
    override val root = anchorpane()

    init{
        listofballs = load()
        with(root){
            pane{
                prefHeight = 600.0
                prefWidth = 800.0

                for(obj in listofballs) {
                    circle {
                        fill = c(obj.color1, obj.color2, obj.color3)
                        centerX = obj.x
                        centerY = obj.y
                        radius = obj.rd
                    }
                }
                setOnMousePressed{

                    if(it.isPrimaryButtonDown) {
                        val x = it.x
                        val y = it.y
                        val rd = ThreadLocalRandom.current().nextDouble(30.0, 100.0)
                        val color1 = ThreadLocalRandom.current().nextInt(255)
                        val color2 = ThreadLocalRandom.current().nextInt(255)
                        val color3 = ThreadLocalRandom.current().nextInt(255)


                        circle {
                            fill = c(color1, color2, color3)
                            centerX = x
                            centerY = y
                            radius = rd
                        }

                        listofballs.add(LayerBall(x, y, rd, color1, color2, color3))
                        save()
                    }

                    if(it.isSecondaryButtonDown){
                        for(i in listofballs.size-1 downTo 0){
                            if(listofballs[i].checkRadius(it.x,it.y)){
                                listofballs.removeAt(i)
                                break
                            }
                        }
                        rectangle {
                            fill = Color.WHITE
                            width = 850.0
                            height = 650.0
                        }

                        for(obj in listofballs){
                            circle {
                                fill = c(obj.color1, obj.color2, obj.color3)
                                centerX = obj.x
                                centerY = obj.y
                                radius = obj.rd
                            }
                        }
                        save()
                    }
                }
            }
        }
    }
}

val gson = GsonBuilder().setPrettyPrinting().create()
fun save(){
    val file = File("C:\\BallClicker\\src\\main\\resources\\listofball")
    val jsonlist:String = gson.toJson(listofballs)
    file.writeText(jsonlist)
    }

fun load():MutableList<LayerBall>{
    val file = File("C:\\BallClicker\\src\\main\\resources\\listofball")
    val jsonList = file.bufferedReader().use { it.readText() }
    val dataList:MutableList<LayerBall> = gson.fromJson(jsonList,object : TypeToken<MutableList<LayerBall>>(){}.type)
    return dataList
}

class LayerBall(_x:Double,_y:Double,_rd:Double,_color1:Int,_color2:Int,_color3:Int){

    val x = _x
    val y = _y
    val rd = _rd
    val color1 = _color1
    val color2 = _color2
    val color3 = _color3

    fun checkRadius(nx:Double,ny:Double):Boolean{
        return (abs(nx - x)<= rd) && (abs(ny - y)<= rd)
    }
}

