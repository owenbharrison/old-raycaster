package raycaster;

import processing.core.*;
import java.util.ArrayList;

public class Main extends PApplet{
	float rot = 0;
	float rs = PI/256;
	float rv = 0;
	float speed = 0.4f;
	float fov = PI/3;//field of view
	PVector pos = new PVector(200, 150);
	PVector vel = new PVector(0, 0);
	public boolean wDown = false,
			           sDown = false,
			           aDown = false,
			           dDown = false;
	public ArrayList<PVector> path;
	
	public static void main(String[] args) {
		PApplet.main(new String[] {"raycaster.Main"});
	}
	
	public void setup() {
		path = new ArrayList<PVector>();
	}
	
	public void keyPressed() {
		if(key=='w')wDown = true;
		if(key=='s')sDown = true;
		if(key=='a')aDown = true;
		if(key=='d')dDown = true;
	}
	
	public void mousePressed() {
		path.add(new PVector(mouseX, mouseY));
	}
	
	public void keyReleased() {
		if(key=='w')wDown = false;
		if(key=='s')sDown = false;
		if(key=='a')aDown = false;
		if(key=='d')dDown = false;
	}
	
	public void settings(){
	  size(600, 800);
	}

	public void draw(){
	  background(100);
	  
	  push();
	  noStroke();
	  fill(0);
	  rect(0, 600, width, 200);
	  pop();
	  
	  //raycast
	  int vd = 200;//view length
	  float inc = fov/100;
	  float sw = 0.3f;
	  for(float i=rot-fov;i<rot+fov;i+=inc){//loop(fov)
	    boolean result = false;
	    for(int j=0;j<vd;j+=2){//loop(vd)
	      for(int k=0;k<path.size()-1;k++){//loop(path)
	        PVector linePos = PVector.fromAngle(i).mult(j).add(pos);
	        if(intersects(path.get(k), path.get(k+1), pos, linePos)){
	          result = true;
	          //casting
	          push();
	          stroke(255);
	          strokeWeight(sw);
	          line(pos.x, pos.y, linePos.x, linePos.y);
	          pop();
	          //rendering
	          push();
	          float col = map(j, 0, vd, 255, 0);
	          stroke(col);
	          strokeWeight(width/100);
	          float xp = map(i, rot-fov, rot+fov, 0, width);
	          float h = map(j*cos(i-rot ), 0, 200, 100, 0);
	          line(xp, 700-h, xp, 700+h);
	          pop();
	          j = vd;
	          k = path.size();
	        }
	      }
	    }
	    if(!result){
	      PVector linePos = PVector.fromAngle(i).mult(vd).add(pos);
	      push();
	      stroke(255);
	      strokeWeight(sw);
	      line(pos.x, pos.y, linePos.x, linePos.y);
	      pop();
	    }
	  }
	  
	  //update player
	  if(wDown){
	    vel.x += speed*Math.cos(rot);//w
	    vel.y += speed*Math.sin(rot);//w
	  }
	  if(sDown){
	    vel.x += speed*Math.cos(rot+Math.PI);//s
	    vel.y += speed*Math.sin(rot+Math.PI);//s
	  }
	  pos.x += vel.x;
	  pos.y += vel.y;
	  vel.x *= 0.8;
	  vel.y *= 0.8;
	  rv += aDown ? -rs : dDown ? rs : 0;
	  rv *= 0.8;
	  rot += rv;
	  
	  push();
	  noStroke();
	  fill(0, 255, 255);
	  circle(pos.x, pos.y, 15);
	  pop();
    push();
	  textSize(16);
	  textAlign(CENTER);
	  text("RayCast", width/2, 15);
	  text("Render", width/2, 595);
	  pop();
	  for(int i=0;i<path.size()-1;i++){
	    push();
	    PVector a = path.get(i);
	    PVector b = path.get(i+1);
	    line(a.x, a.y, b.x, b.y);
	    pop();
	  }
	  surface.setTitle("Raycaster ... "+round(frameRate)+"fps");
	}
	
	public boolean intersects(PVector a, PVector b, PVector c, PVector d){
    float den = (a.x-b.x)*(c.y-d.y)-(a.y-b.y)*(c.x-d.x);
    float t = ((a.x-c.x)*(c.y-d.y)-(a.y-c.y)*(c.x-d.x))/den;
    float u = ((b.x-a.x)*(a.y-c.y)-(b.y-a.y)*(a.x-c.x))/den;
    return (t>=0.0&&t<=1.0&u>=0.0&&u<=1.0);
  }
}
