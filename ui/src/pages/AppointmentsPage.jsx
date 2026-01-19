import React, { useEffect, useState } from "react";
import { Link } from "react-router";
import api from "../services/api.js";

const AppointmentsPage = () => {
  const [appointments, setAppointments] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);
  

  const [searchData, setSearchData] = useState({
    doctorId: "",
    date: "",
  });

  const [formData, setFormData] = useState({
    patientId: "",
    scheduleId: "",
    startDate: "",
    endDate: "",
  });

  /* ===================== Funkcja do konwersji daty ===================== */
const toDateTimeLocal = (value) => {
  if (!value) return "";
  // jeśli backend zwraca ISO string np. 2026-01-17T09:45:00
  const date = new Date(value);
  // format: YYYY-MM-DDTHH:mm
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");

  return `${year}-${month}-${day}T${hours}:${minutes}`;
};


  /* ===================== API ===================== */

  const loadAppointments = async () => {
    try {
      const response = await api.get("/appointment");
      setAppointments(response.data);
    } catch (error) {
      console.error("Błąd pobierania wizyt:", error);
    }
  };

  useEffect(() => {
    loadAppointments();
  }, []);

  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  /* ===================== AKCJE ===================== */

  const searchFreeSlots = async (e) => {
    e.preventDefault();
    try {
      const response = await api.get(
        `/appointment/${searchData.doctorId}/${searchData.date}`
      );
      setAvailableSlots(response.data);
    } catch (error) {
      console.error("Błąd pobierania slotów:", error);
    }
  };

  const addAppointment = async (e) => {
    e.preventDefault();
    try {
      await api.post("/appointment/add", {
        patientId: Number(formData.patientId),
        scheduleId: Number(formData.scheduleId),
        startDate: formData.startDate,
        endDate: formData.endDate,
      });
      alert("Wizyta zapisana!");
      setFormData({
        patientId: "",
        scheduleId: "",
        startDate: "",
        endDate: "",
      });
      loadAppointments();
    } catch (error) {
      console.error("Błąd dodawania wizyty:", error);
    }
  };

  const deleteAppointment = async (id) => {
    try {
      await api.get(`/appointment/${id}/delete`);
      loadAppointments();
    } catch (error) {
      console.error("Błąd usuwania wizyty:", error);
    }
  };

  // wybór slotu → automatyczne wypełnienie formularza
const selectSlot = (slot) => {
  setFormData((prev) => ({
    ...prev,
    scheduleId: slot.scheduleId,
    startDate: toDateTimeLocal(slot.appointmentStartDate),
    endDate: toDateTimeLocal(slot.appointmentEndDate),
  }));
};

  /* ===================== UI ===================== */

  return (
    <div>
      <h1 style={{ textAlign: "center" }}>Wizyty</h1>

      <div style={containerStyle}>
        <Link to="/">Strona główna</Link>

        {/* ===== LISTA WIZYT ===== */}
        {appointments.length > 0 && (
          <div style={{ width: "80%" }}>
            <h2>Lista wizyt</h2>
            {appointments.map((a) => (
              <div key={a.id} style={cardStyle}>
                <p>
                  <strong>Pacjent:</strong> {a.patientFirstName} {a.patientLastName}
                </p>
                <p>
                  <strong>Lekarz:</strong> {a.doctorFirstName} {a.doctorLastName} ({a.specialty})
                </p>
                <p>
                  <strong>Gabinet:</strong> {a.roomNumber}
                </p>
                <p>
                  <strong>Termin:</strong> {toDateTimeLocal(a.appointmentStartDate)} → {toDateTimeLocal(a.appointmentEndDate)}
                </p>
                <button onClick={() => deleteAppointment(a.id)}>Usuń</button>
              </div>
            ))}
          </div>
        )}

        {/* ===== FORMULARZE ===== */}
        <div style={formsWrapperStyle}>
          {/* Szukaj wolnych terminów */}
          <form onSubmit={searchFreeSlots} style={formStyle}>
            <h3>Wolne terminy lekarza</h3>

            <input
              type="number"
              name="doctorId"
              placeholder="ID lekarza"
              value={searchData.doctorId}
              onChange={handleSearchChange}
              required
            />

            <input
              type="datetime-local"
              name="date"
              value={searchData.date}
              onChange={handleSearchChange}
              required
            />

            <button type="submit">Szukaj</button>
          </form>

          {/* Dodaj wizytę */}
          <form onSubmit={addAppointment} style={formStyle}>
            <h3>Dodaj wizytę</h3>

            <input
              type="number"
              name="patientId"
              placeholder="ID pacjenta"
              value={formData.patientId}
              onChange={handleFormChange}
              required
            />

            <input
              type="number"
              name="scheduleId"
              placeholder="ID dyżuru"
              value={formData.scheduleId}
              readOnly
              style={{ backgroundColor: "#eee" }}
            />

            <input
              type="datetime-local"
              name="startDate"
              value={formData.startDate}
              readOnly
            />

            <input
              type="datetime-local"
              name="endDate"
              value={formData.endDate}
              readOnly
            />

            <button type="submit" disabled={!formData.scheduleId}>Zapisz</button>
          </form>
        </div>

        {/* ===== DOSTĘPNE SLOTY ===== */}
        {availableSlots.length > 0 && (
          <div style={{ width: "80%", marginTop: "20px" }}>
            <h3>Dostępne sloty:</h3>
            {availableSlots.map((slot, idx) => (
              <div key={idx} style={slotStyle}>
                <div>
                  {slot.doctorFirstName} {slot.doctorLastName} ({slot.specialty})<br />
                  Gabinet {slot.roomNumber}<br />
                  {toDateTimeLocal(slot.appointmentStartDate)} → {toDateTimeLocal(slot.appointmentEndDate)}
                </div>
                <button onClick={() => selectSlot(slot)}>Wybierz</button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

/* ===================== STYLE ===================== */

const containerStyle = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  gap: "15px",
};

const formsWrapperStyle = {
  display: "flex",
  gap: "20px",
  flexWrap: "wrap",
  justifyContent: "center",
};

const formStyle = {
  width: "300px",
  padding: "20px",
  border: "1px solid #ddd",
  borderRadius: "8px",
  display: "flex",
  flexDirection: "column",
  gap: "10px",
};

const cardStyle = {
  border: "1px solid #ccc",
  padding: "10px",
  marginBottom: "10px",
  borderRadius: "6px",
  backgroundColor: "#f9f9f9",
};

const slotStyle = {
  padding: "8px",
  borderBottom: "1px solid #eee",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
};

export default AppointmentsPage;
