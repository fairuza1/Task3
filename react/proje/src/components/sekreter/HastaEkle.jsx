import React, { useState, useEffect } from "react";
import axios from "axios";
import { Container, TextField, Button, Typography, Alert, MenuItem } from "@mui/material";

const HastaEkle = () => {
    const [adSoyad, setAdSoyad] = useState("");
    const [tcKimlikNo, setTcKimlikNo] = useState("");
    const [dogumTarihi, setDogumTarihi] = useState("");
    const [telefon, setTelefon] = useState("");
    const [adres, setAdres] = useState("");
    const [doktorId, setDoktorId] = useState(""); // 📌 seçilen doktor ID
    const [doktorlar, setDoktorlar] = useState([]);
    const [mesaj, setMesaj] = useState("");

    // 📡 Doktorları sayfa açıldığında getir
    useEffect(() => {
        const fetchDoktorlar = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get("http://localhost:8080/api/doktorlar", {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setDoktorlar(res.data);
            } catch (err) {
                console.error("❌ Doktorlar alınamadı:", err);
            }
        };
        fetchDoktorlar();
    }, []);

    // 🩺 Yeni hasta ekle
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");

            // 📌 doktorId varsa doktor nesnesi olarak gönder
            const hastaData = {
                adSoyad,
                tcKimlikNo,
                dogumTarihi,
                telefon,
                adres,
                doktor: doktorId ? { id: doktorId } : null,
            };

            await axios.post("http://localhost:8080/api/hastalar", hastaData, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setMesaj("✅ Hasta başarıyla eklendi!");
            setAdSoyad("");
            setTcKimlikNo("");
            setDogumTarihi("");
            setTelefon("");
            setAdres("");
            setDoktorId("");
        } catch (err) {
            console.error(err);
            setMesaj("❌ Hasta eklenemedi. Yetkiniz olmayabilir.");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                ➕ Yeni Hasta Ekle
            </Typography>

            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="Ad Soyad"
                    margin="normal"
                    value={adSoyad}
                    onChange={(e) => setAdSoyad(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="TC Kimlik No"
                    margin="normal"
                    value={tcKimlikNo}
                    onChange={(e) => setTcKimlikNo(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Doğum Tarihi"
                    type="date"
                    margin="normal"
                    InputLabelProps={{ shrink: true }}
                    value={dogumTarihi}
                    onChange={(e) => setDogumTarihi(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Telefon"
                    margin="normal"
                    value={telefon}
                    onChange={(e) => setTelefon(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Adres"
                    margin="normal"
                    value={adres}
                    onChange={(e) => setAdres(e.target.value)}
                />

                {/* 📌 Doktor seçimi */}
                <TextField
                    select
                    fullWidth
                    label="Doktor Seç.."
                    margin="normal"
                    value={doktorId}
                    onChange={(e) => setDoktorId(e.target.value)}
                >
                    <MenuItem value="">Doktor Seçiniz</MenuItem>
                    {doktorlar.map((doktor) => (
                        <MenuItem key={doktor.id} value={doktor.id}>
                            {doktor.adSoyad} – {doktor.uzmanlikAlani}
                        </MenuItem>
                    ))}
                </TextField>

                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Kaydet
                </Button>
            </form>

            {mesaj && (
                <Alert severity={mesaj.startsWith("✅") ? "success" : "error"} sx={{ mt: 2 }}>
                    {mesaj}
                </Alert>
            )}
        </Container>
    );
};

export default HastaEkle;
