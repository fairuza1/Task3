import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { TextField, Button, Container, Typography, Box, Alert, Link } from '@mui/material';

function Login({ setIsLoggedIn }) {
    const [user, setUser] = useState('');
    const [sifre, setSifre] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/auth/login', {
                kullaniciAdi: user,
                sifre: sifre
            });

            // Başarılı giriş
            if (response.status === 200) {
                localStorage.setItem('userId', response.data.userId);
                localStorage.setItem('kullaniciAdi', response.data.kullaniciAdi);
                setIsLoggedIn(true);
                setError('');
                navigate('/home');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'An error occurred');
        }
    };

    return (
        <Container maxWidth="sm">
            <Box component="form" onSubmit={handleLogin} sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Login</Typography>
                <TextField
                    label="KullaniciAdi"
                    fullWidth
                    margin="normal"
                    value={user}
                    onChange={(e) => setUser(e.target.value)}
                />
                <TextField
                    label="sifre"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={sifre}
                    onChange={(e) => setSifre(e.target.value)}
                />
                {error && <Alert severity="error">{error}</Alert>}
                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Login
                </Button>
                <Box mt={2}>
                    <Link component={RouterLink} to="/signup">Don't have an account? Sign Up</Link>
                </Box>
            </Box>
        </Container>
    );
}

export default Login;
