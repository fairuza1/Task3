import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, Table, TableBody, TableCell, TableHead, TableRow, Paper, Alert } from "@mui/material";

const Muayenelerim = () => {
    const [muayeneler, setMuayeneler] = useState([]);
    const [mesaj, setMesaj] = useState("");

    useEffect(() => {
        fetchMuayeneler();
    }, []);

    const fetchMuayeneler = async () => {
        try {
            const token = localStorage.getItem("token");
            const userId = localStorage.getItem("userId");

            if (!token || !userId) {
                setMesaj("Giriş bilgileri bulunamadı!");
                return;
            }

            const res = await axios.get(`http://localhost:8080/api/muayeneler/doktor/${userId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setMuayeneler(res.data);
        } catch (err) {
            console.error(err);
            setMesaj("Muayeneler yüklenemedi!");
        }
    };

    return (
        <Container sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                👨‍⚕️ Muayenelerim
            </Typography>

            {mesaj && <Alert severity="error">{mesaj}</Alert>}

            {muayeneler.length > 0 ? (
                <Paper sx={{ mt: 2 }}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell><b>ID</b></TableCell>
                                <TableCell><b>Hasta Adı</b></TableCell>
                                <TableCell><b>Tanı</b></TableCell>
                                <TableCell><b>Tarih</b></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {muayeneler.map((m) => (
                                <TableRow key={m.id}>
                                    <TableCell>{m.id}</TableCell>
                                    <TableCell>{m.hasta?.adSoyad}</TableCell>
                                    <TableCell>{m.tani}</TableCell>
                                    <TableCell>{new Date(m.tarih).toLocaleString()}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </Paper>
            ) : (
                !mesaj && <Typography sx={{ mt: 2 }}>Henüz muayene kaydı yok.</Typography>
            )}
        </Container>
    );
};

export default Muayenelerim;
