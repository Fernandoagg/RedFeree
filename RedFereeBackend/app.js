const express = require("express");
const cors = require("cors");
const db = require("./config/db"); // Asegúrate de que este archivo exista

const app = express();

// --- 1. MIDDLEWARES ---
app.use(cors());
app.use(express.json()); 

// --- 2. RUTAS ---
// Rutas importadas (Estilo HEAD - más ordenado)
const partidosRoutes = require('./routes/partidos');
const listaArbitrosRoutes = require('./routes/listaArbitros');

// --- 3. USAR RUTAS ---
// Aquí unimos lo que tenías tú con lo que venía de la otra rama
app.use('/api/partidos', partidosRoutes);
app.use('/api/lista-arbitros', listaArbitrosRoutes);

// La ruta de usuarios estaba en ambas versiones, la dejamos activa
app.use('/api/usuarios', require("./routes/usuarios")); 

// --- 4. ENCENDER SERVIDOR ---
app.listen(3000, () => {
    console.log("Servidor corriendo en puerto 3000");
});