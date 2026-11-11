package com.example.konekin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityUserFormBinding
import com.example.konekin.model.ApiResponse
import com.example.konekin.model.UserRequest
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // id -1 means "add" mode; any other id means "edit" mode
        val userId = intent.getIntExtra(EXTRA_ID, -1)
        val isEdit = userId != -1

        if (isEdit) {
            binding.tvTitle.text = "Edit User"
            binding.etName.setText(intent.getStringExtra(EXTRA_NAME))
            binding.etSalary.setText(intent.getIntExtra(EXTRA_SALARY, 0).toString())
            binding.etAge.setText(intent.getIntExtra(EXTRA_AGE, 0).toString())
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val salary = binding.etSalary.text.toString().toIntOrNull()
            val age = binding.etAge.text.toString().toIntOrNull()

            if (name.isEmpty() || salary == null || age == null) {
                Toast.makeText(this, "Name, salary, dan age wajib diisi", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val body = UserRequest(name, salary, age)
            val client = ApiClient.getInstance()
            val request = if (isEdit) client.updateUser(userId, body) else client.createUser(body)

            binding.btnSave.isEnabled = false
            request.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    binding.btnSave.isEnabled = true
                    if (response.isSuccessful) {
                        val message = response.body()?.message ?: "Data tersimpan"
                        Toast.makeText(this@UserFormActivity, message, Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@UserFormActivity, "Gagal menyimpan (${response.code()})", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this@UserFormActivity, "Koneksi error", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_SALARY = "extra_salary"
        const val EXTRA_AGE = "extra_age"
    }
}
