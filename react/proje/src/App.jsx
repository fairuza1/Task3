import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import axios from "axios";
import Header from "./components/Header";
axios.defaults.withCredentials = true;

import Login from "./components/Login.jsx";
import Signup from "./components/Signup.jsx";
import Home from "./components/Home.jsx";
import AdminDashboard from "./components/admin/AdminDashboard";
import UserList from "./components/admin/UserList";
import CreateUser from "./components/admin/CreateUser";
import EditUser from "./components/admin/EditUser";
import DoctorList from "./components/doktor/DoctorList";
import CreateDoctor from "./components/doktor/CreateDoctor";
import EditDoctor from "./components/doktor/EditDoctor";
import HastaDuzenle from "./components/sekreter/HastaDuzenle.jsx";
import HastaEkle from "./components/sekreter/HastaEkle.jsx";
import SekreterDashboard from "./components/sekreter/SekreterDashboard.jsx";
import HastaListesi from "./components/sekreter/HastaListesi.jsx";
import MuayeneListesi from "./components/doktor/MuayeneListesi.jsx";
import MuayeneEkle from "./components/doktor/MuayeneEkle.jsx";
import ReceteEkle from "./components/doktor/ReceteEkle.jsx";
import MuayeneDetay from "./components/doktor/MuayeneDetay.jsx";
import DoktorDashboard from "./components/doktor/DoktorDashboard.jsx";
import HastaReceteListesi from "./components/doktor/HastaReceteListesi.jsx";
import BasDoktorDashboard from "./components/doktor/BasDoktorDashboard.jsx";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // ✅ Sayfa yenilendiğinde token varsa giriş yapılmış say
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            setIsLoggedIn(true);
        }
    }, []);

    return (
        <Router>
            {isLoggedIn && <Header setIsLoggedIn={setIsLoggedIn} />}

            <Routes>
                <Route path="/" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/home" element={<Home />} />
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/admin/users" element={<UserList />} />
                <Route path="/admin/create-user" element={<CreateUser />} />
                <Route path="/admin/users/edit/:id" element={<EditUser />} />
                <Route path="/doktorlar" element={<DoctorList />} />
                <Route path="/doktorlar/ekle" element={<CreateDoctor />} />
                <Route path="/doktorlar/duzenle/:id" element={<EditDoctor />} />
                <Route path="/sekreter" element={<SekreterDashboard />} />
                <Route path="/sekreter/hasta-listesi" element={<HastaListesi />} />
                <Route path="/sekreter/hasta-ekle" element={<HastaEkle />} />
                <Route path="/sekreter/hasta-duzenle/:id" element={<HastaDuzenle />} />
                <Route path="/doktor/muayeneler" element={<MuayeneListesi />} />
                <Route path="/doktor/muayene-ekle" element={<MuayeneEkle />} />
                <Route path="/doktor/recete-ekle/:id" element={<ReceteEkle />} />
                <Route path="/doktor/muayene-detay/:id" element={<MuayeneDetay />} />
                <Route path="/doktor" element={<DoktorDashboard />} />
                <Route path="/doktor/hasta/:hastaId/receteler" element={<HastaReceteListesi />}/>
                <Route path="/bas-doktor" element={<BasDoktorDashboard />} />
            </Routes>
        </Router>
    );
}

export default App;
