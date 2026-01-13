import React , {use, useEffect, useState} from "react";
import { Link } from "react-router";
import api from "../services/api.js";

class Doctor {
    constructor(id, firstname, lastname, specialization, pesel, address, phone) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.specialization = specialization;
        this.pesel = pesel;
        this.address = address;
        this.phone = phone;
    }
}

const DoctorPage = () => {
    const [doctorsList, setDoctorsList] = useState([]);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        specialty: "Kardiolog",
        pesel: "",
        address: "",
        phone: ""
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleAddDoctor = async (e) => {
        e.preventDefault();
        try {
            await api.post("/doctors/add", formData);
            alert("Doktor dodany!");
            showDoctors();
            setFormData({
                firstName: "",
                lastName: "",
                specialty: "Kardiolog",
                pesel: "",
                address: "",
                phone: ""
            });
        } catch (error) {
            console.error("Error adding doctor:", error);
            alert("Błąd podczas dodawania doktora");
        }
    };

    const handleDeleteDoctor = async (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tego doktora?")) return;
        try {
            const response = await api.get(`/doctors/delete/${id}`);
            if (response.data === true) {
                alert("Doktor usunięty.");
                showDoctors();
            } else {
                alert("Nie można usunąć doktora. Prawdopodobnie ma przypisane dyżury.");
            }
        } catch (error) {
            console.error("Error deleting doctor:", error);
            alert("Błąd podczas usuwania doktora.");
        }
    };

    const parseDoctor = (doctorString) => {
        try {
            const parts = doctorString.split(', ');
            const getVal = (key) => {
                const part = parts.find(p => p.startsWith(key));
                return part ? part.split(': ')[1] : '';
            };

            const id = getVal("Doktor ID");
            if (!id) return null;

            return new Doctor(
                parseInt(id),
                getVal("Imię"),
                getVal("Nazwisko"),
                getVal("Specjalizacja"),
                getVal("Pesel"),
                getVal("Adres"),
                getVal("Telefon")
            );
        } catch (e) {
            console.error("Error parsing doctor string:", doctorString, e);
            return null;
        }
    };
    const showDoctors = async () => {
        try {
            const response = await api.get("/doctors");
            console.log("Response data:", response.data);
            
            let dataToParse = response.data;
            
            if(!Array.isArray(dataToParse)) {
                 console.warn("Response data is not an array", dataToParse);
                 return;
            }

            const doctors = dataToParse.map(doctorString => parseDoctor(doctorString)).filter(d => d !== null);
            setDoctorsList(doctors);                                              
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
        }
    };

    useEffect(() => {
        showDoctors();
    }, []);
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona doktorów</h1>
            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", marginTop: "20px"}}>
                <Link to="/"> Strona główna </Link>
                <h1>Lista doktorów</h1>
                {doctorsList && doctorsList.length > 0 && (
                    <div style={{marginTop: "20px", width: "80%"}}>
                        <h2>Lista doktorów:</h2>
                        {doctorsList.map((doctor) => (
                            <div key={doctor.id} style={{
                                border: "1px solid #ccc",
                                padding: "10px",
                                margin: "10px 0",
                                borderRadius: "5px",
                                backgroundColor: "#f9f9f9"
                            }}>
                                <p><strong>ID:</strong> {doctor.id}</p>
                                <p><strong>Imię i Nazwisko:</strong> {doctor.firstname} {doctor.lastname}</p>
                                <p><strong>Specjalizacja:</strong> {doctor.specialization}</p>
                                <p><strong>PESEL:</strong> {doctor.pesel}</p>
                                <p><strong>Adres:</strong> {doctor.address}</p>
                                <p><strong>Telefon:</strong> {doctor.phone}</p>
                                <button onClick={() => handleDeleteDoctor(doctor.id)} style={{backgroundColor: "#ff4444", color: "white", border: "none", padding: "5px 10px", marginTop: "5px", cursor: "pointer", borderRadius: "3px"}}>Usuń</button>
                            </div>
                        ))}
                    </div>
                )}  
                <form onSubmit={handleAddDoctor} style={{display: "flex", flexDirection: "column", gap: "10px", width: "300px", padding: "20px", border: "1px solid #ddd", borderRadius: "8px"}}>
                    <h3>Dodaj Doktora</h3>
                    <input name="firstName" placeholder="Imię" value={formData.firstName} onChange={handleInputChange} required />
                    <input name="lastName" placeholder="Nazwisko" value={formData.lastName} onChange={handleInputChange} required />
                    <select name="specialty" value={formData.specialty} onChange={handleInputChange}>
                        <option value="Kardiolog">Kardiolog</option>
                        <option value="Dermatolog">Dermatolog</option>
                        <option value="Neurolog">Neurolog</option>
                        <option value="Pediatra">Pediatra</option>
                        <option value="Ortopeda">Ortopeda</option>
                        <option value="Ginekolog">Ginekolog</option>
                        <option value="Psychiatra">Psychiatra</option>
                    </select>
                    <input name="pesel" placeholder="PESEL" value={formData.pesel} onChange={handleInputChange} required />
                    <input name="address" placeholder="Adres" value={formData.address} onChange={handleInputChange} required />
                    <input name="phone" placeholder="Telefon" value={formData.phone} onChange={handleInputChange} required />
                    <button type="submit">Zapisz Doktora</button>
                </form>

            </div>
        </div>
    );
};
export default DoctorPage;