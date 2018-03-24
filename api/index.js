const express    = require('express');
const bodyParser = require('body-parser');
const dotenv     = require('dotenv').config();
const jwt        = require('jsonwebtoken');

// routes
const login     = require('./routes/login');
const register  = require('./routes/register');
const cities    = require('./routes/cities');
const countries = require('./routes/countries');
const address   = require('./routes/address');
const listing   = require('./routes/listing');
const profile   = require('./routes/profile');

// app variables
const app = express();

// middlewares
function logger(req, res, next) {
    console.log(`${req.ip}\t${req.method}\t${req.path}\t${req.body}`);
    next();
}

function requireToken(req, res, next) {
    let token = req.headers.token || '';

    if (token == '') {
        res.json({ 
            success: false,
            message: 'token header required!'
         });
    } else {
        jwt.verify(token, process.env.SECRET, (err, decoded) => {
            if (err) {
                res.json({
                    success: false,
                    message: 'invalid token!'
                });
                return;                
            }

            req.body.user_id = decoded.user_id;
            next();
        })
    }
}

function badRequest(req, res, next) {
    res.json({
        success: false,
        message: 'bad request!'
    })
}

app.use(logger);

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use('/login', login);
app.use('/register', register);
app.use('/cities', cities);
app.use('/countries', countries);

app.use(requireToken);

app.use('/address', address);
app.use('/profile', profile);

app.get('/', (req, res) => {
    res.json({
        user_id: req.body.user_id
    });
})

app.use(badRequest);

app.listen(process.env.PORT, () => console.log(`server started on port ${process.env.PORT}`));