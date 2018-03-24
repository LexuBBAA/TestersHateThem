const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.post('/', (req, res) => {
    let user_id    = req.body.user_id;
    let city_id    = req.body.city_id;
    let country_id = req.body.country_id;
    let address    = req.body.address;

    db.query(`SELECT add_address('${user_id}','${city_id}','${country_id}','${address}') AS status`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not add address!'
            });
            return;
        }

        res.json({
            success: true
        })
        
    });
})

module.exports = router