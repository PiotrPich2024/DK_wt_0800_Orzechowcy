import React, { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router";
import api from "../services/api.js";

const AppointmentsPage = () => {
  const [searchParams] = useSearchParams();
  const [appointments, setAppointments] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);
  

  const [searchData, setSearchData] = useState({
    doctorId: "",
    startDate: "",
    endDate: "",
  });

  const [formData, setFormData] = useState({
    patientId: searchParams.get("patientId") || "",
    scheduleId: "",
    startDate: "",
    endDate: "",
  });

  /* ===================== Funkcja do konwersji daty ===================== */
  const parseDate = (value) => {
    if (!value) return null;
    if (Array.isArray(value)) {
        // [y, m, d, h, min]
        return new Date(value[0], value[1] - 1, value[2], value[3] || 0, value[4] || 0);
    }
    return new Date(value);
  };

const toDateTimeLocal = (value) => {
  const date = parseDate(value);
  if (!date || isNaN(date.getTime())) return "";

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
      let dateParam = searchData.startDate;
      // Ensure seconds are present for LocalDateTime
      if (dateParam && dateParam.length === 16) {
         dateParam += ":00";
      }

      const response = await api.get(
        `/appointment/${searchData.doctorId}/${dateParam}`
      );
      
      console.log("Raw slots:", response.data);
      let slots = response.data;
      if (searchData.endDate) {
          const end = new Date(searchData.endDate);
          slots = slots.filter(slot => {
             const slotDate = parseDate(slot.appointmentStartDate);
             return slotDate && slotDate <= end;
          });
      }
      setAvailableSlots(slots);
      if (slots.length === 0) {
          alert("Brak wolnych terminów w wybranym przedziale. Spróbuj zmienić datę początkową.");
      }
    } catch (error) {
      console.error("Błąd pobierania slotów:", error);
      alert("Wystąpił błąd podczas wyszukiwania terminów. Sprawdź poprawność danych (ID lekarza, data).");
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

            <label>Data od:</label>
            <input
              type="datetime-local"
              name="startDate"
              value={searchData.startDate}
              onChange={handleSearchChange}
              required
            />
            
            <label>Data do (opcjonalnie):</label>
            <input
              type="datetime-local"
              name="endDate"
              value={searchData.endDate}
              onChange={handleSearchChange}
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
                   {(() => {
                        const d = parseDate(slot.appointmentStartDate);
                        if (!d || isNaN(d.getTime())) return <span>Błędna data</span>;
                        return <strong>{d.toLocaleDateString()} {d.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</strong>;
                   })()}
                   <br/>
                  {slot.doctorFirstName} {slot.doctorLastName} ({slot.specialty})<br />
                  Gabinet {slot.roomNumber}
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
