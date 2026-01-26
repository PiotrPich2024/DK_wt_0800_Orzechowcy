import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
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

const DoctorPage = () => {
    const navigate = useNavigate();

    const [doctors, setDoctors] = useState([]);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        specialty: "Kardiolog",
        pesel: "",
        address: "",
        phone: ""
    });

    const loadDoctors = async () => {
        try {
            const response = await api.get("/doctors");
            setDoctors(response.data);
        } catch (err) {
            console.error("Błąd pobierania doktorów", err);
        }
    };

    useEffect(() => {
        loadDoctors();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/doctors/add", formData);
            alert("Doktor dodany");
            setFormData({
                firstName: "",
                lastName: "",
                specialty: "Kardiolog",
                pesel: "",
                address: "",
                phone: ""
            });
            loadDoctors();
        } catch (err) {
            console.error(err);
            alert("Błąd podczas dodawania doktora");
        }
    };

    const deleteDoctor = async (id) => {
        if (!window.confirm("Czy na pewno usunąć doktora?")) return;

        try {
            const response = await api.get(`/doctors/delete/${id}`);
            if (response.data === true) {
                loadDoctors();
            } else {
                alert("Nie można usunąć doktora – ma przypisane dyżury");
            }
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div style={{ maxWidth: "900px", margin: "0 auto" }}>
            <h1 style={{ textAlign: "center" }}>Doktorzy</h1>

            <Link to="/">← Strona główna</Link>

            <h2>Lista doktorów</h2>

            {doctors.length === 0 && <p>Brak doktorów</p>}

            {doctors.map(d => (
                <div key={d.id} style={{
                    border: "1px solid #ccc",
                    padding: "12px",
                    marginBottom: "10px",
                    borderRadius: "6px"
                }}>
                    <p><strong>ID: {d.id}</strong></p>
                    <p><strong>{d.firstName} {d.lastName}</strong></p>
                    <p>Specjalizacja: {d.specialty}</p>
                    <p>Telefon: {d.phone}</p>
                    <button onClick={() => navigate(`/doctors/${d.id}`)}>
                        Szczegóły
                    </button>

                    <button
                        onClick={() => deleteDoctor(d.id)}
                        style={{ marginLeft: "10px", background: "#ff4444", color: "white" }}
                    >
                        Usuń
                    </button>
                </div>
            ))}

            <h2>Dodaj doktora</h2>

            <form onSubmit={handleSubmit} style={{ display: "grid", gap: "8px", maxWidth: "300px" }}>
                <input
                    name="firstName"
                    placeholder="Imię"
                    value={formData.firstName}
                    onChange={handleChange}
                    required
                />

                <input
                    name="lastName"
                    placeholder="Nazwisko"
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                />

                <select name="specialty" value={formData.specialty} onChange={handleChange}>
                    <option>Kardiolog</option>
                    <option>Dermatolog</option>
                    <option>Neurolog</option>
                    <option>Pediatra</option>
                    <option>Ortopeda</option>
                    <option>Ginekolog</option>
                    <option>Psychiatra</option>
                </select>

                <input
                    name="pesel"
                    placeholder="PESEL"
                    value={formData.pesel}
                    onChange={handleChange}
                    required
                />

                <input
                    name="address"
                    placeholder="Adres"
                    value={formData.address}
                    onChange={handleChange}
                    required
                />

                <input
                    name="phone"
                    placeholder="Telefon"
                    value={formData.phone}
                    onChange={handleChange}
                    required
                />

                <button type="submit">Dodaj</button>
            </form>
        </div>
    );
};

export default DoctorPage;
