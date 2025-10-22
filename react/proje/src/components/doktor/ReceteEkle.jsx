import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Container, Typography, TextField, Button, Alert } from "@mui/material";

const ReceteEkle = () => {
    const { id } = useParams(); // muayeneId
    const [ilacAdi, setIlacAdi] = useState("");
    const [doz, setDoz] = useState("");
    const [aciklama, setAciklama] = useState("");
    const [mesaj, setMesaj] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.post(
                "http://localhost:8080/api/receteler",
                { muayeneId: id, ilacAdi, doz, aciklama },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            setMesaj("✅ Reçete başarıyla oluşturuldu!");
            setTimeout(() => navigate("/doktor/muayeneler"), 1500);
        } catch {
            setMesaj("❌ Reçete oluşturulamadı!");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                💊 Reçete Oluştur
            </Typography>

            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="İlaç Adı"
                    margin="normal"
                    value={ilacAdi}
                    onChange={(e) => setIlacAdi(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Doz"
                    margin="normal"
                    value={doz}
                    onChange={(e) => setDoz(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Açıklama"
                    margin="normal"
                    multiline
                    rows={3}
                    value={aciklama}
                    onChange={(e) => setAciklama(e.target.value)}
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

export default ReceteEkle;
