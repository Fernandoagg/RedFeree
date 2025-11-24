const express = require("express");
const cors = require("cors");
const db = require("./config/db"); // Asegúrate de que este archivo exista

const app = express();

// --- 1. MIDDLEWARES ---
app.use(cors());
app.use(express.json()); 

// --- 2. IMPORTAR RUTAS ---
// Rutas que ya tenías (HEAD)
const partidosRoutes = require('./routes/partidos');
const listaArbitrosRoutes = require('./routes/listaArbitros');

// --- 3. USAR RUTAS ---
// Rutas antiguas
app.use('/api/partidos', partidosRoutes);
app.use('/api/lista-arbitros', listaArbitrosRoutes);

// Rutas nuevas (que venían de F4-Resenas)
app.use("/api/resenas", require("./routes/resenas"));
app.use("/api/arbitros", require("./routes/arbitros"));

// Ruta común (estaba en ambas, dejamos solo una)
app.use('/api/usuarios', require("./routes/usuarios")); 

// --- 4. ENCENDER SERVIDOR ---
app.listen(3000, () => {
    console.log("Servidor corriendo en puerto 3000");
});