// src/components/sekreter/HastaDuzenle.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Container, TextField, Button, Typography, Alert, MenuItem } from "@mui/material";

const HastaDuzenle = () => {
    const { id } = useParams();
    const [hasta, setHasta] = useState(null);
    const [doktorId, setDoktorId] = useState("");
    const [doktorlar, setDoktorlar] = useState([]);
    const [mesaj, setMesaj] = useState("");

    useEffect(() => {
        hastaGetir();
        doktorlariGetir();
    }, []);

    const hastaGetir = async () => {
        const token = localStorage.getItem("token");
        const res = await axios.get(`http://localhost:8080/api/hastalar/${id}`, {
            headers: { Authorization: `Bearer ${token}` },
        });
        setHasta(res.data);
    };

    const doktorlariGetir = async () => {
        const token = localStorage.getItem("token");
        const res = await axios.get("http://localhost:8080/api/doktorlar", {
            headers: { Authorization: `Bearer ${token}` },
        });
        setDoktorlar(res.data);
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.put(
                `http://localhost:8080/api/hastalar/${id}`,
                hasta,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setMesaj("✅ Hasta bilgileri güncellendi!");
        } catch {
            setMesaj("❌ Güncelleme başarısız oldu!");
        }
    };

    const doktorAta = async () => {
        const token = localStorage.getItem("token");
        await axios.put(
            `http://localhost:8080/api/hastalar/${id}/doktor/${doktorId}`,
            {},
            { headers: { Authorization: `Bearer ${token}` } }
        );
        setMesaj("✅ Doktor atandı!");
    };

    if (!hasta) return <div>Yükleniyor...</div>;

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>✏️ Hasta Güncelle</Typography>

            <form onSubmit={handleUpdate}>
                <TextField fullWidth label="Ad Soyad" margin="normal" value={hasta.adSoyad} onChange={(e) => setHasta({ ...hasta, adSoyad: e.target.value })} />
                <TextField fullWidth label="Telefon" margin="normal" value={hasta.telefon} onChange={(e) => setHasta({ ...hasta, telefon: e.target.value })} />
                <TextField fullWidth label="Adres" margin="normal" value={hasta.adres} onChange={(e) => setHasta({ ...hasta, adres: e.target.value })} />

                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Güncelle
                </Button>
            </form>

            <Typography variant="h6" sx={{ mt: 4 }}>👨‍⚕️ Doktor Ata</Typography>
            <TextField select fullWidth margin="normal" value={doktorId} onChange={(e) => setDoktorId(e.target.value)}>
                {doktorlar.map((d) => (
                    <MenuItem key={d.id} value={d.id}>{d.adSoyad} - {d.uzmanlikAlani}</MenuItem>
                ))}
            </TextField>
            <Button variant="outlined" fullWidth onClick={doktorAta}>Doktor Ata</Button>

            {mesaj && <Alert sx={{ mt: 2 }} severity={mesaj.startsWith("✅") ? "success" : "error"}>{mesaj}</Alert>}
        </Container>
    );
};

export default HastaDuzenle;
