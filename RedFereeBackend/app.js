const express = require("express");
const cors = require("cors");
const db = require("./config/db");

const app = express();

app.use(cors());
app.use(express.json());

// RUTAS
app.use("/api/usuarios", require("./routes/usuarios"));

app.listen(3000, () => {
    console.log("Servidor corriendo en puerto 3000");
});
