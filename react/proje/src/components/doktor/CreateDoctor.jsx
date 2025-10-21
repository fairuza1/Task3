import React, { useState } from "react";
import axios from "axios";
import { Container, TextField, Button, Typography, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";

const CreateDoctor = () => {
    const [form, setForm] = useState({ adSoyad: "", uzmanlikAlani: "", telefon: "", kullaniciId: "" });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.post(
                "http://localhost:8080/api/doktorlar",
                {
                    adSoyad: form.adSoyad,
                    uzmanlikAlani: form.uzmanlikAlani,
                    telefon: form.telefon,
                    kullanici: { id: parseInt(form.kullaniciId) }
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );
            navigate("/doktorlar");
        } catch (err) {
            setError("Doktor eklenemedi. Yetkiniz olmayabilir.");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>➕ Yeni Doktor Ekle</Typography>
            {error && <Alert severity="error">{error}</Alert>}
            <form onSubmit={handleSubmit}>
                <TextField fullWidth label="Ad Soyad" name="adSoyad" value={form.adSoyad} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Uzmanlık Alanı" name="uzmanlikAlani" value={form.uzmanlikAlani} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Telefon" name="telefon" value={form.telefon} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Kullanıcı ID" name="kullaniciId" value={form.kullaniciId} onChange={handleChange} sx={{ mb: 2 }} />
                <Button type="submit" variant="contained" color="primary">Kaydet</Button>
            </form>
        </Container>
    );
};

export default CreateDoctor;
