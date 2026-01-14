import React, { useEffect, useState } from "react";
import { Link } from "react-router";
import api from "../services/api.js";

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

  const formatDateTime = (value) => (value ? value : null);

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
      startDate: slot.startDate,
      endDate: slot.endDate,
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
                  <strong>Start:</strong> {s.startDate}
                </p>
                <p>
                  <strong>Koniec:</strong> {s.endDate}
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
                  padding: "8px",
                  borderBottom: "1px solid #eee",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  {slot.doctorName} | {slot.specialization} | gab.{" "}
                  {slot.roomNumber}
                  <br />
                  {slot.startDate} → {slot.endDate}
                </div>
                <button
                  onClick={() => selectSlot(slot)}
                  style={{ marginLeft: "10px" }}
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
