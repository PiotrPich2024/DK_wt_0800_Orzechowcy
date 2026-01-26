import React, { useEffect, useState } from "react";
import { Link } from "react-router";
import api from "../services/api.js";

const parseDate = (value) => {
    if (!value) return null;
    if (Array.isArray(value)) {
        return new Date(value[0], value[1] - 1, value[2], value[3] || 0, value[4] || 0);
    }
    return new Date(value);
};

const formatDateForDisplay = (value) => {
    const date = parseDate(value);
    if (!date || isNaN(date.getTime())) return "";
    return date.toLocaleString('pl-PL', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
};

const toDateTimeLocal = (value) => {
  const date = parseDate(value);
  if (!date || isNaN(date.getTime())) return "";

  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");

  return `${year}-${month}-${day}T${hours}:${minutes}`;
};

const SchedulePage = () => {
  const [scheduleList, setScheduleList] = useState([]); // zapisane dyżury
  const [availableSlots, setAvailableSlots] = useState([]); // dostępne sloty

  const [formData, setFormData] = useState({
    doctorId: "",
    roomId: "",
    startDate: "",
    endDate: "",
  });

  const [availFormData, setAvailFormData] = useState({
    specialization: "Kardiolog",
    startDate: "",
    endDate: "",
  });

  /* ===================== API ===================== */

  // pobierz zapisane dyżury
  const showSchedules = async () => {
    try {
      const response = await api.get("/schedule");
      setScheduleList(response.data);
    } catch (error) {
      console.error("Błąd pobierania dyżurów:", error);
    }
  };

  useEffect(() => {
    showSchedules();
  }, []);

  const formatDateTime = (value) => {
      // If value is a date string from input (YYYY-MM-DDTHH:mm), return it as is or handle appropriately.
      // Backend expects ISO-like structure if it maps to LocalDateTime.
      // Usually standard ISO string creates array on backend if using Jackson without extra config.
      // But let's assume raw string from input works if it worked before or just needs minor check.
      return value ? value : null;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAvailInputChange = (e) => {
    const { name, value } = e.target;
    setAvailFormData((prev) => ({ ...prev, [name]: value }));
  };

  // dodaj dyżur
  const handleAddSchedule = async (e) => {
    e.preventDefault();
    try {
      await api.post("/schedule/add", {
        doctorId: Number(formData.doctorId),
        roomId: Number(formData.roomId),
        startDate: formatDateTime(formData.startDate),
        endDate: formatDateTime(formData.endDate),
      });
      alert("Dyżur dodany!");
      setFormData({ doctorId: "", roomId: "", startDate: "", endDate: "" });
      showSchedules();
    } catch (error) {
      console.error("Błąd dodawania dyżuru:", error);
    }
  };

  // sprawdź dostępność
  const checkAvailability = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/schedule/available", {
        specialization: availFormData.specialization,
        startDate: formatDateTime(availFormData.startDate),
        endDate: formatDateTime(availFormData.endDate),
      });
      setAvailableSlots(response.data);
    } catch (error) {
      console.error("Błąd sprawdzania dostępności:", error);
    }
  };

  // uzupełnij formularz dodawania dyżuru klikając slot
  const selectSlot = (slot) => {
    setFormData({
      doctorId: slot.doctorId,
      roomId: slot.roomId,
      startDate: toDateTimeLocal(slot.startDate),
      endDate: toDateTimeLocal(slot.endDate),
    });
  };

  /* ===================== UI ===================== */
  return (
    <div>
      <h1 style={{ textAlign: "center" }}>Strona dyżurów</h1>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: "15px",
        }}
      >
        <Link to="/">Strona główna</Link>

        {/* ===== ZAPISANE DYŻURY ===== */}
        {scheduleList.length > 0 && (
          <div style={{ width: "80%" }}>
            <h2>Lista dyżurów</h2>
            {scheduleList.map((s, idx) => (
              <div
                key={idx}
                style={{
                  border: "1px solid #ccc",
                  padding: "10px",
                  marginBottom: "10px",
                  borderRadius: "6px",
                  backgroundColor: "#f9f9f9",
                }}
              >
                <p>
                  <strong>Lekarz:</strong> {s.doctorsFullName}
                </p>
                <p>
                  <strong>Specjalizacja:</strong> {s.specialization}
                </p>
                <p>
                  <strong>Gabinet:</strong> {s.roomNumber}
                </p>
                <p>
                  <strong>Termin:</strong> {formatDateForDisplay(s.startDate)} - {formatDateForDisplay(s.endDate)}
                </p>
              </div>
            ))}
          </div>
        )}

        {/* ===== FORMULARZE ===== */}
        <div
          style={{
            display: "flex",
            gap: "20px",
            flexWrap: "wrap",
            justifyContent: "center",
          }}
        >
          {/* Sprawdź dostępność */}
          <form onSubmit={checkAvailability} style={formStyle}>
            <h3>Sprawdź dostępność</h3>

            <select
              name="specialization"
              value={availFormData.specialization}
              onChange={handleAvailInputChange}
            >
              <option>Kardiolog</option>
              <option>Dermatolog</option>
              <option>Neurolog</option>
              <option>Pediatra</option>
              <option>Ortopeda</option>
              <option>Ginekolog</option>
              <option>Psychiatra</option>
            </select>

            <input
              type="datetime-local"
              name="startDate"
              value={availFormData.startDate}
              onChange={handleAvailInputChange}
              required
            />
            <input
              type="datetime-local"
              name="endDate"
              value={availFormData.endDate}
              onChange={handleAvailInputChange}
              required
            />

            <button type="submit">Szukaj</button>
          </form>

          {/* Dodaj dyżur */}
          <form onSubmit={handleAddSchedule} style={formStyle}>
            <h3>Dodaj dyżur</h3>

            <input
              name="doctorId"
              type="number"
              placeholder="ID lekarza"
              value={formData.doctorId}
              onChange={handleInputChange}
              required
            />
            <input
              name="roomId"
              type="number"
              placeholder="ID gabinetu"
              value={formData.roomId}
              onChange={handleInputChange}
              required
            />

            <input
              type="datetime-local"
              name="startDate"
              value={formData.startDate}
              onChange={handleInputChange}
              required
            />
            <input
              type="datetime-local"
              name="endDate"
              value={formData.endDate}
              onChange={handleInputChange}
              required
            />

            <button type="submit">Zapisz</button>
          </form>
        </div>

        {/* ===== DOSTĘPNE SLOTY ===== */}
        {availableSlots.length > 0 && (
          <div style={{ width: "80%", marginTop: "20px" }}>
            <h3>Dostępne sloty:</h3>
            {availableSlots.map((slot, idx) => (
              <div
                key={idx}
                style={{
                  padding: "15px",
                  borderBottom: "1px solid #ddd",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  backgroundColor: "#fff",
                  borderRadius: "5px",
                  marginBottom: "5px",
                  boxShadow: "0 1px 3px rgba(0,0,0,0.1)"
                }}
              >
                <div>
                  <div style={{fontWeight: "bold", fontSize: "1.1em"}}>{slot.doctorName} ({slot.specialization})</div>
                  <div>ID Lekarza: {slot.doctorId}</div>
                  <div>Gabinet: {slot.roomNumber} (ID: {slot.roomId})</div>
                  <div style={{marginTop: "5px", color: "#555"}}>
                      {formatDateForDisplay(slot.startDate)} — {formatDateForDisplay(slot.endDate)}
                  </div>
                </div>
                <button
                  onClick={() => selectSlot(slot)}
                  style={{ marginLeft: "10px", padding: "8px 16px", cursor: "pointer", backgroundColor: "#007bff", color: "white", border: "none", borderRadius: "4px" }}
                >
                  Wybierz
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
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

export default SchedulePage;
