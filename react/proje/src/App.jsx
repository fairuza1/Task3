// src/App.jsx
import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import axios from "axios";
axios.defaults.withCredentials = true; // 🍪 Cookie gönderimini aç

// components altındaki dosyalara göre import et
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

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    return (
        <Router>
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
            </Routes>
        </Router>
    );
}

export default App;
