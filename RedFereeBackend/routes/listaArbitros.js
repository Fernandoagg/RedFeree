const express = require('express');
const router = express.Router();
const db = require('../config/db'); // Asegúrate que esta ruta a tu db.js sea correcta

// Ruta GET: http://localhost:3000/api/lista-arbitros
router.get('/', (req, res) => {
    // 1. Seleccionamos solo los datos que nos sirven de tu tabla 'usuarios'
    // IMPORTANTE: Asumo que tu tabla se llama 'usuarios' (por la columna password/email).
    // Si se llama diferente, cambia donde dice FROM usuarios.
    const sql = 'SELECT id, nombre, ratingPromedio, totalResenas FROM usuarios';
    
    db.query(sql, (err, results) => {
        if (err) {
            console.error('Error en la base de datos:', err);
            return res.status(500).send('Error del servidor');
        }

        // 2. "Maquillamos" los datos
        // Como tu tabla no tiene precio ni lider, se los inventamos aquí para que Android no falle.
        const arbitrosListos = results.map(item => ({
            id: item.id,
            nombre: item.nombre,
            // Si el usuario no tiene rating, le ponemos 0
            reseñasCount: item.totalResenas || 0, 
            rating: item.ratingPromedio || 0.0,
            // DATOS FALSOS (Hardcoded) para cumplir con el diseño:
            lider: item.nombre, // Usamos el mismo nombre como líder por ahora
            precio: "$ 1,500.00 MXN" // Precio fijo para todos
        }));

        res.json(arbitrosListos);
    });
});

module.exports = router;