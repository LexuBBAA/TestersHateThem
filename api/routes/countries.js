const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    db.query(`SELECT * FROM countries`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not retrieve countries!'
            });
            return;
        }

        res.json({
            success: true,
            data: results
        })
        
    });
})

module.exports = router