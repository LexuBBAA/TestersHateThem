const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    let user_id = req.body.user_id;

    db.query(`SELECT uid, email, name, phone, description, score FROM users WHERE uid = ${user_id}`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not get profile!'
            });
            return;
        }

        res.json({
            success: true,
            data: results[0]
        })
        
    });
})

router.get('/:id', (req, res) => {
    let id = req.params.id;

    db.query(`SELECT uid, email, name, phone, description, score FROM users WHERE uid = ${id}`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not get profile!'
            });
            return;
        }

        res.json({
            success: true,
            data: results[0]
        })
        
    });
})

module.exports = router