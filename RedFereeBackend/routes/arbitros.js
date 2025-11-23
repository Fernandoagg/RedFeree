const express = require("express");
const router = express.Router();
const db = require("../config/db");

// GET: Obtener lista de árbitros para el Spinner
router.get("/desplegar", (req, res) => {
    const sql = "SELECT id, nombre FROM redferee.usuarios WHERE tipo = 'arbitro'"; 
    
    db.query(sql, (err, results) => {
        if (err) {
            return res.status(500).json({ error: "Error al obtener árbitros" });
        }
        res.json(results);
    });
});

module.exports = router;