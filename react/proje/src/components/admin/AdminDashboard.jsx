import React from "react";
import { Link } from "react-router-dom";
import { Container, Typography, Button, Box } from "@mui/material";

const AdminDashboard = () => {
    return (
        <Container maxWidth="md" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                🛠️ Yönetici Paneli
            </Typography>

            <Box sx={{ display: "flex", gap: 2, mt: 4 }}>
                <Button variant="contained" component={Link} to="/admin/users">
                    👤 Kullanıcıları Görüntüle
                </Button>
                <Button variant="outlined" component={Link} to="/admin/create-user">
                    ➕ Yeni Kullanıcı Oluştur
                </Button>
            </Box>
        </Container>
    );
};

export default AdminDashboard;
