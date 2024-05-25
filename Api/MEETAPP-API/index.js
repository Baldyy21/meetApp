const bodyParser = require('body-parser');
const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/meetAppRest',).catch(error => {
    console.error("Database connection error:", error);
});

const MeetAppMarker = mongoose.model('meetAppMarker', mongoose.Schema({
    id: {
        type: Number,
        required: true,
        unique: true
    },
    description: {
        type: String,
        trim: true
    },
    latLng: {
        type: String,
        required: true,
        trim: true
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
            id: marker.id,
            description: marker.description || '',
            latLng: marker.latLng
        }));
        console.log("Markers", result);
        res.status(200).json(markers);
    }).catch(error => {
        console.error("Error fetching markers:", error);
        res.status(500).json(getErrorTemplate("Internal server error"));
    });
});

app.post('/MeetAppMarker', (req, res) => {
    let meetAppMarker = new MeetAppMarker({
        id: req.body.id,
        description: req.body.description,
        latLng: req.body.latLng
    });

    meetAppMarker.save().then(result => {
        console.log("Marker added:", result);
        res.status(200).send(jsonResultado(result));
    }).catch(error => {
        console.error("Error adding marker:", error);
        if (req.body.latLng == null) {
            res.status(400).send(getErrorTemplate("latLng can't be empty!"));
        } else {
            res.status(500).send(getErrorTemplate("Internal server error"));
        }
    });
});

app.put('/MeetAppMarker/:id', (req, res) => {
    MeetAppMarker.updateOne({ id: req.params.id }, {
        $set: {
            description: req.body.description,
            latLng: req.body.latLng
        }
    }).then(result => {
        if (result.nModified > 0) {
            res.status(200).send(jsonResultado(result));
        } else {
            res.status(404).send(getErrorTemplate("MeetAppMarker not found"));
        }
    }).catch(error => {
        console.error("Error updating marker:", error);
        res.status(500).send(getErrorTemplate("Internal server error"));
    });
});

app.delete('/MeetAppMarker/:id', (req, res) => {
    MeetAppMarker.deleteOne({ id: req.params.id }).then(result => {
        if (result.deletedCount > 0) {
            res.status(200).send(jsonResultado(result));
            console.log("Marker deleted:", req.params.id);
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
