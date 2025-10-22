import React, { useEffect, useState } from "react";
import axios from "axios";
import {
    Container,
    Typography,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Button,
    Alert,
    Box,
    CircularProgress,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const Muayenelerim = () => {
    const [muayeneler, setMuayeneler] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchMuayeneler();
    }, []);

    const fetchMuayeneler = async () => {
        try {
            const token = localStorage.getItem("token");
            const doktorId = localStorage.getItem("userId"); // kullanıcı ID (doktorun kullanici_id'si)

            if (!token || !doktorId) {
                setError("Oturum bulunamadı! Lütfen tekrar giriş yapın.");
                setLoading(false);
                return;
            }

            const url = `http://localhost:8080/api/muayeneler/doktor/${doktorId}`;
            console.log("📡 İstek atılıyor:", url);

            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
            });

            console.log("------ DEBUG ------");
            console.log("Backend yanıt tipi:", typeof res.data);
            console.log("Backend verisi:", res.data);
            console.log("-------------------");

            if (!Array.isArray(res.data)) {
                console.warn("⚠️ Beklenmeyen JSON formatı:", res.data);
                if (res.data?.message) {
                    setError(res.data.message);
                } else if (typeof res.data === "string") {
                    setError(res.data);
                } else {
                    setError("Beklenmeyen veri formatı alındı.");
                }
                setMuayeneler([]);
                return;
            }

            // ✅ Doğru formatta ise
            setMuayeneler(res.data);
        } catch (err) {
            console.error("❌ Muayeneler yüklenemedi:", err);
            if (err.response) {
                console.error("Sunucu yanıtı:", err.response.status, err.response.data);
                if (err.response.status === 403) {
                    setError("Yetkiniz yok veya bu kullanıcıya ait doktor kaydı bulunamadı.");
                } else {
                    setError("Sunucu hatası: " + JSON.stringify(err.response.data));
                }
            } else {
                setError("Sunucuya ulaşılamıyor.");
            }
            setMuayeneler([]);
        } finally {
            setLoading(false);
        }
    };

    const deleteMuayene = async (id) => {
        if (!window.confirm("Bu muayeneyi silmek istediğinizden emin misiniz?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/muayeneler/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
            });
            fetchMuayeneler();
        } catch (err) {
            console.error("❌ Silme işlemi başarısız:", err);
            setError("Silme işlemi başarısız. Yetkiniz olmayabilir.");
        }
    };

    const handleRefresh = () => {
        setLoading(true);
        fetchMuayeneler();
    };

    if (loading) {
        return (
            <Container sx={{ mt: 5, textAlign: "center" }}>
                <CircularProgress />
                <Typography mt={2}>Muayeneler yükleniyor...</Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                🩺 Muayenelerim
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <Box sx={{ mb: 2 }}>
                <Button variant="contained" color="primary" onClick={handleRefresh}>
                    🔄 Yenile
                </Button>
            </Box>

            {muayeneler.length === 0 ? (
                <Alert severity="info">Henüz muayene kaydı bulunmuyor.</Alert>
            ) : (
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Hasta Adı</TableCell>
                            <TableCell>Doktor Adı</TableCell>
                            <TableCell>Tarih</TableCell>
                            <TableCell>Tanı</TableCell>
                            <TableCell>İşlemler</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {muayeneler.map((m) => (
                            <TableRow key={m.id}>
                                <TableCell>{m.id}</TableCell>
                                <TableCell>{m.hasta?.adSoyad || "—"}</TableCell>
                                <TableCell>{m.doktor?.adSoyad || "—"}</TableCell>
                                <TableCell>
                                    {m.tarih ? new Date(m.tarih).toLocaleString() : "—"}
                                </TableCell>
                                <TableCell>{m.tani || "—"}</TableCell>
                                <TableCell>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        sx={{ mr: 1 }}
                                        onClick={() => navigate(`/doktor/muayene-detay/${m.id}`)}
                                    >
                                        🔍 Görüntüle
                                    </Button>
                                    <Button
                                        variant="outlined"
                                        color="error"
                                        onClick={() => deleteMuayene(m.id)}
                                    >
                                        🗑️ Sil
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}
        </Container>
    );
};

export default Muayenelerim;
