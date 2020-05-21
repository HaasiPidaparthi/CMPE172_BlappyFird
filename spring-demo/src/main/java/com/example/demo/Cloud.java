package com.example.demo;

import com.example.demo.Obstacle;

public class Cloud extends Obstacle {
	private static final String imagePath = "src/sprites/cloud.png";
	public static final int WIDTH = 115, HEIGHT = 48;
	
	public Cloud(int x, int y, int scrollSpeed) {
		super(x, y, WIDTH, HEIGHT, imagePath, scrollSpeed);
	}
}
