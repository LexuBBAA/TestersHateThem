const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    let user_id = req.body.user_id;

    db.query(`SELECT a.aid, co.name, ci.name, a.address FROM addresses a JOIN cities ci ON (ci.cid = a.city_id) JOIN countries co ON (co.cid = a.country_id) WHERE a.uid = ${user_id}`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'could not get address!'
            });
            return;
        }

        res.json({
            success: true,
            data: results
        });
    });
});

router.post('/', (req, res) => {
    let user_id    = req.body.user_id;
    let city_id    = req.body.city_id;
    let country_id = req.body.country_id;
    let address    = req.body.address;

    db.query(`SELECT add_address('${user_id}','${city_id}','${country_id}','${address}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not add address!'
            });
            return;
        }

        res.json({
            success: true,
            message: 'address added!'
        })
        
    });
})

module.exports = router