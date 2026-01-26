import React , {useEffect, useState} from "react";
import { Link, useNavigate } from "react-router";
import api from "../services/api.js";

const PatientPage = () => {
    const navigate = useNavigate();
    const [patientsList, setPatientsList] = useState([]);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        pesel: "",
        address: "",
        phone: ""
    });

    useEffect(() => {
        showPatients();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleAddPatient = async (e) => {
        e.preventDefault();
        try {
            await api.post("/patients/add", formData);
            alert("Pacjent dodany!");
            showPatients();
            setFormData({
                firstName: "",
                lastName: "",
                pesel: "",
                address: "",
                phone: ""
            });
        } catch (error) {
            console.error("Error adding patient:", error);
            alert("Błąd podczas dodawania pacjenta");
        }
    };

    const handleDeletePatient = async (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tego pacjenta?")) return;
        try {
            const response = await api.get(`/patients/delete/${id}`);
            if (response.data === true) {
                alert("Pacjent usunięty.");
                showPatients();
            } else {
                alert("Nie można usunąć pacjenta.");
            }
        } catch (error) {
            console.error("Error deleting patient:", error);
            alert("Błąd podczas usuwania pacjenta.");
        }
    };

    const showPatients = async () => {
        try {
            const response = await api.get("/patients");
            console.log("Response data:", response.data);
            
            if (Array.isArray(response.data)) {
                setPatientsList(response.data);
            } else {
                console.warn("Response data is not an array", response.data);
            }
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
        }
    };

    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona pacjentów</h1>
            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", marginTop: "20px"}}>
                <Link to="/"> Strona główna </Link>

                {patientsList && patientsList.length > 0 && (
                     <div style={{marginTop: "20px", width: "80%"}}>
                        <h2>Lista pacjentów:</h2>
                        {patientsList.map((patient) => (
                            <div key={patient.id} style={{
                                border: "1px solid #ccc",
                                padding: "10px",
                                margin: "10px 0",
                                borderRadius: "5px",
                                backgroundColor: "#f9f9f9"
                            }}>
                                <p><strong>ID:</strong> {patient.id}</p>
                                <p><strong>Imię i Nazwisko:</strong> {patient.firstName} {patient.lastName}</p>
                                <p><strong>Telefon:</strong> {patient.phone}</p>
                                <div style={{display: "flex", gap: "10px", marginTop: "5px"}}>
                                    <button onClick={() => handleDeletePatient(patient.id)} style={{backgroundColor: "#ff4444", color: "white", border: "none", padding: "5px 10px", cursor: "pointer", borderRadius: "3px"}}>Usuń</button>
                                    <button onClick={() => navigate(`/appointment?patientId=${patient.id}`)} style={{backgroundColor: "#007bff", color: "white", border: "none", padding: "5px 10px", cursor: "pointer", borderRadius: "3px"}}>Umów wizytę</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                <form onSubmit={handleAddPatient} style={{display: "flex", flexDirection: "column", gap: "10px", width: "300px", padding: "20px", border: "1px solid #ddd", borderRadius: "8px"}}>
                    <h3>Dodaj Pacjenta</h3>
                    <input name="firstName" placeholder="Imię" value={formData.firstName} onChange={handleInputChange} required />
                    <input name="lastName" placeholder="Nazwisko" value={formData.lastName} onChange={handleInputChange} required />
                    <input name="pesel" placeholder="PESEL" value={formData.pesel} onChange={handleInputChange} required />
                    <input name="address" placeholder="Adres" value={formData.address} onChange={handleInputChange} required />
                    <input name="phone" placeholder="Telefon" value={formData.phone} onChange={handleInputChange} required />
                    <button type="submit">Zapisz Pacjenta</button>
                </form>
            </div>
        </div>
    );
};
export default PatientPage;