const express = require('express');
const router = express.Router();
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
module.exports = router;