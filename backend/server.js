const express = require("express");
const fs = require("fs");
const path = require("path");

const PORT = process.env.PORT || 3000;
const DATA_FILE = process.env.DATA_FILE || path.join(__dirname, "data", "employees.json");

// Starting data, written to DATA_FILE on the first run only
const SEED = [
  { id: 1, employee_name: "Tiger Nixon", employee_salary: 320800, employee_age: 61, profile_image: "" },
  { id: 2, employee_name: "Garrett Winters", employee_salary: 170750, employee_age: 63, profile_image: "" },
  { id: 3, employee_name: "Ashton Cox", employee_salary: 86000, employee_age: 66, profile_image: "" },
  { id: 4, employee_name: "Cedric Kelly", employee_salary: 433060, employee_age: 22, profile_image: "" },
  { id: 5, employee_name: "Airi Satou", employee_salary: 162700, employee_age: 33, profile_image: "" },
  { id: 6, employee_name: "Brielle Williamson", employee_salary: 372000, employee_age: 61, profile_image: "" },
  { id: 7, employee_name: "Herrod Chandler", employee_salary: 137500, employee_age: 59, profile_image: "" },
  { id: 8, employee_name: "Rhona Davidson", employee_salary: 327900, employee_age: 55, profile_image: "" },
  { id: 9, employee_name: "Colleen Hurst", employee_salary: 205500, employee_age: 39, profile_image: "" },
  { id: 10, employee_name: "Sonya Frost", employee_salary: 103600, employee_age: 23, profile_image: "" }
];

function loadEmployees() {
  if (!fs.existsSync(DATA_FILE)) {
    saveEmployees(SEED);
  }
  return JSON.parse(fs.readFileSync(DATA_FILE, "utf8"));
}

function saveEmployees(list) {
  fs.mkdirSync(path.dirname(DATA_FILE), { recursive: true });
  fs.writeFileSync(DATA_FILE, JSON.stringify(list, null, 2));
}

let employees = loadEmployees();

// Accepts numbers or numeric strings ("345"), like the original API
function toNonNegativeInt(value) {
  const n = Number(value);
  return Number.isInteger(n) && n >= 0 ? n : null;
}

function parseBody(body) {
  const name = typeof body.employee_name === "string" ? body.employee_name.trim() : "";
  const salary = toNonNegativeInt(body.employee_salary);
  const age = toNonNegativeInt(body.employee_age);
  if (!name || salary === null || age === null) {
    return null;
  }
  return { employee_name: name, employee_salary: salary, employee_age: age };
}

function findIndex(id) {
  return employees.findIndex((e) => e.id === Number(id));
}

const app = express();
app.use(express.json());

const api = express.Router();

api.get("/employees", (req, res) => {
  res.json({ status: "success", data: employees, message: "Successfully! All records has been fetched." });
});

api.get("/employee/:id", (req, res) => {
  const index = findIndex(req.params.id);
  if (index === -1) {
    return res.status(404).json({ status: "error", data: null, message: "Record not found" });
  }
  res.json({ status: "success", data: employees[index], message: "Successfully! Record has been fetched." });
});

api.post("/create", (req, res) => {
  const fields = parseBody(req.body || {});
  if (!fields) {
    return res.status(400).json({ status: "error", data: null, message: "employee_name, employee_salary, and employee_age are required" });
  }
  const nextId = employees.reduce((max, e) => Math.max(max, e.id), 0) + 1;
  const created = { id: nextId, ...fields, profile_image: "" };
  employees.push(created);
  saveEmployees(employees);
  res.status(201).json({ status: "success", data: created, message: "Successfully! Record has been added." });
});

api.put("/update/:id", (req, res) => {
  const index = findIndex(req.params.id);
  if (index === -1) {
    return res.status(404).json({ status: "error", data: null, message: "Record not found" });
  }
  const fields = parseBody(req.body || {});
  if (!fields) {
    return res.status(400).json({ status: "error", data: null, message: "employee_name, employee_salary, and employee_age are required" });
  }
  employees[index] = { ...employees[index], ...fields };
  saveEmployees(employees);
  res.json({ status: "success", data: employees[index], message: "Successfully! Record has been updated." });
});

api.delete("/delete/:id", (req, res) => {
  const index = findIndex(req.params.id);
  if (index === -1) {
    return res.status(404).json({ status: "error", data: null, message: "Record not found" });
  }
  employees.splice(index, 1);
  saveEmployees(employees);
  res.json({ status: "success", data: String(req.params.id), message: "Successfully! Record has been deleted" });
});

app.use("/api/v1", api);

app.listen(PORT, () => {
  console.log(`Konekin API running on http://localhost:${PORT}/api/v1/`);
});
