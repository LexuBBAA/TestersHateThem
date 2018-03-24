const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/:city_id', (req, res) => {
    let user_id = req.body.user_id;
    let city_id = req.params.city_id;

    db.query(`CALL listing('${user_id}','${city_id}',0)`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not get listing!'
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