const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    let user_id    = req.body.user_id;

    db.query(`SELECT listing('${user_id}') AS status`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not add address!'
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