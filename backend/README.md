# Konekin backend

REST API pengganti `dummy.restapiexample.com` dengan path, body, dan bentuk balasan yang sama. Data disimpan di `data/employees.json`, sehingga hasil tambah, ubah, dan hapus tetap ada setelah server dimatikan.

## Menjalankan

Butuh Node.js 18 atau lebih baru.

```bash
cd backend
npm install
npm start
```

Server berjalan di `http://localhost:3000/api/v1/`. Port bisa diganti dengan variabel `PORT`.

Data awal dibuat saat server pertama kali jalan. Untuk mengulang dari data awal, hapus folder `data/` lalu jalankan lagi.

## Endpoint

| Method | Path | Body |
| --- | --- | --- |
| GET | `/api/v1/employees` | - |
| GET | `/api/v1/employee/{id}` | - |
| POST | `/api/v1/create` | `employee_name`, `employee_salary`, `employee_age` |
| PUT | `/api/v1/update/{id}` | `employee_name`, `employee_salary`, `employee_age` |
| DELETE | `/api/v1/delete/{id}` | - |

## Menghubungkan aplikasi

`BASE_URL` di `app/build.gradle.kts` menentukan server yang dipakai aplikasi.

- Emulator Android: `http://10.0.2.2:3000/api/v1/` (10.0.2.2 adalah alamat komputer dari dalam emulator).
- HP fisik di Wi-Fi yang sama: ganti dengan IP komputer, misalnya `http://192.168.1.10:3000/api/v1/`, lalu tambahkan IP itu di `app/src/main/res/xml/network_security_config.xml`.
- Server yang sudah di-deploy dengan HTTPS: isi alamatnya, dan izin cleartext tidak diperlukan.
