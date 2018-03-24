const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    let user_id = req.body.user_id;

    db.query(`SELECT name, description FROM users WHERE uid = ${user_id}`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not get profile!'
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