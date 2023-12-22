package application;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Chicken extends ImageView{
    private Image[] sprites;
    double w,h,maxWidth,maxHeight,rychlost;
    int act = 0;
    int stav = 0;
    private Image[] spritesd;
    private int live = 13;
    private int dead = 8;
    private String s,n;
    private Timeline Timeline;
    private Pane root;
    private AmmunitionManager ammunitionManager;
    private AudioClip hit1;
    private AudioClip hit2;
    private AudioClip hit3;
    private ScoreCounter scoreCounter;
    
    public Chicken(Pane root, double maxw, double maxh,String s,String n,AmmunitionManager ammunitionManager,ScoreCounter scoreCounter) {
    	   super();
    	   this.scoreCounter = scoreCounter;
    	   this.ammunitionManager = ammunitionManager;
    	   maxWidth = maxw; maxHeight = maxh;
    	   this.s=s;
    	   this.n=n;
    	   hit1 = new AudioClip(getClass().getResource("/chick_hit1.mp3").toString());
    	   hit2 = new AudioClip(getClass().getResource("/chick_hit2.mp3").toString());
    	   hit3 = new AudioClip(getClass().getResource("/chick_hit3.mp3").toString());
    	   this.root=root;
    	   sprites = new Image[live];
    	   for(int i = 0; i < live; i++) {
    	       sprites[i] = new Image("spriteLive"+n+s+(i+1)+".png");
    	   }
    	   spritesd = new Image[dead];
    	   for(int i = 0; i < dead; i++) {
    	       spritesd[i] = new Image("spriteDead"+n+s+(i+1)+".png");
    	   }
    	   Timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {vykresli();move();
           }));
    	   Timeline.setCycleCount(Animation.INDEFINITE);
    	   Timeline.play();
    	   
    	        
    	   if (n.equals("Right")) {
           setLayoutX(0); 
       } else if (n.equals("Left")) {
           setLayoutX(maxw); 
       }
    	   setLayoutY(100 + (int) (Math.random()* maxHeight)); 
   	    setOnMousePressed(evt->onClick());		
    	} 
    
    


    private void vykresli() {    
        if(this.stav==0) {
            setImage(sprites[act]);  
            act++;
            if(act >= sprites.length) act = 0;
        }
        if(this.stav==1) {
            setImage(spritesd[act]);
            act++;
            if(act >= spritesd.length) { 
                removeImage();
                act = 0;
            }
        }
    }

     
    
    private void removeImage() {
        root.getChildren().remove(this);
        act = 0;
        stav = 0;
    }


    
    private void move() {
    	if(this.n=="Right") this.setLayoutX(this.getLayoutX()+5);
    	if(this.n=="Left") this.setLayoutX(this.getLayoutX()-5);
    	if(this.getLayoutX()>=root.getWidth() || this.getLayoutX()<=-50) this.root.getChildren().remove(this);
    }
    private void onClick() {
    	   if(this.stav==1 || this.stav==2) {
    		   System.out.println("Dead");
    	   }
    	   else if (ammunitionManager.removeBullet()) {
               stav = 1; 
        	   act=0;
        	   playRandomHitSound();
        	   scoreCounter.increaseScore(this.s);
           }
    	   else {
    		   System.out.println("not bul");
    	   }
    	}
 
    private void playRandomHitSound() {
        Random random = new Random();
        int randomSound = random.nextInt(3) + 1; 

        switch (randomSound) {
            case 1:
                hit1.play();
                break;
            case 2:
                hit2.play();
                break;
            case 3:
                hit3.play();
                break;
        }
    }


}
