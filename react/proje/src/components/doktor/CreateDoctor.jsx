import React, { useState, useEffect } from "react";
import axios from "axios";
import {
    Container,
    TextField,
    Button,
    Typography,
    Alert,
    MenuItem,
    FormControl,
    InputLabel,
    Select,
    Paper,
    CircularProgress,
    Box,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const CreateDoctor = () => {
    const [form, setForm] = useState({
        adSoyad: "",
        uzmanlikAlani: "",
        telefon: "",
        kullaniciId: "",
    });
    const [users, setUsers] = useState([]); // 🔹 Rolü DOKTOR olan kullanıcılar
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    // 🔹 Sayfa açıldığında kullanıcı listesini çek
    useEffect(() => {
        fetchDoctorUsers();
    }, []);

    const fetchDoctorUsers = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(
                "http://localhost:8080/api/kullanicilar/role/DOKTOR",
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );
            setUsers(res.data);
        } catch (err) {
            console.error("❌ Kullanıcılar alınamadı:", err);
            setError("Kullanıcı listesi alınamadı. Yetkiniz olmayabilir.");
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) =>
        setForm({ ...form, [e.target.name]: e.target.value });

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
                    kullanici: { id: parseInt(form.kullaniciId) },
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );
            navigate("/doktorlar");
        } catch (err) {
            console.error("❌ Doktor eklenemedi:", err);
            setError("Doktor eklenemedi. Yetkiniz olmayabilir.");
        }
    };

    if (loading) {
        return (
            <Container sx={{ mt: 5, textAlign: "center" }}>
                <CircularProgress />
                <Typography mt={2}>Kullanıcılar yükleniyor...</Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Paper elevation={3} sx={{ p: 4, borderRadius: 3 }}>
                <Typography variant="h4" gutterBottom>
                    ➕ Yeni Doktor Ekle
                </Typography>

                {error && <Alert severity="error">{error}</Alert>}

                <form onSubmit={handleSubmit}>
                    {/* 🔹 Kullanıcı seçimi */}
                    <FormControl fullWidth sx={{ mb: 2 }}>
                        <InputLabel>Kullanıcı (rolü DOKTOR)</InputLabel>
                        <Select
                            name="kullaniciId"
                            value={form.kullaniciId}
                            onChange={handleChange}
                            required
                        >
                            {users.length > 0 ? (
                                users.map((user) => (
                                    <MenuItem key={user.id} value={user.id}>
                                        {user.kullaniciAdi} ({user.email})
                                    </MenuItem>
                                ))
                            ) : (
                                <MenuItem disabled>⚠️ Uygun kullanıcı bulunamadı</MenuItem>
                            )}
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Ad Soyad"
                        name="adSoyad"
                        value={form.adSoyad}
                        onChange={handleChange}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        fullWidth
                        label="Uzmanlık Alanı"
                        name="uzmanlikAlani"
                        value={form.uzmanlikAlani}
                        onChange={handleChange}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        fullWidth
                        label="Telefon"
                        name="telefon"
                        value={form.telefon}
                        onChange={handleChange}
                        sx={{ mb: 2 }}
                    />

                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Kaydet
                    </Button>
                </form>
            </Paper>
        </Container>
    );
};

export default CreateDoctor;
