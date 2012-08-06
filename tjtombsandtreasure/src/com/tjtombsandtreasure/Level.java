package com.tjtombsandtreasure;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Level {
	public int tiles[];
	public int width;
	public int height;
	
	private BufferedImage tile0 = Art.Load("block1.png");
	private BufferedImage tile1 = Art.Load("brick.png");
	
	public Level(BufferedImage levelImage)
	{
		width = levelImage.getWidth();
		height = levelImage.getHeight();
		
		tiles = new int[width * height];
		int pixels[] = new int[width * height];
		
		// copy image pixels
		levelImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		for(int i = 0; i < width * height; i++)
		{
			// we don't need no alpha
			int pixel = pixels[i];
			int r = (pixel >> 16) & 0xff;
			int g = (pixel >> 8) & 0xff;
			int b = pixel & 0xff;
			
			pixel = (r << 16) | (g << 8) | b;
			pixels[i] = pixel;
			
		//	System.out.println( i +" " + pixel + "--- R:" + r + " G:" + g + " B:" + b);
			
		}
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				int pixel = pixels[x + y * width];
				int tile = 0;
				
				if( pixel == 0x000000 ) tile = 1;
				
				tiles[x + y * width] = tile;
			}
		}
	}
	
	public int GetTile(int x, int y)
	{
		return tiles[x + y * width];
	}
	
	public void Draw(Graphics g, int xOffset, int yOffset)
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				int curTile = tiles[x + y * width];
				if(curTile == 1)
				{
					g.drawImage(tile0,
							8 * x * Main.SCALE + xOffset * Main.SCALE,
							8 * y * Main.SCALE + yOffset * Main.SCALE,
							8 * Main.SCALE,
							8 * Main.SCALE,
							null);
				}
				else
				{
					g.drawImage(tile1,
							8 * x * Main.SCALE + xOffset * Main.SCALE,
							8 * y * Main.SCALE + yOffset * Main.SCALE,
							8 * Main.SCALE,
							8 * Main.SCALE,
							null);
				}
			}
		}
	}
}
