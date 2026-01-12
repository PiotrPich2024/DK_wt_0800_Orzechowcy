import React , {useEffect, useState} from "react";
import { useNavigate, Link } from "react-router";

const HomePage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}}>Strona główna przychodni</h1>
            <Link to="/doctors"> Strona doktorów </Link>
            <Link to="/rooms"> Strona gabinetów </Link>
            <Link to="/patients"> Strona pacjentów </Link>
            <Link to="/schedules"> Strona dyżurów </Link>
        </div>
    );
};
export default HomePage;

