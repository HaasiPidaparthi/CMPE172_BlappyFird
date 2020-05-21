package com.example.demo;

import java.util.Collection;

public interface RecordsDao {
	Collection<Records> getAllRecords();
	
	Records getRecordByScore(int score);
	
	Collection<Records> sortRecordByRank();
	
	void removeRecordByRank(int rank);
	
	void updateRecordByName(String name);

    void insertRecord(Records record);

}
