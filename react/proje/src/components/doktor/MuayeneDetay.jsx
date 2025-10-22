import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { Container, Typography, Paper, Button, Alert } from "@mui/material";

const MuayeneDetay = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [muayene, setMuayene] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchMuayene();
    }, []);

    const fetchMuayene = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:8080/api/muayeneler/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMuayene(res.data);
        } catch {
            setError("❌ Muayene bilgileri yüklenemedi!");
        }
    };

    if (error) return <Alert severity="error">{error}</Alert>;
    if (!muayene) return <Typography>Yükleniyor...</Typography>;

    return (
        <Container maxWidth="md" sx={{ mt: 5 }}>
            <Paper elevation={3} sx={{ p: 4, borderRadius: 3 }}>
                <Typography variant="h4" gutterBottom>
                    🩺 Muayene Detayı
                </Typography>
                <Typography variant="body1" sx={{ mb: 1 }}>
                    <strong>Hasta:</strong> {muayene.hasta?.adSoyad}
                </Typography>
                <Typography variant="body1" sx={{ mb: 1 }}>
                    <strong>Doktor:</strong> {muayene.doktor?.adSoyad}
                </Typography>
                <Typography variant="body1" sx={{ mb: 1 }}>
                    <strong>Tarih:</strong> {new Date(muayene.tarih).toLocaleString()}
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                    <strong>Tanı:</strong> {muayene.tani}
                </Typography>

                <Button
                    variant="contained"
                    color="success"
                    onClick={() => navigate(`/doktor/recete-ekle/${muayene.id}`)}
                >
                    💊 Reçete Yaz
                </Button>
            </Paper>
        </Container>
    );
};

export default MuayeneDetay;
