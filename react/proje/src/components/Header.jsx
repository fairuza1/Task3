import React from "react";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const Header = ({ setIsLoggedIn }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("kullaniciAdi");
        localStorage.removeItem("rol");
        setIsLoggedIn(false);
        navigate("/");
    };

    return (
        <AppBar position="static" sx={{ backgroundColor: "#1976d2" }}>
            <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
                <Typography
                    variant="h6"
                    sx={{ cursor: "pointer" }}
                    onClick={() => navigate("/home")}
                >
                    🏥 Hastane Yönetim Sistemi
                </Typography>
                <Button color="inherit" onClick={handleLogout}>
                    🚪 Çıkış Yap
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Header;
