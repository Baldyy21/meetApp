const bodyParser = require('body-parser');
const express = require('express');
const mongoose = require('mongoose');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/MeetApp');

const MeetAppMarker = mongoose.model('meetappmarkers', mongoose.Schema({
    description: {
        type: String,
        required: true,
        trim: true
    },
    latLang: {
        type: String,
        required: true
        }
}));

let app = express();
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

app.get('/meetAppMarkers', (req, res) => {
    MeetAppMarker.find().then(result => {
        res.status(200).send(jsonResultado(result));
    }).catch(error => {
        res.status(500).send(getErrorTemplate("Internal server error"));
    });
});


app.post('/meetAppMarker', (req, res) => {
    let marker = new MeetAppMarker({
        description: req.body.description,
        latLang: req.body.latLang
    });

    marker.save().then(result => {
        res.status(200).send(jsonResultado(result));
    }).catch(error => {
        if (req.body.description == null) {
            res.status(400).send(getErrorTemplate("Description cant be empty!"));
        } else if (req.body.latLang == null) {
            res.status(400).send(getErrorTemplate("Wrong latLang!"));
        }
    });
});

app.put('/tasks/:id', (req, res)=>{
    try{
        Task.findByIdAndUpdate(req.params.id, {
            $set:{
                description: req.body.description,
                latLang: req.body.latLang
        }
        }).then(result =>{
            res.status(200).send(jsonResultado(result));
        }).catch(error => {
            res.status(200).send(getErrorTemplate("Task not found"));
        });
    }catch(error){
        res.status(200).send(getErrorTemplate("Internal server error"));
    }

});

app.delete('/meetAppMarkers/:id', (req, res) => {
    MeetAppMarker.findByIdAndDelete({_id:req.params.id}).then(result=>{
        if(result){
            res.status(200).send(jsonResultado(result));
        }else{
            res.status(200).send(getErrorTemplate("Task not found"));
        }
    }).catch(error=>{
        res.status(200).send(getErrorTemplate("Internal server error"));
    });
});

app.listen(8082, () => {
    console.log('Server is running on port 8082');
});