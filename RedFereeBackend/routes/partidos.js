const express = require('express');
const router = express.Router();
//<<<<<<< HEAD
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

//=======
const db = require('../config/db'); // Asegúrate de que esta ruta sea correcta según tu estructura

// GET: Obtener todos los partidos
// Ruta final: http://localhost:3000/api/partidos
router.get('/', (req, res) => {
    // Hacemos un JOIN básico para intentar sacar el nombre del árbitro si existe
    // Si tu tabla de usuarios tiene 'nombre', esto funcionará.
    const sql = `
        SELECT p.*, u.nombre as nombreArbitro 
        FROM partidos p 
        LEFT JOIN usuarios u ON p.arbitroId = u.id
    `;

    db.query(sql, (err, results) => {
        if (err) {
            console.error("Error al obtener partidos:", err);
            return res.status(500).send("Error en el servidor");
        }
        res.json(results);
    });
});

// --- ESTA ES LA LÍNEA QUE TE FALTABA ---
//>>>>>>> c55b8a2e389b5f9856dab1b3051afb6be445684c
module.exports = router;