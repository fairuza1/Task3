import React, { useState } from "react";
import axios from "axios";
import { Container, TextField, Button, Typography, MenuItem, Alert } from "@mui/material";

const CreateUser = () => {
    const [kullaniciAdi, setKullaniciAdi] = useState("");
    const [sifre, setSifre] = useState("");
    const [email, setEmail] = useState("");
    const [rol, setRol] = useState("");
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
            setRol("");
        } catch (err) {
            setMessage("❌ Kullanıcı oluşturulamadı. Yetkiniz olmayabilir veya veriler hatalı.");
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
                    required
                />
                <TextField
                    fullWidth
                    label="Email"
                    type="email"
                    margin="normal"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <TextField
                    fullWidth
                    label="Şifre"
                    type="password"
                    margin="normal"
                    value={sifre}
                    onChange={(e) => setSifre(e.target.value)}
                    required
                />
                <TextField
                    select
                    fullWidth
                    label="Rol Seç"
                    margin="normal"
                    value={rol}
                    onChange={(e) => setRol(e.target.value)}
                    required
                >
                    <MenuItem value="ADMIN">👑 ADMIN</MenuItem>
                    <MenuItem value="BAS_DOKTOR">🩺 BAS_DOKTOR</MenuItem>
                    <MenuItem value="DOKTOR">👨‍⚕️ DOKTOR</MenuItem>
                    <MenuItem value="SEKRETER">📞 SEKRETER</MenuItem>
                    <MenuItem value="USER">👤 USER</MenuItem>
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
