import React , {useEffect, useState} from "react";
import { useNavigate, Link } from "react-router";

const RoomPage = () => {
    return (
        <div>
            <h1 style={{textAlign: "center"}} >Strona gabinetów</h1>
            <Link to="/"> Strona główna </Link>
        </div>
    );
};
export default RoomPage;