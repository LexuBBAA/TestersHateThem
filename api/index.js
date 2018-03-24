const express = require('express');
const bodyParser = require('body-parser');
const dotenv = require('dotenv').config();

// app modules
const db = require('./db.js');

// app variables
const app = express();

app.get('/', (req, res) => {
    res.send('it works');
})

app.listen(process.env.PORT, () => console.log(`server started on port ${process.env.PORT}`));