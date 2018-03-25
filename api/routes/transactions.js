const express = require('express');

const db = require('../db.js');

const router = express.Router();

router.get('/', (req, res) => {
    let user_id = req.body.user_id;
    let user_type = req.body.user_type;
    
    db.query(`CALL get_transactions('${user_id}', '${user_type}')`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not get transactions!'
            });
            return;
        }

        res.json({
            success: true,
            data: results[0]
        })
        
    });
})

router.post('/', (req, res) => {
    let cid = '';
    let pid = '';

    if (req.body.user_type == 'provider') {
        cid = req.body.id;
        pid = req.body.user_id;
    } else {
        cid = req.body.user_id;
        pid = req.body.id;
    }

    let quantity = req.body.quantity;
    let due_date = req.body.due_date;
    
    db.query(`SELECT add_transaction('${cid}', '${pid}', '${quantity}', '${due_date}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not create transaction!'
            });
            return;
        }

        res.json({
            success: true,
            transaction_id: results[0].status
        })
        
    });
});

router.post('/:tid/approve', (req, res) => {
    let user_id = req.body.user_id;
    let tid = req.params.tid;
    
    db.query(`SELECT approve_transaction('${user_id}','${tid}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not approve transaction!'
            });
            return;
        }

        let status = results[0].status;

        if (status < 0) {
            res.json({
                success: false,
                message: 'could not approve transaction!'
            })
        } else {
            res.json({
                success: true,
                message: 'transaction approved!'
            })
        }
        
    });
});

router.post('/:tid/finish', (req, res) => {
    let user_id = req.body.user_id;
    let tid = req.params.tid;
    
    db.query(`SELECT finish_transaction('${user_id}','${tid}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not finish transaction!'
            });
            return;
        }

        let status = results[0].status;

        if (status < 0) {
            res.json({
                success: false,
                message: 'could not finish transaction!'
            })
        } else {
            res.json({
                success: true,
                message: 'transaction finished!'
            })
        }
        
    });
});

router.post('/:tid/rating', (req, res) => {
    let user_id = req.body.user_id;
    let tid = req.params.tid;
    let rating = req.body.rating;
    let message = req.body.message;
    
    db.query(`SELECT rate_transaction('${user_id}','${tid}', '${rating}', '${message}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not rate transaction!'
            });
            return;
        }

        let status = results[0].status;

        if (status < 0) {
            res.json({
                success: false,
                message: 'could not rate transaction!'
            })
        } else {
            res.json({
                success: true,
                message: 'transaction rated!'
            })
        }
        
    });
});

router.delete('/:tid', (req, res) => {
    let user_id = req.body.user_id;
    let tid = req.params.tid;
    
    db.query(`SELECT delete_transaction('${user_id}','${tid}') AS status`, (err, results) => {
        if (err) {
            console.log(err);
            res.json({
                success: false,
                message: 'could not delete transaction!'
            });
            return;
        }

        let status = results[0].status;

        if (status < 0) {
            res.json({                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
                success: false,
                message: 'could not delete transaction!'
            })
        } else {
            res.json({
                success: true,
                message: 'transaction deleted!'
            })
        }
        
    });
});

module.exports = router