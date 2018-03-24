const express = require('express');
const jwt = require('jsonwebtoken');

const router = express.Router();

const db = require('../db.js');

router.post('/', (req, res) => {
    let email =       req.body.email;
    let password =    req.body.password;
    let name =        req.body.name;
    let type =        req.body.type;
    let description = req.body.description;
    let city_id =     req.body.city_id;
    let country_id =  req.body.country_id;
    let address =     req.body.address;


    db.query(`SELECT register('${email}', '${password}', '${name}', '${type}', '${description}', '${city_id}', '${country_id}', '${address}') AS status`, (err, results) => {
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
            let token = jwt.sign({ user_id: user_id }, process.env.SECRET);

            res.json({
                success: true,
                message: 'registered successfully!',
                token: token
            })
        }
    });
})

module.exports = router