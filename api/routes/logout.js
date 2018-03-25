const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/:device_id', (req, res) => {
    let device_id = req.params.device_id;
    
    db.query(``, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not log out!'
            });
            return;
        }

        res.json({
            success: true,
            message: 'logged out!'
        })
        
    });
})

module.exports = router