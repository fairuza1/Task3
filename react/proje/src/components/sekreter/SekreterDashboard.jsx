// src/components/sekreter/SekreterDashboard.jsx
import React from "react";
import { Container, Typography, Button, Box } from "@mui/material";
import { Link } from "react-router-dom";

const SekreterDashboard = () => {
    return (
        <Container maxWidth="md" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                📋 Sekreter Paneli
            </Typography>

            <Box sx={{ display: "flex", gap: 2, mt: 4 }}>
                <Button variant="contained" component={Link} to="/sekreter/hasta-listesi">
                    🩺 Hastaları Görüntüle
                </Button>
                <Button variant="outlined" component={Link} to="/sekreter/hasta-ekle">
                    ➕ Yeni Hasta Ekle
                </Button>
            </Box>
        </Container>
    );
};

export default SekreterDashboard;
