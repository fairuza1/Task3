// src/components/sekreter/HastaListesi.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, Table, TableHead, TableRow, TableCell, TableBody, Button, Alert } from "@mui/material";
import { Link } from "react-router-dom";

const HastaListesi = () => {
    const [hastalar, setHastalar] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        hastalariGetir();
    }, []);

    const hastalariGetir = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/hastalar", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setHastalar(res.data);
        } catch (err) {
            setError("Hastalar yüklenemedi. Yetkiniz olmayabilir.");
        }
    };

    const hastaSil = async (id) => {
        if (!window.confirm("Bu hastayı silmek istediğine emin misin?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/hastalar/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            hastalariGetir();
        } catch (err) {
            alert("Hasta silinemedi!");
        }
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                🩺 Hasta Listesi
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Ad Soyad</TableCell>
                        <TableCell>TC Kimlik</TableCell>
                        <TableCell>Telefon</TableCell>
                        <TableCell>Adres</TableCell>
                        <TableCell>Doktor</TableCell>
                        <TableCell>İşlemler</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {hastalar.map((hasta) => (
                        <TableRow key={hasta.id}>
                            <TableCell>{hasta.id}</TableCell>
                            <TableCell>{hasta.adSoyad}</TableCell>
                            <TableCell>{hasta.tcKimlikNo}</TableCell>
                            <TableCell>{hasta.telefon}</TableCell>
                            <TableCell>{hasta.adres}</TableCell>
                            <TableCell>{hasta.doktor?.adSoyad || "Atanmadı"}</TableCell>
                            <TableCell>
                                <Button component={Link} to={`/sekreter/hasta-duzenle/${hasta.id}`} variant="outlined" size="small" sx={{ mr: 1 }}>
                                    ✏️ Güncelle
                                </Button>
                                <Button variant="contained" color="error" size="small" onClick={() => hastaSil(hasta.id)}>
                                    🗑️ Sil
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Container>
    );
};

export default HastaListesi;
