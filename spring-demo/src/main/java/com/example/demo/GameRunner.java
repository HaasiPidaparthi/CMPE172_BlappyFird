package com.example.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.BirdCharacter;
import com.example.demo.Cloud;
import com.example.demo.GameBoard;
import com.example.demo.Obstacle;

@Service
public class GameRunner extends JFrame implements KeyListener, ActionListener {
	private static final int DELAY = 15;
	private static Timer timer;
	private BirdCharacter c;
	private boolean started;
	private ArrayList<Obstacle> obs;
	private static GameBoard gb;
	private static JPanel charButtons;
    public ArrayList<String> s;
   

    private int time, highScore, driftSpeed, frequency, currentScore;

    @Autowired
	public GameRunner() {
		//initGame();	// Initialize the game
        frequency = 100;
        driftSpeed = 2;
        started = false;
        time = 0;
        highScore = 0;
        currentScore = 0;
//        score.add("jon 55");
//        score.add("tam 76");
        obs = new ArrayList<>();
        c = new BirdCharacter();
        gb = new GameBoard(c, this);
        timer = new Timer(DELAY, this);
        timer.start();
        charButtons = gb.addCharacterButtons();
		// Add the GameBoard and character buttons to this JFrame
		add(gb);
		add(charButtons, BorderLayout.SOUTH);

		// Complete other setup
		addKeyListener(this);
		setFocusable(true);
		setVisible(true);
		setTitle(" Blappy Fird");
		setSize(GameBoard.WIDTH, GameBoard.HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

	// Helper method to initialize values for instance variables
	private void initGame() {
		frequency = 100;
		driftSpeed = 2;
		started = false;
		time = 0;
		highScore = 0;
        s.add("jon 55");
        s.add("tam 76");
		obs = new ArrayList<>();
		c = new BirdCharacter();
		gb = new GameBoard(c, this);
		timer = new Timer(DELAY, this);
		timer.start();
		charButtons = gb.addCharacterButtons();
	}

    public ArrayList<String> scores() {
        return s;
    }

	// Marks the game as started
	public boolean started() {
		return started;
	}
	public int gethScore(){ return highScore; }
    public int getcScore(){ return currentScore; }

	// Gets the score as a function of time
	public int getScore() {
		return time/10;
	}

	// Gets the list of obstacles
	public ArrayList<Obstacle> getObstacles() {
		return obs;
	}
	
	// convert Date format from util to sql
	private java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

	// Utility method to end the game
	public void endGame() {
		//EventQueue.invokeLater(() -> { });
	
		int score = getScore();

		String name = JOptionPane.showInputDialog("Good run!\n"+"Score: " + score + "\nEnter you name: ");
		
		if (name != null) {
			System.out.println("Input: " + name);
			String player_name = name.substring(0,3);
			
			java.util.Date Date = new java.util.Date(); 
			java.sql.Date date = convertUtilToSql(Date);
		     		
			System.out.println("Date: " + date);
			
			Collection<Records> recordList = sortRecordByRank();
			//Iterator<Records> iterator = recordList.iterator();
			int rank = 11; 
			boolean updated = false;
			
			System.out.println("CURR score: " + score);
			
			for (Records rec : recordList) {
		        int prevScore = rec.getScore();
		        System.out.println("PREV record score: " + prevScore);
				if (prevScore < score && updated == false) {
					rank = rec.getRank();
					System.out.println("RANK recorded");
					System.out.println("UPDATING 1st record");
					updateRecordByName(rec.getName());
					updated = true;
				} else if (updated == true) {
					System.out.println("UPDATING record ranks");
					updateRecordByName(rec.getName());
				} else {}
				
		    }
			
	        insertRecord(new Records(player_name, rank, score, date));
			System.out.println("Record inserted");
	        removeRecordByRank(11);
	        System.out.println("Removed Last Record");
			
			sortRecordByRank();
			System.out.println("SORTED");
		}	
		
		this.gameReset();
	}

	// Helper method to reset the game
	private void gameReset() {
		started = false;
		c.initialize();
		time = 0;
		frequency = 100;
		obs.clear();
		driftSpeed = 2;
		charButtons.setVisible(true);
	}

	// Helper method to calculate Cloud height
	private int calcCloudHeight() {
		return (int)(Math.random()*(GameBoard.HEIGHT-Cloud.HEIGHT));
	}

	// Adds new Clouds just off-screen to the right
	private void addClouds() {
		// Use time to regularly add in clouds
		// Frequency is a variable so that it can be increased as time goes on
		if (time % frequency == 0) {
			int h1 = calcCloudHeight();
			Obstacle o1 = new Cloud(GameBoard.WIDTH, h1, driftSpeed);
			int h2 = calcCloudHeight();
			// Don't want overlapping clouds, so make sure the height is different
			while (!(h2 >= (h1+Cloud.HEIGHT) || h1 >= (h2+Cloud.HEIGHT)))
				h2 = calcCloudHeight();
			Obstacle o2 = new Cloud(GameBoard.WIDTH, h2, driftSpeed);
			obs.add(o1);
			obs.add(o2);
		}
	}

	// Determines whether the character made contact with an obstacle
	private boolean charContact(Obstacle obs) {
		return (c.getBounds().intersects(obs.getBounds()) || c.offscreen());
	}

	// Moves the obstacles across the screen, creates new ones, removes old ones
	private void moveObstacles() {
		ArrayList<Obstacle> removeObstacles = new ArrayList<>(); // A list of obstacles to removed

		// Make each of the Clouds drift across the screen
		for (int i = 0; i < obs.size(); ++i) {
			Cloud cloud = (Cloud)obs.get(i);
			cloud.drift();

			// Clouds that go off-screen are added to the removal list
			if(c.offscreen()) removeObstacles.add(cloud);

			if (charContact(cloud)) // Check if the Character touched a Cloud or went off-screen
				endGame();
		}
		obs.removeAll(removeObstacles); // Remove the off-screen obstacles
	}

	@Override
	// This method is called every DELAY ms by the timer.
    public void actionPerformed(ActionEvent e) {
		gb.repaint(); // Calls the paintComponent method of gb to update visual changes
		if (started) {	// If game hasn't started, don't do anything yet
			addClouds();
			c.move();
			moveObstacles();

			// Increase the drifting speed and spawn frequency
			// of obstacles every 30 seconds
			// Math: time variable increments every 15 ms
			// -> 15 ms * 2000 = 30000 ms = 30 seconds
			if (++time % 2000 == 0) {
				driftSpeed++;
				frequency -= 10;
			}

		}else {
			//System.out.println(this.gethScore());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// If the up arrow is pressed
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			c.jump();						// Makes the character jump
			started = true;					// Starts the game
			charButtons.setVisible(false); 	// Hides the character buttons
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Do nothing
		// Just here to satisfy the implemented KeyListener
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
		// Just here to satisfy the implemented KeyListener
	}

	public void playIt(){
		//SpringApplication.run(GameRunner.class, args);
	}
	
	
	
	// Records Service
    @Autowired
	@Qualifier("mysql")
    
    private RecordsDao recordsDao;
    
	public Collection<Records> getAllRecords(){
        return this.recordsDao.getAllRecords();
    }

    public Records getRecordByScore(int score){
        return this.recordsDao.getRecordByScore(score);
    }
    
    public Collection<Records> sortRecordByRank() {
    	return this.recordsDao.sortRecordByRank();
    }

    public void removeRecordByRank(int rank) {
        this.recordsDao.removeRecordByRank(rank);
    }
    
    public void updateRecordByName(String name){
        this.recordsDao.updateRecordByName(name);
    }

    public void insertRecord(Records record){
        this.recordsDao.insertRecord(record);
    }
}
