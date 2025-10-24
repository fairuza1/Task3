import React from "react";
import { useNavigate } from "react-router-dom";
import { Container, Typography, Button, Box, Paper } from "@mui/material";

const BasDoktorDashboard = () => {
    const navigate = useNavigate();
    const kullaniciAdi = localStorage.getItem("kullaniciAdi") || "Baş Doktor";

    return (
        <Container maxWidth="md" sx={{ mt: 8 }}>
            <Paper elevation={3} sx={{ p: 4, borderRadius: 3, textAlign: "center" }}>
                <Typography variant="h4" gutterBottom>
                    🧑‍⚕️ Hoş geldiniz, {kullaniciAdi}
                </Typography>

                <Typography variant="body1" sx={{ mb: 4 }}>
                    Baş Doktor paneline hoş geldiniz. Buradan doktorları yönetebilir,
                    muayeneleri inceleyebilir ve sistem genel durumunu görebilirsiniz.
                </Typography>

                <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => navigate("/doktorlar")}
                    >
                        👨‍⚕️ Doktorları Görüntüle
                    </Button>

                    <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => navigate("/doktorlar/ekle")}
                    >
                        ➕ Yeni Doktor Ekle
                    </Button>

                    <Button
                        variant="contained"
                        color="success"
                        onClick={() => navigate("/doktor/muayeneler")}
                    >
                        🩺 Tüm Muayeneleri Gör
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

export default BasDoktorDashboard;
