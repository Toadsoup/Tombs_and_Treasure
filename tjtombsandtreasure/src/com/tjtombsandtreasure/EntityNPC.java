
package com.tjtombsandtreasure;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EntityNPC extends Entity {
	public double xv, yv;
	public boolean onFloor = false;
	public int tickcount = 0;
	
	public double height = 0.5;
	public double topheight = 0.25;
	public double width = .50;
	public double rightwidth = 0.25;
	
	public int moveDirection = 1;
	
	public EntityNPC(BufferedImage Sprite, double X, double Y)
	{
		super(Sprite, X, Y);
//		sprite = Sprite;
		
	}
	

	public void Tick(Level level)
	{	

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
