package com.tjtombsandtreasure;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
//import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

//import com.battlezone.Main;
import com.tjtombsandtreasure.Entity;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	boolean running = true;
	
	public static int HEIGHT = 128;
	public static int WIDTH = 256;
	public static int SCALE = 4;
	
	private static Thread thread;
	private Input input = new Input();
	
	BufferStrategy bs;
	private BufferedImage bgImage;
	private BufferedImage cat = Art.Load("cat.png");
//	private BufferedImage cat2 = Art.Load("/cat2.png");
	private BufferedImage treasure = Art.Load("treasure.png");
	private BufferedImage adventureguy = Art.Load("adventure-guy-idle.png");
	private BufferedImage adventureguyright = Art.Load("adventure-guy-right.png");
	private BufferedImage adventureguyleft = Art.Load("adventure-guy-left.png");
	
//	private BufferedImage levelImage = Art.Load("/level-blank-hook.png");
	private BufferedImage levelImage = Art.Load("level-1b.png");	
	
	private double cameraX = 0;
	private double cameraY = 0;
//	private int last = 0;
	
	public int tickcount = 0;
	
	private Level level = new Level(levelImage);
	private EntityNPC[] entities = new EntityNPC[15];
	private EntityTreasure[] treasures = new EntityTreasure[500];
	private EntityPlayer adventurer = new EntityPlayer(adventureguy, 100,100);
	
	
	public Main()
	{
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setSize(size);
		setPreferredSize(size);
		
		bgImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		addKeyListener(input);
		
		Random r = new Random();
		
		for(int i = 0; i < entities.length; i++)
		{
			entities[i] = new EntityNPC(cat, 2 + r.nextDouble() * 22, 2 + r.nextDouble() * 4);
		}
		for(int i = 0; i < treasures.length; i++)
		{
			treasures[i] = new EntityTreasure(treasure, 2 + r.nextDouble() * 26, 1 + r.nextDouble() * 4);
		}
			adventurer = new EntityPlayer(adventureguy, 2+r.nextDouble()*22, + r.nextDouble() * 4);

		
	}
	
	public static void main(String[] args)
	{
		Main main2 = new Main();
		JFrame frame = new JFrame();
		frame.add(main2);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		thread = new Thread(main2);
		thread.start();
	}
	
	public void run()
	{
		createBufferStrategy(3);
		bs = getBufferStrategy();
		
		double secondsPerTick = 1 / 60.0;
		long lastTime = System.nanoTime();
		double unprocessedTime = 0;
		
		while(running)
		{
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			
			unprocessedTime += passedTime / 1000000000.0;
			while(unprocessedTime > secondsPerTick)
			{
				unprocessedTime -= secondsPerTick;
				tick();
				render();
			}
		}
		System.exit(0);
	}
	
	public void tick()
	{
		tickcount++;
		
		adventurer.xv = 0;
		if(input.keys[KeyEvent.VK_LEFT])
		{
			adventurer.xv = -0.1;
			adventurer.sprite = adventureguyleft;
		}	
		else if(input.keys[KeyEvent.VK_RIGHT])
		{
			adventurer.xv = 0.15;
			adventurer.sprite= adventureguyright;
		}
		else
			adventurer.sprite= adventureguy;
		
		
		if(input.keys[KeyEvent.VK_UP])
			if(adventurer.onFloor)
				adventurer.yv = -0.25;
		
		if(input.keys[KeyEvent.VK_SPACE])
			adventurer.actionStart();
		else
			adventurer.actionOver();
		
		
		
		
		
		if(input.keys[KeyEvent.VK_ESCAPE])
			running = false;
		
		for(int i = 0; i < entities.length; i++)
		{
			if(entities[i].onFloor)
				entities[i].xv = 0.1;
//			else entities[i].xv = 0;
			entities[i].Tick(level);
		}
		adventurer.onFloorCount = 0;
		adventurer.Tick(level);
		
		
		
		for(int i = 0; i < treasures.length; i++)
		{
			treasures[i].Tick(level);
		}
		
	}
	
	public void render()
	{
		Graphics g = bs.getDrawGraphics();

		// Draw background
		g.drawImage( bgImage, 0, 0, WIDTH * SCALE, WIDTH * SCALE, null );
		
		
		// Set Camera X to left side of screen
		cameraX = 0;
		// Set Camera Y to base off Adventurer position
		// Fix when at bottom of Screen so you don't see past the map
		cameraY = -(adventurer.y)+ 10;
		if ( level.height - (int)adventurer.y < 7)
			cameraY = -(level.height -16) ;
			
		
	//	System.out.println("Level height = " + level.height + " Adventurer Y = " + adventurer.y);
	//	System.out.println(level.height -(int) adventurer.y);
		
		
		
		if (cameraY > 0 )
			cameraY = 0;
			
		level.Draw(g, 0, (int)(cameraY*8) );	

		//Looping background here	
		
		
		/*    // Reviewing Position
			if(last != (int) adventurer.y)
			{
				System.out.println((int) adventurer.y);
				last = (int) adventurer.y;
			}
		*/
		
		for(int i = 0; i < entities.length; i++)
		{
			entities[i].Render(g, (cameraX), (cameraY));
		}
		for(int i = 0; i<treasures.length;i++)
		{
			treasures[i].Render(g, (cameraX),(cameraY));
		}
		
		adventurer.Render(g, cameraX, cameraY);
		
		
		g.setColor(Color.RED);
//		g.drawLine((int) (adventurer.x* 8 * Main.SCALE)+(int)(cameraX *8 *SCALE), (int)(adventurer.y * 8 * Main.SCALE) + (int)(cameraY*8*SCALE), (int) 0, (int) (cameraY * 8));

		bs.show();
	}
	
	public void draw(BufferedImage toDraw, int x, int y)
	{
		Graphics g = bs.getDrawGraphics();
		g.drawImage(toDraw, x * SCALE, y * SCALE, 4 * SCALE, 4 * SCALE, null);
	}

}
