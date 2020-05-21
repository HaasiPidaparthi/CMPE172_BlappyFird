//package com.example.demo;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//
////@Service
//public class RecordsService {
//	//@Autowired
//	//@Qualifier("mysql")
//    private RecordsDao recordsDao;
//	
//	public Collection<Records> getAllRecords(){
//        return this.recordsDao.getAllRecords();
//    }
//
//    public Records getRecordByName(String name){
//        return this.recordsDao.getRecordByName(name);
//    }
//
//    public void removeRecordByRank(String name) {
//        this.recordsDao.removeRecordByRank(name);
//    }
//
//    public void insertRecord(Records record){
//        this.recordsDao.insertRecord(record);
//    }
//
//    
//    // Records Service
//    
//}
