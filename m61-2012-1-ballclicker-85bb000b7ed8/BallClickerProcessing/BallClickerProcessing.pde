ArrayList<LayerCircle> layer_circle;
JSONObject jsonobj;
JSONArray jsonlist;
PGraphics pg;

class LayerCircle{
  PGraphics pg;
  int x,y;
  float ext,rd,color1,color2,color3;
  
  LayerCircle(int _x,int _y,float _ext,float _color1,float _color2,float _color3){
    x = _x;
    y = _y;
    ext = _ext;
    rd = _ext/2;
    color1 = _color1;
    color2 = _color2;
    color3 = _color3;
    
    pg = createGraphics(width,height);
    pg.beginDraw();
    pg.noStroke();
    pg.fill(color1,color2,color3);
    pg.circle(x,y,ext);
    pg.endDraw();
    //image(pg,0,0);
  }
  Boolean checkRadius(int new_x,int new_y){
    return ((abs(new_x-x) <= rd)&&(abs(new_y-y) <= rd));
  }
  void clearLayer(){
    pg.beginDraw();
    pg.clear();
    pg.endDraw();
  }
}

void setup(){
  size(800,600);
  smooth();
  background(255);
  layer_circle = new ArrayList<LayerCircle>();
  try{
    load();
  }
  catch (Exception e){
    println("There isn't any json file");
  }
}

void draw(){
  refresh();
}

void refresh(){
    background(255);
    for(int i = 0; i < layer_circle.size(); i = i + 1){
      pg = layer_circle.get(i).pg;
      image(pg,0,0);
    }
}

void mousePressed(){
  if(mouseButton == LEFT){
    layer_circle.add(new LayerCircle(mouseX,mouseY,random(30,width/5),random(255),random(255),random(255)));
    saved();
  }
  if(mouseButton == RIGHT && layer_circle.size() > 0){
    for(int i = layer_circle.size()-1; i >= 0; i = i - 1){
      if(layer_circle.get(i).checkRadius(mouseX,mouseY)){
        layer_circle.get(i).clearLayer();
        layer_circle.remove(i);
        saved();
        break;
      }
    }
  }
}

void saved(){
  jsonlist = new JSONArray();
    
    for (int i = 0; i < layer_circle.size(); i++){
      
      jsonobj = new JSONObject();
      jsonobj.setInt("x",layer_circle.get(i).x);
      jsonobj.setInt("y",layer_circle.get(i).y);
      jsonobj.setFloat("ext",layer_circle.get(i).ext);
      jsonobj.setFloat("color1", layer_circle.get(i).color1);
      jsonobj.setFloat("color2", layer_circle.get(i).color2);
      jsonobj.setFloat("color3", layer_circle.get(i).color3);
      jsonlist.setJSONObject(i,jsonobj);
    }
   saveJSONArray(jsonlist, "data/circle.json");
}

void load(){
  jsonlist = loadJSONArray( "data/circle.json");
  
  for (int i =0; i < jsonlist.size(); i++){
    
    jsonobj = jsonlist.getJSONObject(i);
    int x = jsonobj.getInt("x");
    int y = jsonobj.getInt("y");
    float ext = jsonobj.getFloat("ext");
    float color1 = jsonobj.getFloat("color1");
    float color2 = jsonobj.getFloat("color2");
    float color3 = jsonobj.getFloat("color3");
    
    layer_circle.add(new LayerCircle(x,y,ext,color1,color2,color3));
  }
}
