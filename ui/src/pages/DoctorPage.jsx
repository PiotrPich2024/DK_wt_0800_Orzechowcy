import React , {useEffect, useState} from "react";
import { useNavigate, Link } from "react-router";

const DoctorPage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona doktorów</h1>
            <Link to="/"> Strona główna </Link>
        </div>
    );
};
export default DoctorPage;