package com.tjtombsandtreasure;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Art {
	public static BufferedImage Load(String string)
	{
		BufferedImage img;
		try {
			img = ImageIO.read(Art.class.getResource(string));
		} catch (IOException e) {
			return null;
		}
		return img;
	}
}
