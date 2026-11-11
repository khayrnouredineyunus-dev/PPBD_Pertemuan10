package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityUserDetailBinding
import com.example.konekin.model.ApiResponse
import com.example.konekin.model.Data
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private var userId = -1
    private var currentData: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra(EXTRA_ID, -1)
        if (userId == -1) {
            Toast.makeText(this, "ID user tidak ditemukan", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.btnEdit.setOnClickListener { openEditForm() }
        binding.btnDelete.setOnClickListener { confirmDelete() }
    }

    // Reload every time the page is shown, so changes made in the edit form appear
    override fun onResume() {
        super.onResume()
        if (userId != -1) {
            loadDetail()
        }
    }

    private fun loadDetail() {
        val client = ApiClient.getInstance()
        client.getUserById(userId).enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                val data = response.body()?.data
                if (!response.isSuccessful || data == null) {
                    Toast.makeText(this@UserDetailActivity, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                    return
                }
                currentData = data
                binding.tvName.text = "Name: ${data.employeeName}"
                binding.tvId.text = "ID: ${data.id}"
                binding.tvAge.text = "Age: ${data.employeeAge}"
                binding.tvSalary.text = "Salary: ${data.employeeSalary}"
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openEditForm() {
        val data = currentData
        if (data == null) {
            Toast.makeText(this, "Data belum dimuat", Toast.LENGTH_SHORT).show()
            return
        }
        val formIntent = Intent(this, UserFormActivity::class.java)
        formIntent.putExtra(UserFormActivity.EXTRA_ID, userId)
        formIntent.putExtra(UserFormActivity.EXTRA_NAME, data.employeeName)
        formIntent.putExtra(UserFormActivity.EXTRA_SALARY, data.employeeSalary)
        formIntent.putExtra(UserFormActivity.EXTRA_AGE, data.employeeAge)
        startActivity(formIntent)
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Hapus user")
            .setMessage("Yakin ingin menghapus user ini?")
            .setPositiveButton("Hapus") { _, _ -> deleteUser() }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteUser() {
        val client = ApiClient.getInstance()
        client.deleteUser(userId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "User dihapus"
                    Toast.makeText(this@UserDetailActivity, message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@UserDetailActivity, "Gagal menghapus (${response.code()})", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@UserDetailActivity, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
