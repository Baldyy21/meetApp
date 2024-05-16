const bodyParser = require('body-parser');
const express = require('express');
const cors = require('cors'); // Importa el middleware cors
const mongoose = require('mongoose');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/meetAppRest');

const MeetAppMarker = mongoose.model('meetAppMarker', mongoose.Schema({
    id: {
        type: Number,
        required: true
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
            description: marker.description || '', // Si description es null o undefined, asigna una cadena vacía
            latLng: marker.latLng // Parsea latLng a un objeto si es necesario
        }));
        res.status(200).json(markers);
    }).catch(error => {
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
        res.status(200).send(jsonResultado(result));
    }).catch(error => {
        if (req.body.latLng == null) {
            res.status(400).send(getErrorTemplate("latLng cant be empty!"));
        }
    });
});

app.put('/MeetAppMarker/:id', (req, res)=>{
    try{
        MeetAppMarker.findByIdAndUpdate(req.params.id, {
            $set:{
                description: req.body.description,
                latLng: req.body.latLng
        }
        }).then(result =>{
            res.status(200).send(jsonResultado(result));
        }).catch(error => {
            res.status(200).send(getErrorTemplate("MeetAppMarker not found"));
        });
    }catch(error){
        res.status(200).send(getErrorTemplate("Internal server error"));
    }

});

app.delete('/MeetAppMarker/:id', (req, res) => {
    MeetAppMarker.findByIdAndDelete({_id:req.params.id}).then(result=>{
        if(result){
            res.status(200).send(jsonResultado(result));
        }else{
            res.status(200).send(getErrorTemplate("MeetAppMarker not found"));
        }
    }).catch(error=>{
        res.status(200).send(getErrorTemplate("Internal server error"));
    });
});

app.listen(8081, () => {
    console.log('Server is running on port 8081');
});