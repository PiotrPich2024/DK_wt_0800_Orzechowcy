import React , {useEffect, useState} from "react";
import { Link } from "react-router";
import api from "../services/api.js";

class Room {
    constructor(id, roomNumber, description) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.description = description;
    }
}

const RoomPage = () => {
    const [roomsList, setRoomsList] = useState([]);
    const [formData, setFormData] = useState({
        roomNumber: "",
        description: ""
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleAddRoom = async (e) => {
        e.preventDefault();
        try {
            await api.post("/room/add", formData);
            alert("Gabinet dodany!");
            showRooms();
            setFormData({ roomNumber: "", description: "" });
        } catch (error) {
            console.error("Error adding room:", error);
            alert("Błąd podczas dodawania gabinetu");
        }
    };

    const handleDeleteRoom = async (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć ten gabinet?")) return;
        try {
            const response = await api.get(`/room/delete/${id}`);
            if (response.data === true) {
                alert("Gabinet usunięty.");
                showRooms();
            } else {
                alert("Nie można usunąć gabinetu. Prawdopodobnie ma przypisane dyżury.");
            }
        } catch (error) {
            console.error("Error deleting room:", error);
            alert("Błąd podczas usuwania gabinetu.");
        }
    };

    const parseRoom = (roomString) => {
        try {
            const parts = roomString.split(', ');
            const getVal = (key) => {
                const part = parts.find(p => p.startsWith(key));
                return part ? part.split(': ')[1] : '';
            };

            const id = getVal("ID gabinetu");
            if (!id) return null;

            return new Room(
                parseInt(id),
                getVal("Numer gabinetu"),
                getVal("Opis")
            );
        } catch (e) {
            console.error("Error parsing room string:", roomString, e);
            return null;
        }
    };

    useEffect(() => {
        showRooms();
    }, []);
    const showRooms = async () => {
        try {
            const response = await api.get("/room"); 
            console.log("Response data:", response.data);
            
            let dataToParse = response.data;
            if(!Array.isArray(dataToParse)) {
                console.warn("Response data is not an array", dataToParse);
                return;
            }

            const rooms = dataToParse.map(r => parseRoom(r)).filter(r => r !== null);
            setRoomsList(rooms);
        } catch (error) {
            console.error("There was a problem with the fetch operation:", error);
        }
    };

    return (
        <div>
            <h1 style={{textAlign: "center"}} >Strona gabinetów</h1>
            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", marginTop: "20px"}}>
                <Link to="/"> Strona główna </Link>
                {roomsList && roomsList.length > 0 && (
                     <div style={{marginTop: "20px", width: "80%"}}>
                        <h2>Lista gabinetów:</h2>
                        {roomsList.map((room) => (
                            <div key={room.id} style={{
                                border: "1px solid #ccc",
                                padding: "10px",
                                margin: "10px 0",
                                borderRadius: "5px",
                                backgroundColor: "#f9f9f9"
                            }}>
                                <p><strong>ID:</strong> {room.id}</p>
                                <p><strong>Numer:</strong> {room.roomNumber}</p>
                                <p><strong>Opis:</strong> {room.description}</p>
                                <button onClick={() => handleDeleteRoom(room.id)} style={{backgroundColor: "#ff4444", color: "white", border: "none", padding: "5px 10px", marginTop: "5px", cursor: "pointer", borderRadius: "3px"}}>Usuń</button>
                            </div>
                        ))}
                    </div>
                )}

                <form onSubmit={handleAddRoom} style={{display: "flex", flexDirection: "column", gap: "10px", width: "300px", padding: "20px", border: "1px solid #ddd", borderRadius: "8px", marginTop: "20px"}}>
                    <h3>Dodaj Gabinet</h3>
                    <input name="roomNumber" type="number" placeholder="Numer gabinetu" value={formData.roomNumber} onChange={handleInputChange} required />
                    <input name="description" placeholder="Opis" value={formData.description} onChange={handleInputChange} required />
                    <button type="submit">Zapisz Gabinet</button>
                </form>
            </div>
        </div>
    );
};
export default RoomPage;