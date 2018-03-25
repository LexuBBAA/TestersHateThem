const mysql = require('mysql');

const dbConfig = {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: process.env.DB_NAME
}
let connection = {}

function handleDisconnect() {
    connection = mysql.createConnection(dbConfig);  
    connection.connect( function onConnect(err) {   
        if (err) {                                  
            console.log('error when connecting to db:', err);
            setTimeout(handleDisconnect, 10000);    
        }                                        
        console.log('established db connection');   
    });                                             
                                                    
    connection.on('error', function onError(err) {
        console.log('db error', err);
        if (err.code == 'PROTOCOL_CONNECTION_LOST') {   
            handleDisconnect();                         
        } else {                                        
            throw err;                                  
        }
    });
}
handleDisconnect();

module.exports = connection;