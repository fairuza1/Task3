import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, Table, TableHead, TableRow, TableCell, TableBody, Button, Alert, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";

const DoctorList = () => {
    const [doctors, setDoctors] = useState([]);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchDoctors();
    }, []);

    const fetchDoctors = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/doktorlar", {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
            });
            setDoctors(res.data);
        } catch (err) {
            setError("Doktorlar yüklenemedi. Yetkiniz olmayabilir.");
        }
    };

    const deleteDoctor = async (id) => {
        if (!window.confirm("Bu doktoru silmek istediğinizden emin misiniz?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/doktorlar/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
            });
            fetchDoctors(); // Listeyi yenile
        } catch (err) {
            setError("Silme işlemi başarısız. Yetkiniz olmayabilir.");
        }
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                👨‍⚕️ Doktor Listesi
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <Box sx={{ mb: 2 }}>
                <Button variant="contained" color="primary" onClick={() => navigate("/doktorlar/ekle")}>
                    ➕ Yeni Doktor Ekle
                </Button>
            </Box>

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Ad Soyad</TableCell>
                        <TableCell>Uzmanlık Alanı</TableCell>
                        <TableCell>Telefon</TableCell>
                        <TableCell>İşlemler</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {doctors.map((doc) => (
                        <TableRow key={doc.id}>
                            <TableCell>{doc.id}</TableCell>
                            <TableCell>{doc.adSoyad}</TableCell>
                            <TableCell>{doc.uzmanlikAlani}</TableCell>
                            <TableCell>{doc.telefon}</TableCell>
                            <TableCell>
                                <Button variant="outlined" color="primary" sx={{ mr: 1 }} onClick={() => navigate(`/doktorlar/duzenle/${doc.id}`)}>
                                    ✏️ Düzenle
                                </Button>
                                <Button variant="outlined" color="error" onClick={() => deleteDoctor(doc.id)}>
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

export default DoctorList;
