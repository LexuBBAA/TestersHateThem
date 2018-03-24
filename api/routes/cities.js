const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/:id', (req, res) => {
    let country_id = req.params.id;

    db.query(`SELECT * FROM cities WHERE country_id = ${country_id}`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not retrieve cities!'
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