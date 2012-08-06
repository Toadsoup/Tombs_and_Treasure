
package com.tjtombsandtreasure;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class EntityPlayer extends Entity {
	public double xv, yv;
	public boolean onFloor = false;
	public int tickcount = 0;
	
	public boolean action = false;
	public int ropeLenght = 5;
	
	
	public double height = 0.5;
	public double topheight = 0.25;
	public double width = .50;
	public double rightwidth = 0.25;

	public static BufferedImage adventureguy = Art.Load("adventure-guy-idle.png");
	public static BufferedImage adventureguyright = Art.Load("adventure-guy-right.png");
	public static BufferedImage adventureguyleft = Art.Load("adventure-guy-left.png");
	
	public EntityPlayer(double X, double Y)
	{
		super(adventureguy, X, Y);
	}
	public void actionStart()
	{
		if (!action)
			actionEvent();
		action = true;
	}
	public void actionEvent()
	{
		action = true;
		System.out.println("Action Started!");
	}
	public void actionOver()
	{
		if(action)
		{
		action = false;
		System.out.println("Action Over!");
		}
	}
	
	public void Tick(Level level, Input input)
	{	

		this.xv = 0;
		if(input.keys[KeyEvent.VK_LEFT])
		{
			this.xv = -0.1;
			this.sprite = adventureguyleft;
		}	
		else if(input.keys[KeyEvent.VK_RIGHT])
		{
			this.xv = 0.15;
			this.sprite= adventureguyright;
		}
		else
			this.sprite= adventureguy;
		
		
		if(input.keys[KeyEvent.VK_UP])
			if(this.onFloor)
				this.yv = -0.25;
		
		if(input.keys[KeyEvent.VK_SPACE])
			this.actionStart();
		else
			this.actionOver();
		
		if (yv <=1 && yv > .25)
			yv += 0.00175;
		else if (yv <= .25 && yv > 0 )
			yv += 0.0035;
		else
			yv += 0.007;
		
		tickcount++;
		
		double xPos = (x + 0.6);
		double yPos = (y + 0.5);
		double nxPos = (x + 0.5 + xv);
		double nyPos = (y + 0.5 + yv);
		
		onFloor = false;
		
		if(
				   level.GetTile((int)(nxPos - width), (int)(yPos - topheight)) == 0
				&& level.GetTile((int)(nxPos - width), (int)(yPos + height)) == 0
				&& level.GetTile((int)(nxPos + width), (int)(yPos - topheight)) == 0
				&& level.GetTile((int)(nxPos + width), (int)(yPos + height)) == 0)
			x += xv;
		else
			xv = 0;
		
		if(		   
				   level.GetTile((int)(xPos - width), (int)(nyPos - topheight)) == 0
				&& level.GetTile((int)(xPos - width), (int)(nyPos + height)) == 0
				&& level.GetTile((int)(xPos + rightwidth), (int)(nyPos - topheight)) == 0
				&& level.GetTile((int)(xPos + rightwidth), (int)(nyPos + height)) == 0)
		{
			y += yv;
		}
		else
		{
			if(yv > 0)
				onFloor = true;
			
			yv = 0;
		}
		
	}

	public void Render(Graphics g, double cameraX, double cameraY)
	{
		g.drawImage(sprite, (int)(x * 8 * Main.SCALE) + (int)(cameraX * 8 * Main.SCALE), (int)(y * 8 * Main.SCALE) + (int)(cameraY * 8 * Main.SCALE), 8 * Main.SCALE, 8 * Main.SCALE, null);
	}
}
