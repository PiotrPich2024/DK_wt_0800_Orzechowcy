import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router';
import HomePage from './pages/HomePage';
import DoctorPage from './pages/DoctorPage';
import RoomPage from './pages/RoomPage';
import PatientPage from './pages/PatientPage';
import SchedulePage from './pages/SchedulePage';
import DoctorDetailsPage from './pages/DoctorDetailsPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/doctors" element={<DoctorPage />} />
        <Route path='/doctors/:id' element={<DoctorDetailsPage/>}/>
        <Route path="/rooms" element={<RoomPage />} />
        <Route path="/patients" element={<PatientPage />} />
        <Route path="/schedules" element={<SchedulePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
