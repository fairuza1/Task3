import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { TextField, Button, Container, Typography, Box, Alert } from '@mui/material';

function Signup() {
    const [kullaniciAdi, setKullaniciAdi] = useState('');
    const [sifre, setSifre] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                'http://localhost:8080/api/auth/signup',
                {kullaniciAdi, sifre, email },
                { withCredentials: true } // backend cookie ile uyumlu
            );

            setMessage('Signup successful! Redirecting to login...');
            setTimeout(() => navigate('/'), 2000);
        } catch (err) {
            setError(err.response?.data?.message || 'Signup failed');
        }
    };

    return (
        <Container maxWidth="sm">
            <Box component="form" onSubmit={handleSignup} sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Signup</Typography>
                <TextField
                    label="KullanıcıAdı"
                    fullWidth
                    margin="normal"
                    value={kullaniciAdi}
                    onChange={(e) => setKullaniciAdi(e.target.value)}
                />
                <TextField
                    label="sifre"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={sifre}
                    onChange={(e) => setSifre(e.target.value)}
                />
                <TextField
                    label="Email"
                    type="email"
                    fullWidth
                    margin="normal"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                {error && <Alert severity="error">{error}</Alert>}
                {message && <Alert severity="success">{message}</Alert>}
                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>Signup</Button>
            </Box>
        </Container>
    );
}

export default Signup;
