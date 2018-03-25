const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    db.query(`SELECT uid, email, name, phone, description, score FROM users WHERE user_type='provider' ORDER BY score DESC LIMIT 6`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not get rankings!'
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