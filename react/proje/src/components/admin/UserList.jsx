import React, { useEffect, useState } from "react";
import axios from "axios";
import { Container, Typography, Table, TableHead, TableRow, TableCell, TableBody, Alert } from "@mui/material";

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get("http://localhost:8080/api/kullanicilar", {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                });
                setUsers(res.data);
            } catch (err) {
                setError("Kullanıcılar yüklenemedi. Yetkiniz olmayabilir.");
            }
        };
        fetchUsers();
    }, []);

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                👥 Kullanıcı Listesi
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>ID</TableCell>
                        <TableCell>Kullanıcı Adı</TableCell>
                        <TableCell>Email</TableCell>
                        <TableCell>Rol</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {users.map((user) => (
                        <TableRow key={user.id}>
                            <TableCell>{user.id}</TableCell>
                            <TableCell>{user.kullaniciAdi}</TableCell>
                            <TableCell>{user.email}</TableCell>
                            <TableCell>{user.rol}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Container>
    );
};

export default UserList;
