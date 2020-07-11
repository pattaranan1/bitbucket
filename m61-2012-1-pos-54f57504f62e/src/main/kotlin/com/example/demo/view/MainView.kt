package com.example.demo.view
import com.example.demo.app.Styles
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import javafx.stage.StageStyle
import tornadofx.*
import kotlin.math.round


fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

class Model: ViewModel(){

    var bakerylist = mutableListOf<Menu>(Menu("Cupcake", 12.0, "cupcake-2.png")
            , Menu("Ice cream", 8.5, "ice-cream-13.png")
            , Menu("Doughnut", 4.0, "doughnut-1.png")
            ,Menu("Pancake",9.5,"pancakes.png")
            ,Menu("Pretzel",6.5,"pretzel.png")
            ,Menu("Pudding",14.0,"pudding.png")).observable()

    var beveragelist = mutableListOf<Menu>(Menu("Espresso",10.0,"Espresso.png")
            ,Menu("Hot Tea",20.0,"HotTea.png")
            ,Menu("Iced Chocolate",25.0,"IcedChocolate.png")).observable()
    var cart = mutableListOf<Menu>().observable()

    val subtotal = SimpleDoubleProperty()
    val totalprice = SimpleDoubleProperty()
    val change = SimpleDoubleProperty()
    val tax = SimpleDoubleProperty()
    var cash = SimpleStringProperty()
    val net = SimpleDoubleProperty()
    val status = SimpleStringProperty()
    val discountlist = listOf<String>("-","5%","10%","20%").observable()
    val discount = SimpleDoubleProperty()
    val selecteddiscount = SimpleStringProperty()
    val selectedmenu = SimpleObjectProperty<Menu>()
    val selectedcart = SimpleObjectProperty<Menu>()
}

class MainView : View("Cashier") {

    val model: Model by inject()
    val controller: MyController by inject()



    override val root = borderpane()

    init {
        with(root) {
            prefWidth = 650.0
            prefHeight = 650.0
            left {
                style {
                    padding = box(10.px)
                }
                form {
                    style {
                        paddingTop = 20
                    }
                    fieldset("Menu") {
                        field {
                            vbox {
                                drawer{
                                    item("Bakery"){
                                        add<Bakery>()
                                    }
                                    item("Beverage"){
                                        add<Beverage>()
                                    }
                                }
                            }
                        }
                        field("Discount"){
                            style {
                                fontSize = 16.px
                                fontWeight = FontWeight.BOLD
                            }
                            combobox(model.selecteddiscount, model.discountlist){
                                model.selecteddiscount.onChange {
                                    controller.updatesubtotal()
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                    }
                }
            }
            center {
                vbox(10.0){
                    style {
                        padding = box(10.px)
                        paddingLeft = 10.0
                    }
                    vbox{
                        style {
                            backgroundColor += Color.WHITE
                            borderColor += box(Color.LIGHTGREY)
                        }
                        form{
                            fieldset{
                                field("Sub Total"){
                                    style{
                                        fontSize = 16.px
                                        fontWeight = FontWeight.BOLD
                                    }
                                    hbox {
                                        alignment = Pos.BASELINE_RIGHT
                                        label(model.subtotal) {
                                            hboxConstraints {
                                                hGrow = Priority.ALWAYS
                                            }
                                            style {
                                                fontSize = 16.px
                                                fontWeight = FontWeight.SEMI_BOLD
                                            }
                                        }
                                    }
                                }
                                field("Billing Discount"){
                                    style{
                                        fontSize = 16.px
                                        textFill = Color.RED
                                        fontWeight = FontWeight.BOLD
                                    }
                                    hbox{
                                        alignment = Pos.BASELINE_RIGHT
                                        label(model.discount) {
                                            hboxConstraints {
                                                hGrow = Priority.ALWAYS
                                            }
                                            style {
                                                textFill = Color.RED
                                                fontSize = 16.px
                                                fontWeight = FontWeight.SEMI_BOLD
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        hbox {
                            alignment = Pos.BASELINE_RIGHT
                            label(model.totalprice) {
                                hboxConstraints {
                                    marginRight = 10.0
                                    hGrow = Priority.ALWAYS
                                }
                                textAlignment = TextAlignment.RIGHT
                                style {
                                    fontSize = 40.px
                                    fontWeight = FontWeight.SEMI_BOLD

                                }
                            }
                        }
                        button("Pay") {
                            useMaxWidth = true
                            addClass(Styles.greenbutton)
                            setOnMouseEntered {
                                removeClass(Styles.greenbutton)
                                addClass(Styles.entergreenbutton)
                            }
                            setOnMouseExited {
                                removeClass(Styles.entergreenbutton)
                                addClass(Styles.greenbutton)
                            }
                            action {
                                model.change.value = 0.0
                                model.cash.value = "0"
                                model.status.value = ""
                                model.tax.value = (0.07 * model.totalprice.value).round(2)
                                model.net.value = (model.totalprice.value + model.tax.value).round(2)
                                find<Payment>().openModal(stageStyle = StageStyle.UTILITY)
                            }
                        }
                    }
                    listview(model.cart) {
                        bindSelected(model.selectedcart)
                        onUserSelect() {
                            val obj = model.selectedcart.get()
                            model.cart.remove(obj)
                            controller.updatesubtotal()
                            obj.quantity -= obj.quantity
                        }
                        onUserSelect(1) {
                            if (it.quantity.value > 1) {
                                it.quantity.value -= 1
                                controller.updatesubtotal()
                            } else {
                                it.quantity.value -= 1
                                model.cart.remove(it)
                                controller.updatesubtotal()
                            }
                        }
                        cellCache {
                            hbox {
                                imageview(it.pic) {
                                    fitHeight = 50.0
                                    fitWidth = 50.0
                                                }
                                label(" ${it.name}\n" +
                                        " ${it.price}") {
                                    style {
                                        fontSize = 14.px
                                        fontWeight = FontWeight.SEMI_BOLD
                                    }
                                }
                                hbox(5) {
                                    alignment = Pos.BASELINE_RIGHT
                                    hgrow = Priority.ALWAYS
                                    button("+") {
                                        hboxConstraints {
                                            marginLeft = 5.0
                                            hGrow = Priority.ALWAYS
                                        }
                                        action {
                                            it.quantity.value += 1
                                            controller.updatesubtotal()
                                        }
                                    }
                                    textfield(it.quantity) {
                                        setMaxWidth(40.0)
                                        alignment = Pos.BASELINE_CENTER
                                        textProperty().addListener { _, _, _ ->
                                            controller.updatesubtotal()
                                        }
                                    }
                                    button("-") {
                                        action {
                                            if (it.quantity.value > 1) {
                                                it.quantity.value -= 1
                                                controller.updatesubtotal()
                                            } else {
                                                it.quantity.value -= 1
                                                model.cart.remove(it)
                                                controller.updatesubtotal()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    button("Clear All") {
                        imageview("delete-button.png"){
                            fitWidth = 20.0
                            fitHeight = 25.0
                        }
                        useMaxWidth = true
                        addClass(Styles.clearbutton)
                        setOnMouseEntered {
                            removeClass(Styles.clearbutton)
                            addClass(Styles.enterclearbutton)
                            imageview("gray-delete-button.png"){
                                fitWidth = 20.0
                                fitHeight = 25.0
                            }
                        }
                        setOnMouseExited {
                            removeClass(Styles.enterclearbutton)
                            addClass(Styles.clearbutton)
                            imageview("delete-button.png"){
                                fitWidth = 20.0
                                fitHeight = 25.0
                            }
                        }
                        action {
                            for (obj in model.cart) {
                                obj.quantity -= obj.quantity
                            }
                            model.cart.clear()
                            controller.updatesubtotal()
                        }
                    }
                }
            }
        }
    }
}

class Bakery: View(){

    val model : Model by inject()
    val controller: MyController by inject()

    override val root = listview(model.bakerylist) {

        bindSelected(model.selectedmenu)

        cellCache {
            imageview(it.pic) {
                fitHeight = 50.0
                fitWidth = 50.0
            }
        }
        cellFormat {
            text = " ${it.name} \n ${it.price}"
            style {
                fontSize = 16.px
                fontWeight = FontWeight.SEMI_BOLD
            }
        }
        onUserSelect(1) {
            if (model.selectedmenu.get() !in model.cart) {
                model.selectedmenu.get().quantity += 1
                model.cart.add(model.selectedmenu.get())
                controller.updatesubtotal()
            } else {
                model.selectedmenu.get().quantity += 1
                controller.updatesubtotal()
            }
            selectionModel.clearSelection()
        }
    }
}


class Beverage: View(){

    val model : Model by inject()
    val controller: MyController by inject()

    override val root = listview(model.beveragelist) {
        bindSelected(model.selectedmenu)
        cellCache {
            imageview(it.pic) {
                fitHeight = 50.0
                fitWidth = 50.0
            }
        }
        cellFormat {
            text = " ${it.name} \n ${it.price}"
            style {
                fontSize = 16.px
                fontWeight = FontWeight.SEMI_BOLD
            }
        }
        onUserSelect(1) {
            if (model.selectedmenu.get() !in model.cart) {
                model.selectedmenu.get().quantity += 1
                model.cart.add(model.selectedmenu.get())
                controller.updatesubtotal()
            } else {
                model.selectedmenu.get().quantity += 1
                controller.updatesubtotal()
            }
            selectionModel.clearSelection()
        }
    }
}



    class MyController : Controller() {

        val model : Model by inject()

        fun updatesubtotal(){

            model.subtotal.value = 0.0
            model.discount.value = 0.0

            for (obj in model.cart) {

                model.subtotal.value += (obj.price * obj.quantity.get())
            }

            if (model.selecteddiscount.value == "5%"){
                model.discount.value = (-0.05 * model.subtotal.value).round(2)
                model.totalprice.value = model.subtotal.value + model.discount.value
            }
            else if(model.selecteddiscount.value == "10%"){
                model.discount.value = (-0.1 * model.subtotal.value).round(2)
                model.totalprice.value = model.subtotal.value + model.discount.value
            }
            else if(model.selecteddiscount.value == "20%"){
                model.discount.value = (-0.2 * model.subtotal.value).round(2)
                model.totalprice.value = model.subtotal.value + model.discount.value
            }
            else{
                model.totalprice.value = model.subtotal.value
            }
        }

        fun checkZeroCash(cash: String, num: String): String {
            if (cash == "0" || cash == "0.0") {
                return num
            } else {
                return cash + num
            }
        }

    }

    class Menu(_name: String, _price: Double, _pic: String) {

        val nameProperty = SimpleStringProperty(_name)
        var name by nameProperty
        val priceProperty = SimpleDoubleProperty(_price)
        var price by priceProperty
        val picProperty = SimpleStringProperty(_pic)
        val pic by picProperty

        val quantity = SimpleIntegerProperty()


        override fun toString(): String = "$name : $price"

    }

    class Payment : View("Payment") {


        val model: Model by inject()
        val controller: MyController by inject()

        override val root = borderpane()

        init {

            with(root) {
                prefWidth = 575.0
                left {
                    vbox {
                        style {
                            padding = box(10.px)
                        }
                        hbox {
                            button("Balance"){
                                setPrefWidth(151.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = model.net.value.toString()
                                }

                            }
                            textfield(model.cash) {
                                setPrefWidth(150.0)
                                style {
                                    fontSize = 22.px
                                }
                            }
                        }
                        hbox {
                            button("Clear") {
                                setPrefWidth(101.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = "0"
                                }
                            }
                            button("Del") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = model.cash.value.substring(0..(model.cash.value.length - 2))

                                }
                            }
                            button("1,000") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = (model.cash.value.toDouble() + 1000).toString()

                                }
                            }
                        }
                        hbox {
                            button("7") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "7")

                                }
                            }
                            button("8") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "8")

                                }
                            }
                            button("9") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "9")

                                }
                            }
                            button("500") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = (model.cash.value.toDouble() + 500).toString()

                                }
                            }
                        }
                        hbox {
                            button("4") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "4")

                                }
                            }
                            button("5") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "5")

                                }
                            }
                            button("6") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "6")

                                }
                            }
                            button("100") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = (model.cash.value.toDouble() + 100).toString()

                                }
                            }
                        }
                        hbox {
                            button("1") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "1")

                                }
                            }
                            button("2") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "2")

                                }
                            }
                            button("3") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value = controller.checkZeroCash(model.cash.value, "3")

                                }
                            }
                            button("50") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = (model.cash.value.toDouble() + 50).toString()

                                }
                            }
                        }
                        hbox {
                            button("0") {
                                setPrefWidth(133.33)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    model.cash.value += "0"

                                }
                            }
                            button(".") {
                                setPrefWidth(66.67)
                                style {
                                    fontSize = 22.px
                                }
                                action {
                                    if ("." !in model.cash.value) {
                                        model.cash.value += "."
                                    }
                                }
                            }
                            button("20") {
                                setPrefWidth(100.0)
                                addClass(Styles.numberCal)
                                setOnMouseEntered {
                                    removeClass(Styles.numberCal)
                                    addClass(Styles.enternumberCal)
                                }
                                setOnMouseExited {
                                    removeClass(Styles.enternumberCal)
                                    addClass(Styles.numberCal)
                                }
                                action {
                                    model.cash.value = (model.cash.value.toDouble() + 20.0).toString()

                                }
                            }
                        }
                    }
                }
                center {
                    vbox(10) {
                        style {
                            padding = box(10.px)
                        }
                        hbox {
                            label("Total") {
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                            label(model.totalprice) {
                                hboxConstraints {
                                    marginLeft = 77.0
                                    hgrow = Priority.ALWAYS
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                        hbox {
                            label("Tax") {
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                            label(model.tax) {
                                hboxConstraints {
                                    marginLeft = 90.0
                                    hgrow = Priority.ALWAYS
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                        hbox {
                            label("Net Total") {
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                            label(model.net) {
                                hboxConstraints {
                                    marginLeft = 40.0
                                    hgrow = Priority.ALWAYS
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                        hbox {
                            imageview("cash.png")
                            label(" Cash") {
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                            label(model.cash) {
                                hboxConstraints {
                                    marginLeft = 42.0
                                    hgrow = Priority.ALWAYS
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                        button("Payment Confirm") {
                            useMaxWidth = true
                            addClass(Styles.greenbutton)
                            setOnMouseEntered {
                                removeClass(Styles.greenbutton)
                                addClass(Styles.entergreenbutton)
                            }
                            setOnMouseExited {
                                removeClass(Styles.entergreenbutton)
                                addClass(Styles.greenbutton)
                            }
                            action {

                                if (model.cash.value.toDouble() >= model.net.value) {

                                    model.change.value = (model.cash.value.toDouble() - model.net.value).round(2)

                                    for (obj in model.cart) {
                                        obj.quantity -= obj.quantity
                                    }

                                    model.cart.clear()
                                    controller.updatesubtotal()

                                    model.status.value = ""
                                    model.net.value = 0.0
                                    model.tax.value = 0.0
                                    model.cash.value = "0"
                                }
                                else{
                                    model.status.value = "Your cash is not enough"
                                }
                            }
                        }
                        hbox(5) {
                            label("Change") {
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                            label(model.change) {
                                hboxConstraints {
                                    marginLeft = 50.0
                                    hgrow = Priority.ALWAYS
                                }
                                style {
                                    fontSize = 16.px
                                    fontWeight = FontWeight.SEMI_BOLD
                                }
                            }
                        }
                        label(model.status){
                            alignment = Pos.BOTTOM_CENTER
                            addClass(Styles.status)
                        }

                        button("Exit"){
                            useMaxWidth = true
                            vboxConstraints{
                                marginBottom = 10.0
                                vgrow = Priority.ALWAYS
                            }
                            addClass(Styles.clearbutton)
                            setOnMouseEntered {
                                removeClass(Styles.clearbutton)
                                addClass(Styles.enterclearbutton)
                            }
                            setOnMouseExited {
                                removeClass(Styles.enterclearbutton)
                                addClass(Styles.clearbutton)
                            }
                            action{
                                close()
                            }
                        }
                    }
                }
            }
        }
    }




