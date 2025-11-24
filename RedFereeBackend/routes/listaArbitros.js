const express = require('express');
const router = express.Router();
const db = require('../config/db');

// Ruta GET: http://localhost:3000/api/lista-arbitros
router.get('/', (req, res) => {
    // 1. Seleccionamos los datos REALES, incluyendo el nuevo precioBase
    // Agregamos "WHERE tipo = 'arbitro'" para que no salgan los usuarios clientes
    const sql = `
        SELECT id, nombre, ratingPromedio, totalResenas, precioBase 
        FROM usuarios 
        WHERE tipo = 'arbitro'
    `;
    
    db.query(sql, (err, results) => {
        if (err) {
            console.error('Error en la base de datos:', err);
            return res.status(500).send('Error del servidor');
        }

        // 2. Formateamos los datos para la App
        const arbitrosListos = results.map(item => ({
            id: item.id,
            nombre: item.nombre,
            reseñasCount: item.totalResenas || 0,
            rating: item.ratingPromedio || 0.0,
            lider: item.nombre, // Por ahora el líder es el mismo nombre
            
            // AQUÍ ESTÁ EL CAMBIO DE PRECIO:
            // Si tiene precioBase en la BD, lo usamos. Si no, ponemos uno default.
            precio: item.precioBase 
                ? `$ ${item.precioBase} MXN` 
                : "$ 500.00 MXN"
        }));

        res.json(arbitrosListos);
    });
});

module.exports = router;