import java.awt.Image;
import java.awt.event.*;
import javax.swing.ImageIcon;

public class Pacman {

	private int direction; //1=right(default) 2=left 3=up 4=down
	private int xPacCor;
	private int yPacCor;
	private int moveX = 0;
	private int moveY = 0;
	private Image pacman;
	protected boolean powerUp = false;
	protected int score = 0;
	protected int arrayRow;
	protected int arrayCol;
	protected int superCounter;

	public void pacReset(){
		this.setDirection(1);
		this.xPacCor = 190;
		this.yPacCor = 290;
		this.arrayRow = 14;
		this.arrayCol = 9;
		this.superCounter = 0;
		this.moveX = 0;
		this.moveY = 0;	
	}

	public Pacman(){
		this.pacReset();
	}

	public void setDirection(int direction){
		this.direction = direction;
		if (direction==1){
			pacman = new ImageIcon(this.getClass().getResource("images/PacmanRight.png")).getImage();
		} else if (direction==2){
			pacman = new ImageIcon(this.getClass().getResource("images/PacmanLeft.png")).getImage();
		} else if (direction==3){
			pacman = new ImageIcon(this.getClass().getResource("images/PacmanUp.png")).getImage();
		} else if (direction==4){
			pacman = new ImageIcon(this.getClass().getResource("images/PacmanDown.png")).getImage();
		}
	}

	public void closeMouth(){
		pacman = new ImageIcon(this.getClass().getResource("images/PacmanClosed.png")).getImage();
	}

	public int getX(){return xPacCor;}
	public int getY(){return yPacCor;}
	public int getDirection(){return direction;}
	public void setScore(int score){this.score = score;}
	public Image getImage(){return pacman;}
	public boolean getPower(){return powerUp;}
	public void noPower(){powerUp = false; superCounter = 0;}

	public void move(boolean mouthAnimate){
		setDirection(getDirection());
		if (mouthAnimate && (moveX!=0 || moveY!=0))
			closeMouth();
		xPacCor += moveX;
		yPacCor += moveY;
	}

	public void move2(boolean mouthAnimate){
		setDirection(getDirection());
		if (mouthAnimate && (moveX!=0 || moveY!=0))
			closeMouth();
		xPacCor += moveX;
		yPacCor += moveY;
		if (moveX == 10)
			arrayCol += 1;
		if (moveX == -10)
			arrayCol -= 1;
		if (moveY == 10)
			arrayRow += 1;
		if (moveY == -10)
			arrayRow -= 1;
	}

	public void moveRight(){
		setDirection(1);
		moveX = 10;
		moveY = 0;
	}
	public void moveLeft(){
		setDirection(2);
		moveX = -10;
		moveY = 0;
	}
	public void moveUp(){
		setDirection(3);
		moveX = 0;
		moveY = -10;
	}
	public void moveDown(){
		setDirection(4);
		moveX = 0;
		moveY = 10;
	}

	public void detectWall(int[][] currentLevels){
		if (arrayCol<18){
			if (direction == 1 && currentLevels[arrayRow][arrayCol+1] == 2){
				moveX = 0;
			}
		}
		if (arrayCol>0){
			if (direction == 2 && currentLevels[arrayRow][arrayCol-1] == 2){
				moveX = 0;
			}
		}
		if (arrayRow>0){
			if (direction == 3 && currentLevels[arrayRow-1][arrayCol] == 2){
				moveY = 0;
			}
		}
		if (arrayRow<28){
			if (direction == 4 && currentLevels[arrayRow+1][arrayCol] == 2){
				moveY = 0;
			}
		}
	}

	public void detectEdge(){
		if (xPacCor>=370 && direction == 1){
			xPacCor = 370;
			moveX = 0;
		}
		if (xPacCor<=10 && direction == 2){
			xPacCor = 10;
			moveX = 0;
		}
		if (yPacCor<=10 && direction == 3){
			yPacCor = 10;
			moveY = 0;
		}
		if (yPacCor>=570 && direction == 4){
			yPacCor = 570;
			moveY = 0;
		}
	}

	public void eatFruit(int[][] currentLevels){
		if (currentLevels[arrayRow][arrayCol] == 1 || currentLevels[arrayRow][arrayCol] == 11){
			this.score+=1;
			currentLevels[arrayRow][arrayCol] = 0;
		}
		if (currentLevels[arrayRow][arrayCol]==21){
			this.score+=1;
			currentLevels[arrayRow][arrayCol] = 0;
			powerUp = true;
			superCounter = 0;
		}
	}
}