import React , {useEffect, useState} from "react";
import { useNavigate, Link } from "react-router";

const SchedulePage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona dyżurów</h1>
            <Link to="/"> Strona główna </Link>
        </div>
    );
};
export default SchedulePage;