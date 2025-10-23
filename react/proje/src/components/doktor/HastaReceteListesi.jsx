import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import {
    Container,
    Typography,
    Card,
    CardContent,
    CircularProgress,
    Alert,
    Box,
    Divider,
} from "@mui/material";

const HastaReceteListesi = () => {
    const { hastaId } = useParams();
    const [muayeneler, setMuayeneler] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get(
                    `http://localhost:8080/api/muayeneler/hasta/${hastaId}/receteler`,
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                setMuayeneler(res.data);
            } catch (err) {
                setError("Reçete verileri yüklenemedi. Yetkiniz olmayabilir.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [hastaId]);

    if (loading)
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
                <CircularProgress />
            </Box>
        );

    if (error)
        return (
            <Alert severity="error" sx={{ mt: 4, width: "80%", margin: "auto" }}>
                {error}
            </Alert>
        );

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                💊 Hastanın Reçeteleri
            </Typography>

            {muayeneler.length === 0 ? (
                <Alert severity="info">Bu hastaya ait reçete bulunamadı.</Alert>
            ) : (
                muayeneler.map((m) => (
                    <Card key={m.id} sx={{ mb: 3, borderRadius: 3, boxShadow: 3 }}>
                        <CardContent>
                            <Typography variant="h6">
                                🩺 Tanı: {m.tani}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Muayene ID: {m.id}
                            </Typography>
                            <Divider sx={{ my: 1 }} />

                            {m.recete ? (
                                <>
                                    <Typography variant="subtitle1" sx={{ mt: 1 }}>
                                        <b>İlaç:</b> {m.recete.ilacAdi}
                                    </Typography>
                                    <Typography variant="subtitle1">
                                        <b>Doz:</b> {m.recete.doz}
                                    </Typography>
                                    <Typography variant="subtitle1">
                                        <b>Açıklama:</b> {m.recete.aciklama}
                                    </Typography>
                                </>
                            ) : (
                                <Typography color="text.secondary">
                                    📭 Bu muayenede reçete bulunmuyor.
                                </Typography>
                            )}
                        </CardContent>
                    </Card>
                ))
            )}
        </Container>
    );
};

export default HastaReceteListesi;
