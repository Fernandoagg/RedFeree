const express = require('express');
const router = express.Router();
// Dejamos solo una importación de la base de datos
const db = require('../config/db');

// ==========================================
// RUTA 1: AGENDAR PARTIDO (POST) - (Tu código)
// URL: http://localhost:3000/api/partidos
// ==========================================
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

// ==========================================
// RUTA 2: VER HISTORIAL (GET) - (Código del compañero)
// URL: http://localhost:3000/api/partidos
// ==========================================
router.get('/', (req, res) => {
    // OJO: Tu compañero está haciendo un JOIN con 'arbitroId'.
    // Asegúrate de que tu tabla 'partidos' tenga esa columna o si usa 'arbitro_nombre'.
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

// Importante: Esto siempre va al final
module.exports = router;