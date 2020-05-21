package com.example.demo;

import java.sql.Date;

public class Records {
	private String name;
	private int rank;
	private int score;
	private Date date;
	
	public Records() {}
	public Records(String name, int score, Date date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}
	
	public Records(String name, int rank, int score, Date date) {
		this.name = name;
		this.rank = rank;
		this.score = score;
		this.date = date;
	}
	
	public void setRank(int rank) { this.rank = rank; }
	public void setScore(int score) { this.score = score; }
	public void setName(String name) { this.name = name; }
	public void setDate(Date date) { this.date = date; }
	
	public int getRank() { return rank; }
	public int getScore() { return score; }
	public String getName() { return name; }
	public Date getDate(){ return date; }
	
	@Override
	public String toString() {
		return getName() + " " + getScore();
	}
}
