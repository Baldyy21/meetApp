const bodyParser = require('body-parser');
const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/meetAppRest').catch(error => {
    console.error("Database connection error:", error);
});

const MeetAppMarker = mongoose.model('meetAppMarker', mongoose.Schema({
    
    description: {
        type: String,
        trim: true
    },
    latitud: {
        type: Number,
        required: true,
        trim: true,
    },
    longitud: {
        type: Number,
        required: true,
        trim: true,
    }
}));

let app = express();
app.use(cors());
app.use(bodyParser.json());

function jsonResultado(result) {
    return {
        "ok": true,
        "result": result
    };
}

function getErrorTemplate(error) {
    return {
        "ok": false,
        "error": error
    };
}

app.get('/MeetAppMarker', (req, res) => {
    MeetAppMarker.find().then(result => {
        const markers = result.map(marker => ({
            description: marker.description || '',
            latitud: marker.latitud,
            longitud: marker.longitud
        }));
        console.log("Markers", result);
        res.status(200).json(markers);
    }).catch(error => {
        console.error("Error fetching markers:", error);
        res.status(500).json(getErrorTemplate("Internal server error"));
    });
});

app.post('/MeetAppMarker', (req, res) => {
    MeetAppMarker.findOne({ latitud: req.body.latitud, longitud: req.body.longitud }).then(existingMarker => {
        if (existingMarker) {
            console.log("Marker with the same coordinates already exists, ignoring insertion.");
            res.status(200).send(jsonResultado(existingMarker));
        } else {
            let meetAppMarker = new MeetAppMarker({
                description: req.body.description,
                latitud: req.body.latitud,
                longitud: req.body.longitud
            });

            meetAppMarker.save().then(result => {
                console.log("Marker added:", result);
                res.status(200).send(jsonResultado(result));
            }).catch(error => {
                console.error("Error adding marker:", error);
                res.status(500).send(getErrorTemplate("Internal server error"));
            });
        }
    }).catch(error => {
        console.error("Error checking existing markers:", error);
        res.status(500).send(getErrorTemplate("Internal server error"));
    });
});


app.delete('/MeetAppMarker/:latitude/:longitude', (req, res) => {
    var latitude = parseFloat(req.params.latitude);
    var longitude = parseFloat(req.params.longitude);
    
    MeetAppMarker.deleteOne({ latitud: latitude, longitud: longitude }).then(result => {
        if (result.deletedCount > 0) {
            console.log("Marker deleted at:", latitude, longitude);
            res.status(200).send(jsonResultado("MeetAppMarker deleted successfully"));
        } else {
            res.status(404).send(getErrorTemplate("MeetAppMarker not found"));
        }
    }).catch(error => {
        console.error("Error deleting marker:", error);
        res.status(500).send(getErrorTemplate("Internal server error"));
    });
});

app.listen(8081, () => {
    console.log('Server is running on port 8081');
});
