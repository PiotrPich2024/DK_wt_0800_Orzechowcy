import React, { useEffect, useState } from "react";
import { Link } from "react-router";
import api from "../services/api.js";

const RoomPage = () => {
    const [roomsList, setRoomsList] = useState([]);
    const [formData, setFormData] = useState({
        roomNumber: "",
        roomDescription: ""
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const showRooms = async () => {
        try {
            const response = await api.get("/room");
            setRoomsList(response.data);
        } catch (error) {
            console.error("Błąd pobierania gabinetów:", error);
        }
    };

    const handleAddRoom = async (e) => {
        e.preventDefault();
        try {
            await api.post("/room/add", {
                roomNumber: Number(formData.roomNumber),
                roomDescription: formData.roomDescription
            });

            alert("Gabinet dodany!");
            setFormData({ roomNumber: "", roomDescription: "" });
            showRooms();
        } catch (error) {
            console.error("Błąd dodawania gabinetu:", error);
            alert("Nie udało się dodać gabinetu");
        }
    };

    const handleDeleteRoom = async (id) => {
    if (!window.confirm("Czy na pewno chcesz usunąć ten gabinet?")) return;

    try {
        const response = await api.get(`/room/delete/${id}`);
        if (response.data === true) {
            alert("Gabinet usunięty.");
            showRooms();
        } else if (response.data === false) {
            alert(
                "Nie można usunąć gabinetu – ma przypisane dyżury. " +
                "Najpierw usuń powiązane dyżury lub przenieś je do innych gabinetów."
            );
        } else {
            alert("Nie udało się usunąć gabinetu. Spróbuj ponownie.");
        }
    } catch (error) {
        console.error("Błąd usuwania gabinetu:", error);
        alert("Wystąpił błąd podczas usuwania gabinetu. Spróbuj ponownie.");
    }
    };

    useEffect(() => {
        showRooms();
    }, []);

    return (
        <div>
            <h1 style={{ textAlign: "center" }}>Strona gabinetów</h1>

            <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: "15px" }}>
                <Link to="/">Strona główna</Link>

                {roomsList.length > 0 && (
                    <div style={{ width: "80%" }}>
                        <h2>Lista gabinetów</h2>

                        {roomsList.map((room, index) => (
                            <div
                                key={index}
                                style={{
                                    border: "1px solid #ccc",
                                    padding: "10px",
                                    marginBottom: "10px",
                                    borderRadius: "6px",
                                    backgroundColor: "#f9f9f9"
                                }}
                            >
                                <p><strong>Numer:</strong> {room.roomNumber}</p>
                                <p><strong>Opis:</strong> {room.roomDescription}</p>

                                {/* JEŚLI DTO MA id → użyj room.id */}
                                <button
                                    onClick={() => handleDeleteRoom(room.id)}
                                    style={{
                                        backgroundColor: "#ff4444",
                                        color: "white",
                                        border: "none",
                                        padding: "5px 10px",
                                        cursor: "pointer",
                                        borderRadius: "4px"
                                    }}
                                >
                                    Usuń
                                </button>
                            </div>
                        ))}
                    </div>
                )}

                <form
                    onSubmit={handleAddRoom}
                    style={{
                        width: "300px",
                        padding: "20px",
                        border: "1px solid #ddd",
                        borderRadius: "8px",
                        display: "flex",
                        flexDirection: "column",
                        gap: "10px"
                    }}
                >
                    <h3>Dodaj gabinet</h3>

                    <input
                        type="number"
                        name="roomNumber"
                        placeholder="Numer gabinetu"
                        value={formData.roomNumber}
                        onChange={handleInputChange}
                        required
                    />

                    <input
                        type="text"
                        name="roomDescription"
                        placeholder="Opis"
                        value={formData.roomDescription}
                        onChange={handleInputChange}
                        required
                    />

                    <button type="submit">Zapisz</button>
                </form>
            </div>
        </div>
    );
};

export default RoomPage;
