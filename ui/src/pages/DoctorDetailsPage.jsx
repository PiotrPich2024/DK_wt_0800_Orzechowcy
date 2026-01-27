import { useParams, Link } from "react-router";
import { useEffect, useState } from "react";
import api from "../services/api";

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

const DoctorDetailsPage = () => {
    const { id } = useParams();
    const [doctor, setDoctor] = useState(null);
    const [schedules, setSchedules] = useState([]);

    useEffect(() => {
        api.get(`/doctors/${id}`).then(res => setDoctor(res.data));
        api.get(`/doctors/${id}/schedules`).then(res => setSchedules(res.data));
    }, [id]);

    if (!doctor) return <div>Ładowanie...</div>;

    return (
        <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto", fontFamily: "Arial, sans-serif" }}>
             <Link to="/doctors">← Powrót do listy lekarzy</Link>
            <div style={{ border: "1px solid #ccc", padding: "20px", borderRadius: "8px", marginTop: "20px", backgroundColor: "#f9f9f9" }}>
                <h2 style={{ borderBottom: "1px solid #ddd", paddingBottom: "10px" }}>{doctor.firstName} {doctor.lastName}</h2>
                <p><strong>ID:</strong> {doctor.id}</p>
                <p><strong>Specjalizacja:</strong> {doctor.specialty}</p>
                <p><strong>Telefon:</strong> {doctor.phone}</p>
            </div>

            <h3 style={{ marginTop: "30px" }}>Grafik dyżurów</h3>
            {schedules.length === 0 ? (
                <p>Brak zaplanowanych dyżurów.</p>
            ) : (
                <ul style={{ listStyleType: "none", padding: 0 }}>
                    {schedules.map((s, index) => (
                        <li key={index} style={{ backgroundColor: "#eef", margin: "10px 0", padding: "10px", borderRadius: "5px", borderLeft: "5px solid #007bff" }}>
                            <div><strong>Termin:</strong> {formatDateForDisplay(s.startDate)} - {formatDateForDisplay(s.endDate)}</div>
                            <div><strong>Gabinet:</strong> {s.roomNumber}</div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default DoctorDetailsPage;
