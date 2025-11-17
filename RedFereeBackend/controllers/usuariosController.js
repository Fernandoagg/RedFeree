const db = require("../models/db");

exports.registerUser = (req, res) => {
    const { nombre, correo, password } = req.body;

    const query = "INSERT INTO usuarios (nombre, correo, password) VALUES (?, ?, ?)";

    db.query(query, [nombre, correo, password], (err, result) => {
        if (err) return res.status(500).json({ error: err });

        res.json({ message: "Usuario registrado correctamente", id: result.insertId });
    });
};
