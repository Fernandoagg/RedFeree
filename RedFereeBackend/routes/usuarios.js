const express = require("express");
const router = express.Router();
const db = require("../config/db");

// REGISTER
router.post("/register", (req, res) => {
    const { nombre, email, password, tipo } = req.body;

    const sql = "INSERT INTO usuarios (nombre, email, password, tipo) VALUES (?, ?, ?, ?)";
    db.query(sql, [nombre, email, password, tipo], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({ status: "ok", message: "Usuario creado" });
    });
});

// LOGIN
router.post("/login", (req, res) => {
    const { email, password } = req.body;

    const sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
    db.query(sql, [email, password], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        if (result.length === 0) {
            return res.status(401).json({ error: "Credenciales incorrectas" });
        }

        res.json({ status: "ok", usuario: result[0] });
    });
});

module.exports = router;
