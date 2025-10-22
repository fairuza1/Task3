import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import {
    Container,
    Typography,
    TextField,
    Button,
    Box,
    MenuItem,
    FormControlLabel,
    Switch,
    Alert,
    CircularProgress,
} from "@mui/material";

const EditUser = () => {
    const { id } = useParams(); // URL'den kullanıcı ID'si
    const navigate = useNavigate();

    const [user, setUser] = useState({
        kullaniciAdi: "",
        email: "",
        rol: "USER",
        aktif: true,
    });
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    // 📥 Kullanıcı bilgilerini yükle
    useEffect(() => {
        const fetchUser = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get(`http://localhost:8080/api/kullanicilar/${id}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setUser(res.data);
            } catch (err) {
                console.error(err);
                setMessage("❌ Kullanıcı bilgileri alınamadı.");
            } finally {
                setLoading(false);
            }
        };
        fetchUser();
    }, [id]);

    // 📌 Input değişikliklerini yakala
    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    // 🔄 Aktiflik toggle
    const handleSwitch = (e) => {
        setUser({ ...user, aktif: e.target.checked });
    };

    // 💾 Güncelleme isteği
    const handleUpdate = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(`http://localhost:8080/api/kullanicilar/${id}`, user, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("✅ Kullanıcı başarıyla güncellendi!");
            setTimeout(() => navigate("/admin/users"), 1500);
        } catch (err) {
            console.error(err);
            setMessage("❌ Güncelleme başarısız oldu.");
        }
    };

    // 🗑️ Silme işlemi
    const handleDelete = async () => {
        if (!window.confirm("Bu kullanıcıyı silmek istediğine emin misin?")) return;
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/kullanicilar/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("🗑️ Kullanıcı silindi.");
            setTimeout(() => navigate("/admin/users"), 1500);
        } catch (err) {
            console.error(err);
            setMessage("❌ Silme işlemi başarısız oldu.");
        }
    };

    if (loading) {
        return (
            <Container sx={{ mt: 10, textAlign: "center" }}>
                <CircularProgress />
                <Typography mt={2}>Kullanıcı bilgileri yükleniyor...</Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                ✏️ Kullanıcı Güncelle
            </Typography>

            {message && (
                <Alert
                    severity={
                        message.startsWith("✅") || message.startsWith("🗑️")
                            ? "success"
                            : "error"
                    }
                    sx={{ mb: 2 }}
                >
                    {message}
                </Alert>
            )}

            <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                <TextField
                    label="Kullanıcı Adı"
                    name="kullaniciAdi"
                    value={user.kullaniciAdi}
                    onChange={handleChange}
                    fullWidth
                />

                <TextField
                    label="E-posta"
                    name="email"
                    type="email"
                    value={user.email}
                    onChange={handleChange}
                    fullWidth
                />

                {/* 🎭 Rol seçimi (tüm roller) */}
                <TextField
                    select
                    label="Rol"
                    name="rol"
                    value={user.rol}
                    onChange={handleChange}
                    fullWidth
                >
                    <MenuItem value="ADMIN">ADMIN</MenuItem>
                    <MenuItem value="BAS_DOKTOR">BAŞ DOKTOR</MenuItem>
                    <MenuItem value="DOKTOR">DOKTOR</MenuItem>
                    <MenuItem value="SEKRETER">SEKRETER</MenuItem>
                    <MenuItem value="USER">USER</MenuItem>
                </TextField>

                <FormControlLabel
                    control={<Switch checked={user.aktif} onChange={handleSwitch} />}
                    label={user.aktif ? "Aktif" : "Pasif"}
                />

                <Box sx={{ display: "flex", gap: 2, mt: 2 }}>
                    <Button variant="contained" color="primary" onClick={handleUpdate}>
                        💾 Güncelle
                    </Button>
                    <Button variant="outlined" color="error" onClick={handleDelete}>
                        🗑️ Sil
                    </Button>
                    <Button variant="text" onClick={() => navigate("/admin/users")}>
                        🔙 Listeye Dön
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default EditUser;
