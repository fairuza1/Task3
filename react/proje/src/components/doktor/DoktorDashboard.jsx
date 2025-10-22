import React from "react";
import { useNavigate } from "react-router-dom";
import { Container, Typography, Button, Box, Paper } from "@mui/material";

const DoktorDashboard = () => {
    const navigate = useNavigate();
    const doktorAdi = localStorage.getItem("kullaniciAdi") || "Doktor";

    return (
        <Container maxWidth="md" sx={{ mt: 8 }}>
            <Paper elevation={3} sx={{ p: 4, borderRadius: 3, textAlign: "center" }}>
                <Typography variant="h4" gutterBottom>
                    👨‍⚕️ Hoş geldiniz, {doktorAdi}
                </Typography>
                <Typography variant="body1" sx={{ mb: 4 }}>
                    Doktor paneline hoş geldiniz. Buradan muayenelerinizi ve reçetelerinizi yönetebilirsiniz.
                </Typography>

                <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => navigate("/doktor/muayeneler")}
                    >
                        🩺 Muayenelerim
                    </Button>

                    <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => navigate("/doktor/muayene-ekle")}
                    >
                        ➕ Yeni Muayene Ekle
                    </Button>

                    <Button
                        variant="contained"
                        color="success"
                        onClick={() => navigate("/doktor/recete-ekle/1")}
                    >
                        💊 Reçete Yaz (örnek)
                    </Button>

                    <Button
                        variant="outlined"
                        color="error"
                        onClick={() => {
                            localStorage.clear();
                            navigate("/");
                        }}
                    >
                        🚪 Çıkış Yap
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default DoktorDashboard;
