package com.example.konekin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.databinding.ActivityUserDetailBinding
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra(EXTRA_ID, -1)
        if (userId == -1) {
            Toast.makeText(this, "ID user tidak ditemukan", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val client = ApiClient.getInstance()
        client.getUserById(userId).enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                val data = response.body()?.data
                if (!response.isSuccessful || data == null) {
                    Toast.makeText(this@UserDetailActivity, "Data tidak ditemukan", Toast.LENGTH_LONG).show()
                    return
                }
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

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
