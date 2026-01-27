import React , {useEffect, useState} from "react";
import { Link } from "react-router";

const HomePage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona główna przychodni</h1>
            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", marginTop: "20px"}}>
                <Link to="/doctors"> Strona doktorów </Link>
                <Link to="/rooms"> Strona gabinetów </Link>
                <Link to="/patients"> Strona pacjentów </Link>
                <Link to="/schedules"> Strona dyżurów </Link>
                <Link to="/appointment"> Strona spotkań</Link>
            </div>
        </div>
    );
};
export default HomePage;

