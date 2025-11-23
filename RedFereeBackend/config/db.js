const mysql = require("mysql2");

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "Hembi1104",      // tu contraseña de MySQL si tienes
    database: "redferee"
});

db.connect(err => {
    if (err) {
        console.log("Error al conectar a MySQL:", err.message);
        return;
    }
    console.log("MySQL conectado.");
});

module.exports = db;
