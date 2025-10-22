import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Container, Typography, CircularProgress, Alert, Box } from "@mui/material";

const MuayeneDetay = () => {
    const { id } = useParams(); // URL'deki :id parametresini alır
    const [muayene, setMuayene] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchMuayene = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get(`http://localhost:8080/api/muayeneler/${id}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setMuayene(res.data);
            } catch (err) {
                setError("Muayene bilgileri alınamadı.");
            } finally {
                setLoading(false);
            }
        };
        fetchMuayene();
    }, [id]);

    if (loading)
        return (
            <Container sx={{ mt: 5, textAlign: "center" }}>
                <CircularProgress />
                <Typography mt={2}>Muayene bilgileri yükleniyor...</Typography>
            </Container>
        );

    if (error) return <Alert severity="error">{error}</Alert>;

    if (!muayene) return <Alert severity="info">Muayene bulunamadı.</Alert>;

    return (
        <Container sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>🩺 Muayene Detayı</Typography>

            <Box sx={{ mt: 3 }}>
                <Typography><b>ID:</b> {muayene.id}</Typography>
                <Typography><b>Hasta:</b> {muayene.hasta?.adSoyad}</Typography>
                <Typography><b>Doktor:</b> {muayene.doktor?.adSoyad}</Typography>
                <Typography><b>Tarih:</b> {new Date(muayene.tarih).toLocaleString()}</Typography>
                <Typography><b>Tanı:</b> {muayene.tani}</Typography>

                {muayene.recete && (
                    <Box sx={{ mt: 3 }}>
                        <Typography variant="h6">💊 Reçete Bilgileri</Typography>
                        <Typography>İlaç: {muayene.recete.ilacAdi}</Typography>
                        <Typography>Doz: {muayene.recete.doz}</Typography>
                        <Typography>Açıklama: {muayene.recete.aciklama}</Typography>
                    </Box>
                )}
            </Box>
        </Container>
    );
};

export default MuayeneDetay;
