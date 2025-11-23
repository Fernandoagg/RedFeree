const express = require("express");
const router = express.Router();
const db = require("../config/db"); 

// =========================================================
// 1. RUTA POST: Dar de Alta una Reseña (INSERTAR)
// Endpoint: POST /api/resenas
// =========================================================
router.post("/calificar", (req, res) => {
    const { arbitroid, usuarioid, estrellas, texto } = req.body; 
    
    // Validación de datos esenciales
    if (!arbitroid || !usuarioid || !estrellas || !texto) {
        return res.status(400).json({ 
            error: "Faltan campos obligatorios para la reseña.",
            required_fields: ["arbitroid", "usuarioid", "estrellas", "texto"]
        });
    }

    // Consulta SQL para insertar en la tabla 'resenas'.
    // Las columnas SQL usadas son: arbitroid, usuarioid, estrellas, texto.
    const sql = `INSERT INTO resenas (arbitroid, usuarioid, estrellas, texto) 
                 VALUES (?, ?, ?, ?)`;
    
    const values = [
        arbitroid, 
        usuarioid, 
        estrellas, 
        texto
    ];

    db.query(sql, values, (err, result) => {
        if (err) {
            console.error("Error al insertar reseña en MySQL:", err);
            return res.status(500).json({ error: "Error en la base de datos al dar de alta la reseña.", details: err.message });
        }

        // Respuesta 201: Creado exitosamente
        res.status(201).json({ 
            status: "ok", 
            message: "Reseña dada de alta con éxito.", 
            id_insertado: result.insertId
        });
    });
});

// =========================================================
// 2. RUTA GET: Mostrar Reseñas (SELECCIONAR)
// Endpoint: GET /api/resenas?arbitroid=ARBI_X
// =========================================================
router.get("/visualizar", (req, res) => {
    // Filtro opcional: ID del árbitro
    const { arbitroid } = req.query;
    
    // Consulta SQL con JOIN a la tabla 'usuarios' para obtener los nombres
    let sql = `
        SELECT 
            r.texto, 
            r.estrellas, 
            r.fecha, 
            u_arb.nombre AS nombre_arbitro, 
            u_res.nombre AS nombre_usuario
        FROM resenas r
        -- Unir con usuarios para obtener el nombre del árbitro (u_arb)
        INNER JOIN usuarios u_arb ON r.arbitroid = u_arb.id 
        -- Unir con usuarios para obtener el nombre del usuario que reseña (u_res)
        INNER JOIN usuarios u_res ON r.usuarioid = u_res.id 
    `;
    let values = [];
    
    // Aplicar filtro si se especificó un arbitroid
    if (arbitroid) {
        sql += ` WHERE r.arbitroid = ?`;
        values.push(arbitroid);
    }
    
    // Ordenar por fecha de forma descendente y limitar a 20 resultados
    sql += ` ORDER BY r.fecha DESC LIMIT 20`;

    db.query(sql, values, (err, results) => {
        if (err) {
            console.error("Error al obtener reseñas de MySQL:", err);
            return res.status(500).json({ error: "Error interno del servidor al obtener las reseñas.", details: err.message });
        }
        
        // Mapear los resultados al formato JSON que espera la app de Kotlin
        const reviews = results.map(row => ({
            // Mapeo al modelo de Kotlin (NombreArbi, Cali, userName, Resena, tiempo)
            NombreArbi: row.nombre_arbitro, 
            Cali: row.estrellas,           
            userName: row.nombre_usuario,  
            Resena: row.texto,             
            tiempo: new Date(row.fecha).toISOString() // Formato de fecha para la app
        }));

        res.status(200).json({
            status: "ok",
            count: reviews.length,
            reviews
        });
    });
});

module.exports = router;