const express = require('express');
const jwt = require('jsonwebtoken');

const router = express.Router();

const db = require('../db.js');

router.post('/', (req, res) => {
    let email       = req.body.email;
    let password    = req.body.password;
    let name        = req.body.user_name;
    let type        = req.body.user_type;
    let description = req.body.desc;
    let city_id     = req.body.city_id;
    let country_id  = req.body.country_id;
    let address     = req.body.address;
    let phone       = req.body.phone;


    db.query(`SELECT register('${email}', '${password}', '${name}', '${phone}', '${type}', '${description}', '${city_id}', '${country_id}', '${address}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'register failed!'
            });
            return;
        }

        if (results[0].status == 0) {
            res.json({
                success: false,
                message: 'invalid input!'
            })
        } else {
            let user_id = results[0].status;

            db.query(`SELECT user_type FROM users WHERE uid = '${user_id}'`, (err, results) => {
                if (err) {
                    console.log(err);
                    res.json({
                        success: false,
                        message: 'register failed!'
                    });
                    return;
                }
                
                let user_type = results[0].user_type;
                let token = jwt.sign({ user_id: user_id, user_type: user_type }, process.env.SECRET);

                res.json({
                    success: true,
                    message: 'registered successfully!',
                    user_type: user_type,
                    token: token
                })
            });        
        }
    });
})

module.exports = router