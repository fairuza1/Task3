import React, { useState, useEffect } from "react";
import axios from "axios";
import { Container, Typography, TextField, Button, MenuItem, Alert } from "@mui/material";

const MuayeneEkle = () => {
    const [hastalar, setHastalar] = useState([]);
    const [hastaId, setHastaId] = useState("");
    const [tani, setTani] = useState("");
    const [mesaj, setMesaj] = useState("");

    useEffect(() => {
        fetchHastalar();
    }, []);

    const fetchHastalar = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/hastalar", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setHastalar(res.data);
        } catch {
            setMesaj("Hastalar yüklenemedi!");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const doktorId = localStorage.getItem("userId"); // Giriş yapan doktorun ID'si
            await axios.post(
                "http://localhost:8080/api/muayeneler",
                {
                    doktorId: doktorId,
                    hastaId: hastaId,
                    tani: tani
                },
                { headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }}
            );


            setMesaj("✅ Muayene başarıyla eklendi!");
            setHastaId("");
            setTani("");
        } catch {
            setMesaj("❌ Muayene eklenemedi.");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                ➕ Yeni Muayene Ekle
            </Typography>

            <form onSubmit={handleSubmit}>
                <TextField
                    select
                    fullWidth
                    label="Hasta Seç"
                    margin="normal"
                    value={hastaId}
                    onChange={(e) => setHastaId(e.target.value)}
                >
                    {hastalar.map((h) => (
                        <MenuItem key={h.id} value={h.id}>
                            {h.adSoyad} ({h.tcKimlikNo})
                        </MenuItem>
                    ))}
                </TextField>

                <TextField
                    fullWidth
                    label="Tanı"
                    margin="normal"
                    multiline
                    rows={3}
                    value={tani}
                    onChange={(e) => setTani(e.target.value)}
                />

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

export default MuayeneEkle;
