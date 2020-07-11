String text_input1b0 = "0";
String text_input1b1 = "0";
String text_input1b2 = "0";
String text_input1b3 = "0";
String text_input2b0 = "0";
String text_input2b1 = "0";
String text_input2b2 = "0";
String text_input2b3 = "0";

String text_output = "0000";

String decimal_input1 = "0";
String decimal_input2 = "0";
String decimal_output = "0";

ArrayList<BinaryCal> history;


class BinaryCal{
  
  String input1,input2,operator,output;
  BinaryCal(String _input1,String _input2,String _output,String _operator){
    input1 = _input1;
    input2 = _input2;
    output = _output;
    operator = _operator;
  }
  String ToString(){
    return input1 + operator + input2 + " = " + output;
  }
}

void setup(){
  background(255);
  size(400,600);
  history = new ArrayList<BinaryCal>();
}

void draw(){
  background(255);
  updateDraw();
  HistoryViewUpdate();
}

void updateDraw(){
  textSize(18);
  fill(255);
  rect(110,30,30,30);
  rect(150,30,30,30);
  rect(190,30,30,30);
  rect(230,30,30,30);
  
  rect(320,50,60,30);
  
  rect(110,70,30,30);
  rect(150,70,30,30);
  rect(190,70,30,30);
  rect(230,70,30,30);
  
  fill(0);
  
  text("Input1",30,50);
  text("Input2",30,90);
  text("Output",30,130);
  text("History",30,170);
  
  text(decimal_input1,280,50);
  text(decimal_input2,280,90);
  text(decimal_output,280,130);
  
  text("Add",333,73);
  
  text(text_input1b3, 120,50);
  text(text_input1b2, 160,50);
  text(text_input1b1, 200,50);
  text(text_input1b0, 240,50);
  
  text(text_input2b3, 120,90);
  text(text_input2b2, 160,90);
  text(text_input2b1, 200,90);
  text(text_input2b0, 240,90);
  
  text(text_output,120,130);
}

void mousePressed(){ 
  ToggleUpdate();
  Add();
  DecimalUpdate();
  
}
void ToggleUpdate(){
  if((mouseX >= 110 && mouseX <= 140)&&(mouseY >=30 && mouseY <= 60)){
    text_input1b3 = toggle(text_input1b3);
  }
   if((mouseX >= 150 && mouseX <= 180)&&(mouseY >=30 && mouseY <= 60)){
    text_input1b2 = toggle(text_input1b2);
  }
   if((mouseX >= 190 && mouseX <= 220)&&(mouseY >=30 && mouseY <= 60)){
    text_input1b1 = toggle(text_input1b1);
  }
   if((mouseX >= 230 && mouseX <= 260)&&(mouseY >=30 && mouseY <= 60)){
    text_input1b0 = toggle(text_input1b0);
  }
    if((mouseX >= 110 && mouseX <= 140)&&(mouseY >=70 && mouseY <= 100)){
    text_input2b3 = toggle(text_input2b3);
  }
   if((mouseX >= 150 && mouseX <= 180)&&(mouseY >=70 && mouseY <= 100)){
    text_input2b2 = toggle(text_input2b2);
  }
   if((mouseX >= 190 && mouseX <= 220)&&(mouseY >=70 && mouseY <= 100)){
    text_input2b1 = toggle(text_input2b1);
  }
   if((mouseX >= 230 && mouseX <= 260)&&(mouseY >=70 && mouseY <= 100)){
    text_input2b0 = toggle(text_input2b0);
  }
  
}

String toggle(String input){
  String result = "";
  if(input == "1"){
    result = "0";
  }
  else if (input=="0"){
    result = "1";
  }
  return result;
}

void DecimalUpdate(){
  decimal_input1 = str(int(text_input1b3)*8 + int(text_input1b2)*4 + int(text_input1b1)*2 +int(text_input1b0)*1);
  decimal_input2 = str(int(text_input2b3)*8 + int(text_input2b2)*4 + int(text_input2b1)*2 +int(text_input2b0)*1);
  
  decimal_output = str(unbinary(text_output));
}

void Add(){
  if((mouseX >= 320 && mouseX <= 380)&&(mouseY >=50 && mouseY <= 80)){
    
    String text_input2 = text_input2b3 + text_input2b2 + text_input2b1 + text_input2b0;
    String text_input1 = text_input1b3 + text_input1b2 + text_input1b1 + text_input1b0;
    
    String result = "";
    int carry = 0;
    
    for(int i = text_input1.length()-1; i >= 0; i--){
      int add_total = int(str(text_input1.charAt(i))) + int(str(text_input2.charAt(i))) + carry;
      //println(add_total);
      
      if (add_total == 0){
                result += "0";
                carry = 0;
            }
      else if(add_total == 1){
                result += "1";
                carry = 0;
            }
      else if(add_total == 2){
                result += "0";
                carry = 1;
            }
      else{
                result += "1";
                carry = 1;
            }
            
            //println(result);
    }
    
    if (carry == 1){
        result += "1";
      }
      
    //println(result);
    text_output = "";   
    for(int i =result.length()-1; i >= 0; i--){
      //println(i);
      text_output += result.charAt(i);
    }
    
    history.add(new BinaryCal(text_input1,text_input2,text_output, " + "));
    //println(history);
  }
}

void HistoryViewUpdate(){
  int pos_y = 200; 
  for(int i = history.size()-1; i >=0 ;i--){
    text(history.get(i).ToString(),30,pos_y);
    pos_y += 20;
    
    if(pos_y > 580){
      break;
    }
  }
  
}
