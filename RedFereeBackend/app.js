const express = require("express");
const cors = require("cors");
const db = require("./config/db"); // Asegúrate que db.js exista y conecte bien

const app = express();

app.use(cors());
app.use(express.json());

// RUTAS
app.use("/api/usuarios", require("./routes/usuarios"));
// --- AGREGA ESTA LÍNEA ---
app.use("/api/partidos", require("./routes/partidos")); 

app.listen(3000, () => {
    console.log("Servidor corriendo en puerto 3000");
});