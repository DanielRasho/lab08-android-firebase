package com.example.uvg_firebase

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class PokemonDetailsActivity : AppCompatActivity() {
    private lateinit var tvPkId: TextView
    private lateinit var tvPkName: TextView
    private lateinit var tvPkLv: TextView
    private lateinit var tvPkWeight: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("pkId").toString(),
                intent.getStringExtra("pkName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("pkId").toString()
            )
        }

    }

    private fun initView() {
        tvPkId = findViewById(R.id.tvPkId)
        tvPkName = findViewById(R.id.tvPkName)
        tvPkLv = findViewById(R.id.tvPkLv)
        tvPkWeight = findViewById(R.id.tvPkWeight)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvPkId.text = intent.getStringExtra("pkId")
        tvPkName.text = intent.getStringExtra("pkName")
        tvPkLv.text = intent.getStringExtra("pkLv")
        tvPkWeight.text = intent.getStringExtra("pkWeight")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Pokemons").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Pokemon data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(intent.getStringExtra("pkName").toString())
        etEmpAge.setText(intent.getStringExtra("pkLv").toString())
        etEmpSalary.setText(intent.getStringExtra("pkWeight").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpSalary.text.toString()
            )

            Toast.makeText(applicationContext, "Pokemon Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvPkName.text = etEmpName.text.toString()
            tvPkLv.text = etEmpAge.text.toString()
            tvPkWeight.text = etEmpSalary.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Pokemons").child(id)
        val empInfo = PokemonModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }

}