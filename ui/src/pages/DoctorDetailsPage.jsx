import { useParams } from "react-router";
import { useEffect, useState } from "react";
import api from "../services/api";

const DoctorDetailsPage = () => {
    const { id } = useParams();
    console.log(id);
    const [doctor, setDoctor] = useState(null);
    const [schedules, setSchedules] = useState([]);

    useEffect(() => {
        api.get(`/doctors/${id}`).then(res => setDoctor(res.data));
        api.get(`/doctors/${id}/schedules`).then(res => setSchedules(res.data));
    }, [id]);

    if (!doctor) return <div>Ładowanie...</div>;

    return (
        <div>
            <h2>{doctor.firstName} {doctor.lastName}</h2>
            <p>Specjalizacja: {doctor.specialty}</p>

            <h3>Dyżury</h3>
            {schedules.length === 0 && <p>Brak dyżurów</p>}
            {schedules.map(s => (
                <div key={s.id}>
                    <p>{s.day} {s.startTime} - {s.endTime}</p>
                </div>
            ))}
        </div>
    );
};

export default DoctorDetailsPage;
