import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import java.util.*;
public class Breakout extends Application implements EventHandler<InputEvent>
{
	GraphicsContext gc;
	AnimateObjects animate;
	int x=0;
	Canvas canvas;
	ArrayList red;
	ArrayList orange;
	ArrayList yellow;
	ArrayList green;
	ArrayList blue;
	int circleX;
	int circleY;
	int chances=4;
	int bounce=0;
	int changeX;
	int changeY;
	boolean rightPressed;
	boolean leftPressed;
	int score=0;
	AudioClip clip;
	//increase speed at orange or after 12 bounces
	//4 chances, increases when cleared
	//4 points per block
	public static void main(String args[])
	{
		launch();
	}
	public void start(Stage stage)
	{
		stage.setTitle("Breakout");
		Group root=new Group();
		canvas=new Canvas(800,600);
		root.getChildren().add(canvas);
		Scene scene=new Scene(root);
		stage.setScene(scene);
		gc=canvas.getGraphicsContext2D();
		circleX=370;
		circleY=520;
		changeX=-5;
		changeY=-5;
		rightPressed=false;
		leftPressed=false;
		red=new ArrayList<Rectangle2D>();
		for(int i=0;i<=700;i+=100)
			red.add(new Rectangle2D(i+0.0,0.0,100.0,50.0));
		orange=new ArrayList<Rectangle2D>();
		for(int i=0;i<=700;i+=100)
			orange.add(new Rectangle2D(i+0.0,50.0,100.0,50.0));
		yellow=new ArrayList<Rectangle2D>();
		for(int i=0;i<=700;i+=100)
			yellow.add(new Rectangle2D(i+0.0,100.0,100.0,50.0));
		green=new ArrayList<Rectangle2D>();
		for(int i=0;i<=700;i+=100)
			green.add(new Rectangle2D(i+0.0,150.0,100.0,50.0));
		blue=new ArrayList<Rectangle2D>();
		for(int i=0;i<=700;i+=100)
			blue.add(new Rectangle2D(i+0.0,200.0,100.0,50.0));
		animate=new AnimateObjects();
		animate.start();
		stage.show();
		scene.addEventHandler(KeyEvent.KEY_PRESSED,this);
		scene.addEventHandler(KeyEvent.KEY_RELEASED,this);
		scene.addEventHandler(MouseEvent.MOUSE_CLICKED,this);
		URL resource=getClass().getResource("Sound.wav");
		clip=new AudioClip(resource.toString());
	}
	public void handle(final InputEvent event)
	{
		if(event instanceof KeyEvent)
		{
			if(event.getEventType()==KeyEvent.KEY_PRESSED)
			{
				if(((KeyEvent)event).getCode()==KeyCode.LEFT)
					leftPressed=true;
				if(((KeyEvent)event).getCode()==KeyCode.RIGHT)
					rightPressed=true;
			}
			if(event.getEventType()==KeyEvent.KEY_RELEASED)
			{
				if(((KeyEvent)event).getCode()==KeyCode.LEFT)
					leftPressed=false;
				if(((KeyEvent)event).getCode()==KeyCode.RIGHT)
					rightPressed=false;
			}
		}
		if(event instanceof MouseEvent)
		{
			System.out.println(((MouseEvent)event).getX());
			System.out.println(((MouseEvent)event).getY());
		}
	}
	public class AnimateObjects extends AnimationTimer
	{
		public void handle(long now)
		{
			if(chances==0)
			{
				gc.setFill(Color.GREY);
				gc.setStroke(Color.GREY);
				gc.setLineWidth(1);
				Font font=Font.font("SansSerif",FontWeight.NORMAL,48);
				gc.setFont(font);
				gc.fillText("Game Over",275,300);
				gc.strokeText("Game Over",275,300);
				gc.fillText("Score: "+score,300,350);
			}
			else
			{
				if(leftPressed)
					x-=10;
				if(rightPressed)
					x+=10;

				if(x<-325 || x>335)
				{
					if(x<-325)
						x+=10;
					if(x>335)
						x-=10;
				}
				gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
				gc.setFill(Color.CRIMSON);
				for(int i=0;i<red.size();i++)
					gc.fillRect(((Rectangle2D)red.get(i)).getMinX(),0.0,100.0,50.0);
				gc.setFill(Color.DARKORANGE);
				for(int i=0;i<orange.size();i++)
					gc.fillRect(((Rectangle2D)orange.get(i)).getMinX(),50.0,100.0,50.0);
				gc.setFill(Color.GOLD);
				for(int i=0;i<yellow.size();i++)
					gc.fillRect(((Rectangle2D)yellow.get(i)).getMinX(),100.0,100.0,50.0);
				gc.setFill(Color.FORESTGREEN);
				for(int i=0;i<green.size();i++)
					gc.fillRect(((Rectangle2D)green.get(i)).getMinX(),150.0,100.0,50.0);
				gc.setFill(Color.ROYALBLUE);
				for(int i=0;i<blue.size();i++)
					gc.fillRect(((Rectangle2D)blue.get(i)).getMinX(),200.0,100.0,50.0);
				gc.setFill(Color.BLACK);
				Rectangle2D base=new Rectangle2D(320.0+x,570.0,150.0,25.0);
				gc.setFill(Color.GREY);
				gc.setLineWidth(10);
				Font font=Font.font("SansSerif",FontWeight.NORMAL,48);
				gc.setFont(font);
				gc.fillText("Score: "+score,300,350);
				gc.fillText("Chances: "+chances,300,400);
				gc.fillRect(320.0+x,570.0,150.0,25.0);
				Rectangle2D r=new Rectangle2D(base.getMinX(),base.getMinY(),150,25);
				Rectangle2D b=new Rectangle2D(circleX+changeX,circleY+changeY,50.0,50.0);
				if(r.intersects(b) || circleY+changeY<0)
				{
					changeY*=-1;
					clip.play();
				}
				if(r.intersects(b))
					bounce++;
				if(bounce==12)
				{
					if(changeX==5)
						changeX=6;
					if(changeX==-5)
						changeX=-6;
					if(changeY==5)
						changeY=6;
					if(changeY==-5)
						changeY=-6;
				}
				if(circleX+changeX<0 || circleX+changeX>750)
				{
					changeX*=-1;
					clip.play();
				}
				for(int i=0;i<blue.size();i++)
					if(((Rectangle2D)blue.get(i)).intersects(b))
					{
						blue.remove(i);
						changeY*=-1;
						score+=4;
						clip.play();
					}
				for(int i=0;i<green.size();i++)
					if(((Rectangle2D)green.get(i)).intersects(b))
					{
						green.remove(i);
						changeY*=-1;
						score+=4;
						clip.play();
					}
				for(int i=0;i<yellow.size();i++)
					if(((Rectangle2D)yellow.get(i)).intersects(b))
					{
						yellow.remove(i);
						changeY*=-1;
						score+=4;
						clip.play();
					}
				for(int i=0;i<orange.size();i++)
					if(((Rectangle2D)orange.get(i)).intersects(b))
					{
						orange.remove(i);
						score+=4;
						changeX=(changeX/changeX)*10;
						clip.play();
						if(changeY==5)
							changeY=6;
						if(changeY==-5)
							changeY=-6;
						if(changeX==5)
							changeX=6;
						if(changeX==-5)
							changeX=-6;
					}
				for(int i=0;i<red.size();i++)
					if(((Rectangle2D)red.get(i)).intersects(b))
					{
						red.remove(i);
						changeY*=-1;
						score+=4;
						clip.play();
					}
				circleX+=changeX;
				circleY+=changeY;
				if(circleY+changeY>=550)
				{
					chances--;
					circleX=370;
					circleY=520;
					changeY=-5;
				}
				System.out.println(chances);
				Circle c=new Circle(circleX+changeX,circleY+changeY,50.0);
				gc.fillOval(circleX+changeX,circleY+changeY,50,50);
				gc.setStroke(Color.GREY);
				gc.fillText("Chances: "+chances,0,750);
				gc.strokeText("Chances: "+chances,0,750);
			}
		}
	}
}