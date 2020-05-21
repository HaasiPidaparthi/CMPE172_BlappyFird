package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("mysql")
public class MySQLRecordsDao implements RecordsDao {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private static class RecordsRowMapper implements RowMapper<Records> {

        @Override
        public Records mapRow(ResultSet resultSet, int i) throws SQLException {
        	Records record = new Records();
        	record.setName(resultSet.getString("name"));
        	record.setRank(resultSet.getInt("rank"));
        	record.setScore(resultSet.getInt("score"));
            record.setDate(resultSet.getDate("date"));
            return record;
        }
    }
	
	@Override
    public Collection<Records> getAllRecords() {
        // SELECT column_name(s) FROM table_name
        final String sql = "SELECT rank, name, score, date FROM Leaderboard";
        List<Records> records = jdbcTemplate.query(sql, new RecordsRowMapper());
        return records;
    }
	
	@Override
    public Records getRecordByScore(int score) {
        // SELECT column_name(s) FROM table_name where column = value
        final String sql = "SELECT rank, name, score, date FROM Leaderboard where score = ?";
        Records record = jdbcTemplate.queryForObject(sql, new RecordsRowMapper(), score);
        return record;
    }
	
	@Override 
	public Collection<Records> sortRecordByRank() {
		// SELECT column_name(s) FROM table_name ORDER BY rank ASC
        final String sql = "SELECT rank, name, score, date FROM Leaderboard ORDER BY rank ASC";
        List<Records> records = jdbcTemplate.query(sql, new RecordsRowMapper());
        return records;
	}

    @Override
    public void removeRecordByRank(int rank) {
        // DELETE FROM table_name
        // WHERE some_column = some_value
        final String sql = "DELETE FROM Leaderboard WHERE rank = ?";
        jdbcTemplate.update(sql, rank);
    }

    @Override
    public void updateRecordByName(String name) {
        // UPDATE table_name
        // SET column1=value, column2=value2,...
        // WHERE some_column=some_value
        final String sql = "UPDATE Leaderboard SET rank = rank+1 WHERE name = ?";
//        final String name = record.getName();
//        final int rank = record.getRank();
//        final int score = record.getScore();
//        final Date date = record.getDate();
        jdbcTemplate.update(sql, name);
    }
    
	@Override
	public void insertRecord(Records record) {
		// INSERT INTO table_name
        // VALUES (column1=value, column2=value2,...)
		
        final String sql = "INSERT INTO Leaderboard (name, rank, score, date) VALUES (?, ?, ?, ?)";
        final String name = record.getName();
        final int rank = record.getRank();
        final int score = record.getScore();
        final Date date = record.getDate();   
        jdbcTemplate.update(sql, new Object[]{name, rank, score, date});
	}

	
}
