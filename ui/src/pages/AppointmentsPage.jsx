import React, { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router";
import api from "../services/api.js";

const SPECIALIZATIONS = [
  "Kardiolog",
  "Dermatolog",
  "Neurolog",
  "Pediatra",
  "Ortopeda",
  "Ginekolog",
  "Psychiatra"
];

const AppointmentsPage = () => {
  const [searchParams] = useSearchParams();

  const [appointments, setAppointments] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);

  const [searchData, setSearchData] = useState({
    specialization: "",
    startDate: "",
    endDate: ""
  });

  const [formData, setFormData] = useState({
    patientId: searchParams.get("patientId") || "",
    scheduleId: "",
    startDate: "",
    endDate: ""
  });

  /* ===================== DATE HELPERS ===================== */

  const parseDate = (value) => {
    if (!value) return null;
    if (Array.isArray(value)) {
      return new Date(value[0], value[1] - 1, value[2], value[3] || 0, value[4] || 0);
    }
    return new Date(value);
  };

  const toDateTimeLocal = (value) => {
    const date = parseDate(value);
    if (!date || isNaN(date.getTime())) return "";

    const pad = (n) => n.toString().padStart(2, "0");

    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  };

  /* ===================== API ===================== */

  const loadAppointments = async () => {
    const res = await api.get("/appointment");
    setAppointments(res.data);
  };

  useEffect(() => {
    loadAppointments();
  }, []);

  /* ===================== HANDLERS ===================== */

  const handleSearchChange = (e) => {
    const { name, value } = e.target;
    setSearchData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  /* ===================== ACTIONS ===================== */

  const searchFreeSlots = async (e) => {
    e.preventDefault();

    let dateParam = searchData.startDate;
    if (dateParam.length === 16) {
      dateParam += ":00";
    }

    const res = await api.get(
      `/appointment/${searchData.specialization}/${dateParam}`
    );

    let slots = res.data;

    if (searchData.endDate) {
      const end = new Date(searchData.endDate);
      slots = slots.filter(s => parseDate(s.appointmentStartDate) <= end);
    }

    setAvailableSlots(slots);

    if (slots.length === 0) {
      alert("Brak wolnych terminów dla wybranej specjalizacji.");
    }
  };

  const selectSlot = (slot) => {
    setFormData((prev) => ({
      ...prev,
      scheduleId: slot.scheduleId,
      startDate: toDateTimeLocal(slot.appointmentStartDate),
      endDate: toDateTimeLocal(slot.appointmentEndDate)
    }));
  };

  const addAppointment = async (e) => {
    e.preventDefault();

    await api.post("/appointment/add", {
      patientId: Number(formData.patientId),
      scheduleId: Number(formData.scheduleId),
      startDate: formData.startDate,
      endDate: formData.endDate
    });

    alert("Wizyta zapisana!");
    setAvailableSlots([]);
    setFormData({ patientId: "", scheduleId: "", startDate: "", endDate: "" });
    loadAppointments();
  };

  /* ===================== UI ===================== */

  return (
    <div style={containerStyle}>
      <h1>Wizyty</h1>
      <Link to="/">Strona główna</Link>

      {/* ===== SEARCH ===== */}
      <form onSubmit={searchFreeSlots} style={formStyle}>
        <h3>Wolne terminy</h3>

        <select
          name="specialization"
          value={searchData.specialization}
          onChange={handleSearchChange}
          required
        >
          <option value="">-- wybierz specjalizację --</option>
          {SPECIALIZATIONS.map(s => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>

        <input
          type="datetime-local"
          name="startDate"
          value={searchData.startDate}
          onChange={handleSearchChange}
          required
        />

        <input
          type="datetime-local"
          name="endDate"
          value={searchData.endDate}
          onChange={handleSearchChange}
        />

        <button>Szukaj</button>
      </form>

      {/* ===== SLOTS ===== */}
     {availableSlots.map((slot, i) => {
      const d = parseDate(slot.appointmentStartDate);

      return (
        <div key={i} style={slotStyle}>
          <div>
            {d ? (
              <strong>
                {d.toLocaleDateString()}{" "}
                {d.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
              </strong>
            ) : (
              <strong>Nieprawidłowa data</strong>
            )}
            <br />
            {slot.doctorFirstName} {slot.doctorLastName} ({slot.specialty})<br />
            Gabinet {slot.roomNumber}
          </div>
          <button onClick={() => selectSlot(slot)}>Wybierz</button>
        </div>
      );
    })}

      {/* ===== ADD ===== */}
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

        <input value={formData.scheduleId} readOnly />
        <input value={formData.startDate} readOnly />
        <input value={formData.endDate} readOnly />

        <button disabled={!formData.scheduleId}>Zapisz</button>
      </form>
    </div>
  );
};

/* ===================== STYLES ===================== */

const containerStyle = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  gap: "20px"
};

const formStyle = {
  width: "300px",
  padding: "15px",
  border: "1px solid #ccc",
  borderRadius: "8px",
  display: "flex",
  flexDirection: "column",
  gap: "10px"
};

const slotStyle = {
  width: "350px",
  borderBottom: "1px solid #eee",
  padding: "8px",
  display: "flex",
  justifyContent: "space-between"
};

export default AppointmentsPage;
