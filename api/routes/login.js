const express = require('express');
const jwt = require('jsonwebtoken');

const db = require('../db.js');

const router = express.Router();

router.post('/', (req, res) => {
    let email = req.body.email;
    let password = req.body.password;

    db.query(`SELECT login('${email}', '${password}') AS status`, (err, results) => {
        if (err) {
            res.json({
                success: false,
                message: 'login failed!'
            });
            return;
        }

        if (results[0].status == 0) {
            res.json({
                success: false,
                message: 'invalid credentials!'
            })
        } else {
            let user_id = results[0].status;
            let token = jwt.sign({ user_id: user_id }, process.env.SECRET);

            res.json({
                success: true,
                token: token
            })
        }
        
    });
})

module.exports = router