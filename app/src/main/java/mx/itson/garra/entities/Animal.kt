package mx.itson.garra.entities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import mx.itson.garra.persistence.GarraDB

class Animal {

    var id = 0
    var nombre: String = ""
    var especie: String = ""
    var habilidades: String = ""

    constructor()

    constructor(id: Int, nombre: String, especie: String, habilidades: String) {
        this.id = id
        this.nombre = nombre
        this.especie = especie
        this.habilidades = habilidades
    }

    // Obtener todos los animales de la base de datos
    fun get(context: Context): List<Animal> {
        val animales: MutableList<Animal> = ArrayList()
        try {
            val garraDB = GarraDB(context, "GarraDB", null, 1)
            val dataBase: SQLiteDatabase = garraDB.readableDatabase

            val result = dataBase.rawQuery("SELECT id, nombre, especie, habilidades FROM Animal", null)
            while (result.moveToNext()) {
                val animal = Animal(result.getInt(0), result.getString(1), result.getString(2), result.getString(3))
                animales.add(animal)
            }
        } catch (ex: Exception) {
            Log.e("Error obteniendo registros", ex.message.toString())
        }
        return animales
    }

    // Guardar un nuevo animal en la base de dato
    fun save(context: Context, nombre: String, especie: String, habilidades: String): Boolean {
        return try {
            // Validar campos vacíos
            if (nombre.isEmpty() || especie.isEmpty() || habilidades.isEmpty()) {
                Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return false
            }

            val garraDB = GarraDB(context, "GarraDB", null, 1)
            val dataBase: SQLiteDatabase = garraDB.writableDatabase

            val values = ContentValues()
            values.put("nombre", nombre)
            values.put("especie", especie)
            values.put("habilidades", habilidades)

            dataBase.insert("Animal", null, values)
            dataBase.close()

            // Mostrar Toast y vibrar al guardar
            Toast.makeText(context, "Animal guardado", Toast.LENGTH_SHORT).show()
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(100) // Vibrar por 100ms
            }

            true
        } catch (ex: Exception) {
            Log.e("Error al guardar un registro de animal", ex.message.toString())
            false
        }
    }
}