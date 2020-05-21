const mysql = require('mysql');
const http = require('http');

// Create the leaderboard table and populate it with player data.
function makeTable(result) {
  // Create an HTTP server using the 'http' module
  http.createServer((req, res) => {
    // Define the HTTP response header.
    // The content type must be text/html because we are creating
    // the table and displaying it in html.
    res.writeHead(200, { 'Content-Type': 'text/html' });
    // Each write() method adds content to the response message
    // The title:
    res.write("<h2>Blappy Fird Leaderboard</h2>");
    // In-line CSS styling:
    res.write(`<style>
    table,th,td {
      border : 1px solid black;
      border-collapse: collapse;
    }
    th,td {
      padding: 5px;
    }
    </style>`);
    // Leaderboard table header row:
    res.write(`
      <table>
        <tr>
          <th>Rank</th>
          <th>Name</th>
          <th>Score</th>
          <th>Date</th>
        </tr>`);
    /* Read the data from the database, one by one
       This data is stored as an array of objects in the result variable */
    for (i = 1; i <= result.length; i++) {
      /* We want to display the scores by rank, from 1st to 10th.
         The scores may not be stored in order in the database
         so we want to manually insert them into the leaderboard
         table by rank */
      var index = result.findIndex(entry => entry.player_rank === i);
      /* The date read from the database includes the time, which is more
         information than we need. Use substring to remove the time and keep
         the date */
      var date = result[index].date_played;
      date = date.toString().substring(0, 15);
      res.write(`
        <tr>
          <td>${result[index].player_rank}</td>
          <td>${result[index].player_name}</td>
          <td>${result[index].player_score}</td>
          <td>${date}</td>
        </tr>`);
    }
    res.write("</table>");
    res.end(); // end() marks the end of the message and sends it
  }).listen(8080); // The HTTP server connects with port 8080
}

console.log('server running on port 8080');

// Read the leaderboard scores from the MySQL database
var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "mypass",
  database: "leaderboard"
});

con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
  // We want all the scores from the database
  var sql = "SELECT * FROM leaderboard";
  con.query(sql, function (err, result, fields) {
    if (err) throw err;
    makeTable(result);  // After successful query, create the leaderboard table
  });
});
