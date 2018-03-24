const express = require('express');
const jwt = require('jsonwebtoken');

const db = require('../db.js');

const router = express.Router();

router.post('/', (req, res) => {
    let email = req.body.email;
    let password = req.body.password;

    db.query(`SELECT login('${email}', '${password}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
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
            
            db.query(`SELECT user_type FROM users WHERE uid = '${user_id}'`, (err, results) => {
                if (err) {
                    console.log(err);
                    res.json({
                        success: false,
                        message: 'login failed!'
                    });
                    return;
                }
                
                let user_type = results[0].user_type;
                let token = jwt.sign({ user_id: user_id, user_type: user_type }, process.env.SECRET);

                res.json({
                    success: true,
                    message: 'logged in successfully!',
                    user_type: user_type,
                    token: token
                });
            });

        }
        
    });
})

module.exports = router