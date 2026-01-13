import React , {useEffect, useState} from "react";
import { Link } from "react-router";
import api from "../services/api.js";

class Schedule {
    constructor(id, doctorName, roomNumber, startDate, endDate) {
        this.id = id;
        this.doctorName = doctorName;
        this.roomNumber = roomNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

const SchedulePage = () => {
    const [scheduleList, setScheduleList] = useState([]);
    const [formData, setFormData] = useState({
        doctorId: "",
        roomId: "",
        startDate: "",
        endDate: ""
    });

    const [availFormData, setAvailFormData] = useState({
        specialization: "Kardiolog",
        startDate: "",
        endDate: ""
    });
    const [availableResources, setAvailableResources] = useState({ doctors: [], rooms: [] });

    useEffect(() => {
        showSchedules();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleAvailInputChange = (e) => {
        const { name, value } = e.target;
        setAvailFormData({ ...availFormData, [name]: value });
    };

    const formatInfo = (dateStr) => dateStr.replace("T", " ") + ":00";

    const checkAvailability = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                specialization: availFormData.specialization,
                startDate: formatInfo(availFormData.startDate),
                endDate: formatInfo(availFormData.endDate)
            };
            const response = await api.post("/schedule/available", payload);
            console.log("Available resources:", response.data);
            
            const doctors = [];
            const rooms = [];
            
            if (Array.isArray(response.data)) {
                response.data.forEach(item => {
                    if (typeof item === 'string' && item.includes("ID gabinetu:")) {
                        rooms.push(item);
                    } else if (/^\d+$/.test(item)) {
                         rooms.push("ID gabinetu: ? (Numer: " + item + ")");
                    } else {
                        doctors.push(item);
                    }
                });
            }
            setAvailableResources({ doctors, rooms });

        } catch (error) {
            console.error("Error checking availability:", error);
            alert("Błąd podczas sprawdzania dostępności");
        }
    };
    
    const extractDoctorId = (docStr) => {
        const match = docStr.match(/Doktor ID:\s*(\d+)/);
        return match ? match[1] : "";
    };

    const fillDoctor = (docStr) => {
        const id = extractDoctorId(docStr);
        if(id) setFormData(prev => ({...prev, doctorId: id}));
    };

    const extractRoomId = (roomStr) => {
        const match = roomStr.match(/ID gabinetu:\s*(\d+)/);
        return match ? match[1] : "";
    };

    const fillRoom = (roomStr) => {
         const id = extractRoomId(roomStr);
         if(id) setFormData(prev => ({...prev, roomId: id}));
    };

    const handleAddSchedule = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                doctorId: formData.doctorId,
                roomId: formData.roomId,
                startDate: formatInfo(formData.startDate),
                endDate: formatInfo(formData.endDate)
            };

            await api.post("/schedule/add", payload);
            alert("Dyżur dodany!");
            showSchedules();
            setFormData({ doctorId: "", roomId: "", startDate: "", endDate: "" });
        } catch (error) {
            console.error("Error adding schedule:", error);
            alert("Błąd podczas dodawania dyżuru");
        }
    };

    const handleDeleteSchedule = async (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć ten dyżur?")) return;
        try {
            const response = await api.get(`/schedule/delete/${id}`);
            if (response.data === true) {
                alert("Dyżur usunięty.");
                showSchedules();
            } else {
                alert("Nie można usunąć dyżuru.");
            }
        } catch (error) {
            console.error("Error deleting schedule:", error);
            alert("Błąd podczas usuwania dyżuru.");
        }
    };

    const parseSchedule = (scheduleString) => {
        try {
            const parts = scheduleString.split(' ');
            if (parts.length < 6) return null; 

            const id = parts[0];
            const doctorName = parts[1] + " " + parts[2];
            const roomNumber = parts[3];
            
            const remainingParts = parts.slice(4);
            const midPoint = Math.floor(remainingParts.length / 2);
            
            const startDate = remainingParts.slice(0, midPoint).join(' ');
            const endDate = remainingParts.slice(midPoint).join(' ');

            return new Schedule(
                parseInt(id),
                doctorName,
                roomNumber,
                startDate,
                endDate
            );
        } catch (e) {
            console.error("Error parsing schedule string:", scheduleString, e);
            return null;
        }
    };

    const showSchedules = async () => {
        try {
            const response = await api.get("/schedule");
            console.log("Response data:", response.data);
            
            let dataToParse = response.data;
            if(!Array.isArray(dataToParse)) {
                console.warn("Response data is not an array", dataToParse);
                return;
            }

            const schedules = dataToParse.map(s => parseSchedule(s)).filter(s => s !== null);
            setScheduleList(schedules);
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
        }
    };

    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona dyżurów</h1>
            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", marginTop: "20px"}}>
                <Link to="/"> Strona główna </Link>
                {scheduleList && scheduleList.length > 0 && (
                     <div style={{marginTop: "20px", width: "80%"}}>
                        <h2>Lista dyżurów:</h2>
                        {scheduleList.map((schedule) => (
                            <div key={schedule.id} style={{
                                border: "1px solid #ccc",
                                padding: "10px",
                                margin: "10px 0",
                                borderRadius: "5px",
                                backgroundColor: "#f9f9f9"
                            }}>
                                <p><strong>ID:</strong> {schedule.id}</p>
                                <p><strong>Lekarz:</strong> {schedule.doctorName}</p>
                                <p><strong>Gabinet:</strong> {schedule.roomNumber}</p>
                                <p><strong>Start:</strong> {schedule.startDate}</p>
                                <p><strong>Koniec:</strong> {schedule.endDate}</p>
                                <button onClick={() => handleDeleteSchedule(schedule.id)} style={{backgroundColor: "#ff4444", color: "white", border: "none", padding: "5px 10px", marginTop: "5px", cursor: "pointer", borderRadius: "3px"}}>Usuń</button>
                            </div>
                        ))}
                    </div>
                )}
                
                <div style={{display: "flex", gap: "20px", marginTop: "20px", justifyContent: "center", flexWrap: "wrap"}}>
                    <form onSubmit={checkAvailability} style={{display: "flex", flexDirection: "column", gap: "10px", width: "300px", padding: "20px", border: "1px solid #ddd", borderRadius: "8px"}}>
                        <h3>Sprawdź dostępność</h3>
                        <label>Specjalizacja:</label>
                        <select name="specialization" value={availFormData.specialization} onChange={handleAvailInputChange}>
                            <option value="Kardiolog">Kardiolog</option>
                            <option value="Dermatolog">Dermatolog</option>
                            <option value="Neurolog">Neurolog</option>
                            <option value="Pediatra">Pediatra</option>
                            <option value="Ortopeda">Ortopeda</option>
                            <option value="Ginekolog">Ginekolog</option>
                            <option value="Psychiatra">Psychiatra</option>
                        </select>
                        <label>Początek:</label>
                        <input name="startDate" type="datetime-local" value={availFormData.startDate} onChange={handleAvailInputChange} required />
                        <label>Koniec:</label>
                        <input name="endDate" type="datetime-local" value={availFormData.endDate} onChange={handleAvailInputChange} required />
                        <button type="submit">Szukaj</button>
                    </form>

                    <form onSubmit={handleAddSchedule} style={{display: "flex", flexDirection: "column", gap: "10px", width: "300px", padding: "20px", border: "1px solid #ddd", borderRadius: "8px"}}>
                        <h3>Dodaj Dyżur</h3>
                        <input name="doctorId" type="number" placeholder="ID Doktora" value={formData.doctorId} onChange={handleInputChange} required />
                        <input name="roomId" type="number" placeholder="ID Gabinetu" value={formData.roomId} onChange={handleInputChange} required />
                        <label>Data rozpoczęcia:</label>
                        <input name="startDate" type="datetime-local" value={formData.startDate} onChange={handleInputChange} required />
                        <label>Data zakończenia:</label>
                        <input name="endDate" type="datetime-local" value={formData.endDate} onChange={handleInputChange} required />
                        <button type="submit">Zapisz Dyżur</button>
                    </form>
                </div>

                {(availableResources.doctors.length > 0 || availableResources.rooms.length > 0) && (
                    <div style={{marginTop: "20px", width: "80%", borderTop: "2px solid #eee", paddingTop: "20px"}}>
                        <h3>Dostępni lekarze i gabinety:</h3>
                        <div style={{display: "flex", gap: "20px"}}>
                            <div style={{flex: 1}}>
                                <h4>Lekarze:</h4>
                                <ul>
                                    {availableResources.doctors.map((doc, idx) => (
                                        <li key={idx} style={{marginBottom: "5px"}}>
                                            {doc} 
                                            <button type="button" onClick={() => fillDoctor(doc)} style={{marginLeft: "10px", fontSize: "0.8em"}}>Wybierz</button>
                                        </li>
                                    ))}
                                    {availableResources.doctors.length === 0 && <li>Brak</li>}
                                </ul>
                            </div>
                            <div style={{flex: 1}}>
                                <h4>Gabinety:</h4>
                                <ul>
                                    {availableResources.rooms.map((roomStr, idx) => (
                                        <li key={idx} style={{marginBottom: "5px"}}>
                                            {roomStr}
                                            <button type="button" onClick={() => fillRoom(roomStr)} style={{marginLeft: "10px", fontSize: "0.8em"}}>Wybierz</button>
                                        </li>
                                    ))}
                                    {availableResources.rooms.length === 0 && <li>Brak</li>}
                                </ul>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};
export default SchedulePage;