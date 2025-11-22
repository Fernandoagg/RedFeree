const express = require('express');
const router = express.Router();
const db = require('../config/db');

// Ruta POST: http://localhost:3000/api/partidos
// Esta ruta RECIBE datos y los guarda
router.post('/', (req, res) => {
    const { nombreArbitro, deporte, precio } = req.body;

    const sql = 'INSERT INTO partidos (arbitro_nombre, deporte, precio) VALUES (?, ?, ?)';
    
    db.query(sql, [nombreArbitro, deporte, precio], (err, result) => {
        if (err) {
            console.error('Error al guardar partido:', err);
            return res.status(500).send('Error al guardar en base de datos');
        }
        res.json({ message: 'Partido agendado exitosamente', id: result.insertId });
    });
});

module.exports = router;