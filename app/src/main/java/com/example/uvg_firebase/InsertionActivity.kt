package com.example.uvg_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var etPokemonName: EditText
    private lateinit var etPokemonLevel: EditText
    private lateinit var etPokemonWeight: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etPokemonName = findViewById(R.id.etEmpName)
        etPokemonLevel = findViewById(R.id.etEmpAge)
        etPokemonWeight = findViewById(R.id.etEmpSalary)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Pokemons")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val pokemonName = etPokemonName.text.toString()
        val pokemonLevel = etPokemonLevel.text.toString()
        val pokemonWeight = etPokemonWeight.text.toString()

        if (pokemonName.isEmpty()) {
            etPokemonName.error = "Please enter name"
        }
        if (pokemonLevel.isEmpty()) {
            etPokemonLevel.error = "Please enter level"
        }
        if (pokemonWeight.isEmpty()) {
            etPokemonWeight.error = "Please enter weight"
        }

        val empId = dbRef.push().key!!

        val employee = PokemonModel(empId, pokemonName, pokemonLevel, pokemonWeight)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etPokemonName.text.clear()
                etPokemonLevel.text.clear()
                etPokemonWeight.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}