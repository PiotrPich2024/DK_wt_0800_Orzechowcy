import React , {useEffect, useState} from "react";
import { useNavigate, Link } from "react-router";

const PatientPage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona pacjentów</h1>
            <Link to="/"> Strona główna </Link>
        </div>
    );
};
export default PatientPage;