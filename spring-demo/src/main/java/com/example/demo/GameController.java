package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play")
public class GameController {
	
	@Autowired	
	private GameRunner gameRunner;
    //Collection<Records> records;

    public GameController(GameRunner gameRunner) {

        this.gameRunner = gameRunner;
        //this.records = gameRunner.getAllRecords();
        
        //System.out.println(g.scores());
    }
 
    
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Records> sortRecordsByRank(){
        return gameRunner.sortRecordByRank();
    }
    
    @RequestMapping(value = "/{score}", method = RequestMethod.GET)
	public Records getRecordByScore(@PathVariable("score") int score){
        return gameRunner.getRecordByScore(score);
    }
    
    @RequestMapping(value = "/{rank}", method = RequestMethod.DELETE)
	public void removeRecordByRank(@PathVariable("rank")int rank) {
		gameRunner.removeRecordByRank(rank);
    }
    
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateRecordByName(@RequestBody String name){
    	gameRunner.updateRecordByName(name);
    }
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void insertRecord(@RequestBody Records record){
		gameRunner.insertRecord(record);
    }

//    @RequestMapping(value = "/scoreboard")
//    public String scoreboard() {
//        return "{name: ,current score: " + gameRunner.getcScore() + ", high score: " + gameRunner.gethScore() + "}";
//    }
	
	
}
