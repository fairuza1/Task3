import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, Table, TableHead, TableRow, TableCell, TableBody, Alert, Button } from "@mui/material";
import { Link } from "react-router-dom";

const MuayeneListesi = () => {
    const [muayeneler, setMuayeneler] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchMuayeneler();
    }, []);

    const fetchMuayeneler = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/muayeneler", {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMuayeneler(res.data);
        } catch (err) {
            setError("Muayeneler yüklenemedi!");
        }
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                🩺 Muayene Listesi
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Hasta</TableCell>
                        <TableCell>Doktor</TableCell>
                        <TableCell>Tarih</TableCell>
                        <TableCell>Tanı</TableCell>
                        <TableCell>Reçete</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {muayeneler.map((m) => (
                        <TableRow key={m.id}>
                            <TableCell>{m.id}</TableCell>
                            <TableCell>{m.hasta?.adSoyad}</TableCell>
                            <TableCell>{m.doktor?.adSoyad}</TableCell>
                            <TableCell>{new Date(m.tarih).toLocaleString()}</TableCell>
                            <TableCell>{m.tani}</TableCell>
                            <TableCell>
                                <Button
                                    component={Link}
                                    to={`/doktor/recete-ekle/${m.id}`}
                                    variant="outlined"
                                    size="small"
                                >
                                    💊 Reçete Yaz
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Container>
    );
};

export default MuayeneListesi;
