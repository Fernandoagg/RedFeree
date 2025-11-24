const express = require('express');
const router = express.Router();
const db = require('../config/db');

// ==========================================
// RUTA 1: AGENDAR PARTIDO (POST)
// ==========================================
router.post('/', (req, res) => {
    const { nombreArbitro, deporte, precio } = req.body;

    // Guardamos en las columnas de texto que creamos
    const sql = 'INSERT INTO partidos (arbitro_nombre, deporte, precio) VALUES (?, ?, ?)';
    
    db.query(sql, [nombreArbitro, deporte, precio], (err, result) => {
        if (err) {
            console.error('Error al guardar partido:', err);
            return res.status(500).send('Error al guardar en base de datos');
        }
        res.json({ message: 'Partido agendado exitosamente', id: result.insertId });
    });
});

// ==========================================
// RUTA 2: VER HISTORIAL (GET) - ¡CORREGIDO!
// ==========================================
router.get('/', (req, res) => {
    // AQUÍ ESTÁ EL TRUCO:
    // Usamos "AS" para cambiar el nombre de la columna de SQL
    // al nombre exacto que espera tu variable en Kotlin.
    //
    // base de datos (arbitro_nombre) -> Kotlin espera (nombreArbitro)
    // base de datos (precio)         -> Kotlin espera (costo)

    const sql = `
        SELECT 
            id, 
            fecha, 
            ubicacion, 
            deporte, 
            estado,
            arbitro_nombre AS nombreArbitro, 
            precio AS costo
        FROM partidos
        ORDER BY id DESC
    `;

    db.query(sql, (err, results) => {
        if (err) {
            console.error("Error al obtener partidos:", err);
            return res.status(500).send("Error en el servidor");
        }
        // Enviamos los resultados con los nombres corregidos
        res.json(results);
    });
});

module.exports = router;