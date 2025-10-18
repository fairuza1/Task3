import React, { useState } from "react";
import axios from "axios";
import { Container, TextField, Button, Typography, MenuItem, Alert } from "@mui/material";

const CreateUser = () => {
    const [kullaniciAdi, setKullaniciAdi] = useState("");
    const [sifre, setSifre] = useState("");
    const [email, setEmail] = useState("");
    const [rol, setRol] = useState("USER");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.post(
                "http://localhost:8080/api/kullanicilar",
                { kullaniciAdi, sifre, email, rol },
                {
                    headers: { Authorization: `Bearer ${token}` },
                    withCredentials: true,
                }
            );
            setMessage("✅ Kullanıcı başarıyla oluşturuldu!");
            setKullaniciAdi("");
            setSifre("");
            setEmail("");
            setRol("USER");
        } catch (err) {
            setMessage("❌ Kullanıcı oluşturulamadı. Yetkiniz olmayabilir.");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                ➕ Yeni Kullanıcı Oluştur
            </Typography>

            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="Kullanıcı Adı"
                    margin="normal"
                    value={kullaniciAdi}
                    onChange={(e) => setKullaniciAdi(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Email"
                    margin="normal"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <TextField
                    fullWidth
                    label="Şifre"
                    type="password"
                    margin="normal"
                    value={sifre}
                    onChange={(e) => setSifre(e.target.value)}
                />
                <TextField
                    select
                    fullWidth
                    label="Rol"
                    margin="normal"
                    value={rol}
                    onChange={(e) => setRol(e.target.value)}
                >
                    <MenuItem value="ADMIN">ADMIN</MenuItem>
                    <MenuItem value="DOKTOR">DOKTOR</MenuItem>
                    <MenuItem value="SEKRETER">SEKRETER</MenuItem>
                    <MenuItem value="USER">USER</MenuItem>
                </TextField>

                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Oluştur
                </Button>
            </form>

            {message && (
                <Alert severity={message.startsWith("✅") ? "success" : "error"} sx={{ mt: 2 }}>
                    {message}
                </Alert>
            )}
        </Container>
    );
};

export default CreateUser;
