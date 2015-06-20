import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.*;

public class Monster {

	private int color; //1=red 2=yellow 3=pink 4=blue
	private int direction; //1=right 2=left 3=up 4=down
	private int xMonstCor;
	private int yMonstCor;
	protected int arrayRow;
	protected int arrayCol;
	private int moveX = 0;
	private int moveY = 0;
	private Image monster;
	private Random rand = new Random();
	protected boolean dead;

	public void monsterReset(int color){
		dead = false;
		this.color = color;
		if (color == 1){
			monster = new ImageIcon(this.getClass().getResource("images/monsterRed.png")).getImage();
			xMonstCor = 10;
			yMonstCor = 570;
			arrayRow = 28;
			arrayCol = 0;
			direction = 1;
		}
		if (color == 2){
			monster = new ImageIcon(this.getClass().getResource("images/monsterYellow.png")).getImage();
			xMonstCor = 10;
			yMonstCor = 10;
			arrayRow = 0;
			arrayCol = 0;
			direction = 4;
		}
		if (color == 3){
			monster = new ImageIcon(this.getClass().getResource("images/monsterPink.png")).getImage();
			xMonstCor = 370;
			yMonstCor = 10;
			arrayRow = 0;
			arrayCol = 18;
			direction = 2;
		}
		if (color == 4){
			monster = new ImageIcon(this.getClass().getResource("images/monsterBlue.png")).getImage();
			xMonstCor = 370;
			yMonstCor = 570;
			arrayRow = 28;
			arrayCol = 18;
			direction = 3;
		}
	}

	public Monster(int color){
		this.monsterReset(color);
	}

	public int getX(){return xMonstCor;}
	public int getY(){return yMonstCor;}
	public Image getImage(){return monster;}

	public boolean detectWall(int[][] lvlData, int direction){ // detects if there is a maze wall in front
		if (dead)
			return false;
		if (arrayCol<18){
			if (direction == 1 && lvlData[arrayRow][arrayCol+1] == 2){
				return true;
			}
		}
		if (arrayCol>0){
			if (direction == 2 && lvlData[arrayRow][arrayCol-1] == 2){
				return true;
			}
		}
		if (arrayRow>0){
			if (direction == 3 && lvlData[arrayRow-1][arrayCol] == 2){
				return true;
			}
		}
		if (arrayRow<28){
			if (direction == 4 && lvlData[arrayRow+1][arrayCol] == 2){
				return true;
			}
		}
		return false;
	}

	public boolean detectEdge(int direction){ // detects if it is at the edge
		if (xMonstCor>=370 && direction == 1){
			xMonstCor = 370;
			return true;
		}
		if (xMonstCor<=10 && direction == 2){
			xMonstCor = 10;
			return true;
		}
		if (yMonstCor<=10 && direction == 3){
			yMonstCor = 10;
			return true;
		}
		if (yMonstCor>=570 && direction == 4){
			yMonstCor = 570;
			return true;
		}
		return false;
	}

	public boolean canMoverForward(int[][] lvlData, int direction){ // combines detect Edge and Wall
		if (detectEdge(direction) || detectWall(lvlData, direction))
			return false;
		return true;
	}

	public void moveForward(int[][] lvlData, int direction){ // generic move forward in the specified direction
		if (direction==1){
			xMonstCor+=20;
			arrayCol+=1;
		}
		if (direction==2){
			xMonstCor-=20;
			arrayCol-=1;
		}
		if (direction==3){
			yMonstCor-=20;
			arrayRow-=1;
		}
		if (direction==4){
			yMonstCor+=20;
			arrayRow+=1;
		}
	}

	public void moveRandom(int[][] lvlData){
		boolean moved = false;
		if (canMoverForward(lvlData, this.direction)){
			moveForward(lvlData, this.direction);
			moved = true;
		}
		else{
			int newDirection;
			while(!moved){
				newDirection = rand.nextInt(4)+1;
				if (canMoverForward(lvlData, newDirection)){
					moveForward(lvlData, newDirection);
					moved = true;
					this.direction = newDirection;
				}
			}
		}
	}

	public boolean obstructedView(int[][] lvlData, Pacman player, Monster ghost, int orientation){
		int bigger;
		int smaller;
		if (orientation==1){ //column
			if (player.arrayRow > ghost.arrayRow){
				bigger = player.arrayRow;
				smaller = ghost.arrayRow;
			} else{
				bigger = ghost.arrayRow;
				smaller = player.arrayRow;
			}
			for (int i=smaller+1;i<bigger;i++){
				if(lvlData[i][ghost.arrayCol] == 2)
					return true;
			}
		}
		else{ //row
			if (player.arrayCol > ghost.arrayCol){
				bigger = player.arrayCol;
				smaller = ghost.arrayCol;
			} else{
				bigger = ghost.arrayCol;
				smaller = player.arrayCol;
			}
			for (int i=smaller+1;i<bigger;i++){
				if(lvlData[ghost.arrayRow][i] == 2)
					return true;
			}
		}
		return false;
	}

	public void changeColor(Pacman player){
		if(player.getPower()){
			monster = new ImageIcon(this.getClass().getResource("images/monsterWeak.png")).getImage();
		}
		else if(color == 1){
			monster = new ImageIcon(this.getClass().getResource("images/monsterRed.png")).getImage();
		}
		else if(color == 2){
			monster = new ImageIcon(this.getClass().getResource("images/monsterYellow.png")).getImage();
			
		}
		else if(color == 3){
			monster = new ImageIcon(this.getClass().getResource("images/monsterPink.png")).getImage();
			
		}
		else if(color == 4){
			monster = new ImageIcon(this.getClass().getResource("images/monsterBlue.png")).getImage();
		}

	}

	public void moveSearch(int[][]lvlData, Pacman player){
		if (!player.powerUp && dead){
			dead = false;
		}
		changeColor(player);
		if (!die(player)) {
			if(!player.getPower()){
				int chance = rand.nextInt(10)+1;
				if (chance>6)
					moveRandom(lvlData);
				else{
					if (player.arrayCol == this.arrayCol && !obstructedView(lvlData,player,this,1)){
						if (player.arrayRow > this.arrayRow)
							this.direction = 4;
						if (player.arrayRow < this.arrayRow)
							this.direction = 3;
						if (canMoverForward(lvlData, this.direction))
							this.moveForward(lvlData, this.direction);
					}
					else if (player.arrayRow == this.arrayRow && !obstructedView(lvlData,player,this,2)){
						if (player.arrayCol < this.arrayCol)
							this.direction = 2;
						if (player.arrayCol > this.arrayCol)
							this.direction = 1;
						if (canMoverForward(lvlData, this.direction))
							this.moveForward(lvlData, this.direction);
					}
					else
						moveRandom(lvlData);
				}
			}
			else {
				int chance = rand.nextInt(10)+1;
				if (chance>6)
					moveRandom(lvlData);
				else{
					if (player.arrayCol == this.arrayCol && !obstructedView(lvlData,player,this,1)){
						if (player.arrayRow > this.arrayRow)
							this.direction = 3;
						if (player.arrayRow < this.arrayRow)
							this.direction = 4;
						if (canMoverForward(lvlData, this.direction))
							this.moveForward(lvlData, this.direction);
					}
					else if (player.arrayRow == this.arrayRow && !obstructedView(lvlData,player,this,2)){
						if (player.arrayCol < this.arrayCol)
							this.direction = 1;
						if (player.arrayCol > this.arrayCol)
							this.direction = 2;
						if (canMoverForward(lvlData, this.direction))
							this.moveForward(lvlData, this.direction);
					}
					else
						moveRandom(lvlData);
				}
			}
		}
	}

	public boolean kill(Pacman player){
		// if player is within 21 pixels and they are at the same location on the board they die
		int xDiff = Math.abs(this.xMonstCor-player.getX());
		int yDiff = Math.abs(this.yMonstCor-player.getY());
		if (!player.getPower() && (xDiff < 21 && yDiff < 21) && (this.arrayCol == player.arrayCol || this.arrayRow == player.arrayRow))
			return true;
		return false;
	}

	public boolean die(Pacman player){
		// if player is within 21 pixels and they are at the same location on they eat the monster
		int xDiff = Math.abs(this.xMonstCor-player.getX());
		int yDiff = Math.abs(this.yMonstCor-player.getY());
		if (player.getPower() && (xDiff <  21 && yDiff< 21) && (this.arrayCol == player.arrayCol || this.arrayRow == player.arrayRow)){
			if (!dead)
				player.score += 50;
			dead = true;

			return true;
		}
		return false;
	}
}