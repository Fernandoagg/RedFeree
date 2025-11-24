const express = require("express");
const cors = require("cors");
const db = require("./config/db");

const app = express();

// --- 1. MIDDLEWARES (PRIMERO QUE NADA) ---
// Estas dos líneas son OBLIGATORIAS antes de las rutas para poder leer datos
app.use(cors());
app.use(express.json()); 

// --- 2. IMPORTAR RUTAS ---
const partidosRoutes = require('./routes/partidos');
const listaArbitrosRoutes = require('./routes/listaArbitros');
// (Aquí irían tus otras rutas de usuarios si las tienes)

// --- 3. USAR RUTAS ---
app.use('/api/partidos', partidosRoutes);
app.use('/api/lista-arbitros', listaArbitrosRoutes);
app.use('/api/usuarios', require("./routes/usuarios")); // Si tenías esta

// --- 4. ENCENDER SERVIDOR (AL FINAL) ---
app.listen(3000, () => {
    console.log("Servidor corriendo en puerto 3000");
});