import React from "react";
import { Link } from "react-router-dom";
import {
    Container,
    Typography,
    Button,
    Box,
    Card,
    CardContent,
    Grid,
} from "@mui/material";

const AdminDashboard = () => {
    const sections = [
        {
            title: "👤 Kullanıcı Yönetimi",
            description: "Sistemdeki tüm kullanıcıları görüntüle, düzenle veya sil.",
            links: [
                { to: "/admin/users", label: "Kullanıcıları Görüntüle", variant: "contained" },
                { to: "/admin/create-user", label: "Yeni Kullanıcı Oluştur", variant: "outlined" },
            ],
        },
        {
            title: "🩺 Doktor Yönetimi",
            description: "Doktor listesi üzerinde düzenleme veya yeni doktor ekleme işlemleri.",
            links: [
                { to: "/doktorlar", label: "Doktorları Görüntüle", variant: "contained" },
                { to: "/doktorlar/ekle", label: "Yeni Doktor Ekle", variant: "outlined" },
            ],
        },
        {
            title: "💼 Sekreter Yönetimi",
            description: "Sekreterlerin kayıtlarını görüntüle veya yeni kayıt ekle.",
            links: [
                { to: "/sekreter/hasta-listesi", label: "Hasta Listesi", variant: "contained" },
                { to: "/sekreter/hasta-ekle", label: "Yeni Hasta Ekle", variant: "outlined" },
            ],
        },
    ];

    return (
        <Container maxWidth="lg" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom align="center">
                🛠️ Yönetici Paneli
            </Typography>
            <Typography variant="subtitle1" align="center" color="text.secondary" sx={{ mb: 4 }}>
                Sistemi yönetmek için aşağıdaki bölümlerden birini seçin.
            </Typography>

            <Grid container spacing={3}>
                {sections.map((section, index) => (
                    <Grid item xs={12} md={4} key={index}>
                        <Card
                            sx={{
                                borderRadius: 3,
                                boxShadow: 3,
                                height: "100%",
                                display: "flex",
                                flexDirection: "column",
                            }}
                        >
                            <CardContent>
                                <Typography variant="h6" gutterBottom>
                                    {section.title}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                    {section.description}
                                </Typography>
                                <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                                    {section.links.map((link, i) => (
                                        <Button
                                            key={i}
                                            component={Link}
                                            to={link.to}
                                            variant={link.variant}
                                            fullWidth
                                        >
                                            {link.label}
                                        </Button>
                                    ))}
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default AdminDashboard;
