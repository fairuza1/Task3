import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import {
    Container,
    Typography,
    Table,
    TableHead,
    TableRow,
    TableCell,
    TableBody,
    Alert,
    Button,
    Box,
} from "@mui/material";

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    // 📥 Kullanıcıları çek
    const fetchUsers = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get("http://localhost:8080/api/kullanicilar", {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
            });
            setUsers(res.data);
            setError("");
        } catch (err) {
            setError("Kullanıcılar yüklenemedi. Yetkiniz olmayabilir.");
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    // 🗑️ Kullanıcı silme
    const handleDelete = async (id) => {
        if (!window.confirm("Bu kullanıcıyı silmek istediğine emin misin?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/kullanicilar/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setSuccess("🗑️ Kullanıcı başarıyla silindi.");
            fetchUsers(); // listeyi güncelle
        } catch (err) {
            console.error(err);
            setError("❌ Silme işlemi başarısız oldu.");
        }
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                👥 Kullanıcı Listesi
            </Typography>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Kullanıcı Adı</TableCell>
                        <TableCell>Email</TableCell>
                        <TableCell>Rol</TableCell>
                        <TableCell>Durum</TableCell>
                        <TableCell>İşlemler</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {users.map((user) => (
                        <TableRow key={user.id}>
                            <TableCell>{user.id}</TableCell>
                            <TableCell>{user.kullaniciAdi}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>{user.rol}</TableCell>
                            <TableCell>{user.aktif ? "✅ Aktif" : "⛔ Pasif"}</TableCell>
                            <TableCell>
                                <Box sx={{ display: "flex", gap: 1 }}>
                                    {/* ✏️ Güncelle butonu */}
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        size="small"
                                        component={Link}
                                        to={`/admin/users/edit/${user.id}`}
                                    >
                                        ✏️ Güncelle
                                    </Button>

                                    {/* 🗑️ Sil butonu */}
                                    <Button
                                        variant="outlined"
                                        color="error"
                                        size="small"
                                        onClick={() => handleDelete(user.id)}
                                    >
                                        🗑️ Sil
                                    </Button>
                                </Box>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Container>
    );
};

export default UserList;
