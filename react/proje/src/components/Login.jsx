import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { TextField, Button, Container, Typography, Box, Alert, Link } from '@mui/material';

function Login() {
    const [user, setUser] = useState('');
    const [sifre, setSifre] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');

        try {
            // 🔐 Giriş isteği
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                kullaniciAdi: user,
                sifre: sifre
            },
                { withCredentials: true }
            );

            if (response.status === 200) {
                const token = response.data.token;
                localStorage.setItem('token', token);
                localStorage.setItem('userId', response.data.userId);
                localStorage.setItem('kullaniciAdi', response.data.kullaniciAdi);

                // 👤 Kullanıcı bilgilerini çek
                const meResponse = await axios.get('http://localhost:8080/api/auth/me', {
                    headers: { Authorization: `Bearer ${token}` }
                });

                const role = meResponse.data.rol;

                // 🔀 Role göre yönlendirme
                if (role === 'ADMIN') {
                    navigate('/admin');
                } else if (role === 'DOKTOR') {
                    navigate('/doktor');
                } else if (role === 'SEKRETER') {
                    navigate('/sekreter');
                } else {
                    navigate('/home');
                }
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Giriş başarısız! Lütfen bilgilerinizi kontrol edin.');
        }
    };

    return (
        <Container maxWidth="sm">
            <Box component="form" onSubmit={handleLogin} sx={{ mt: 5 }}>
                <Typography variant="h4" gutterBottom>Giriş Yap</Typography>

                <TextField
                    label="Kullanıcı Adı"
                    fullWidth
                    margin="normal"
                    value={user}
                    onChange={(e) => setUser(e.target.value)}
                />
                <TextField
                    label="Şifre"
                    type="password"
                    fullWidth
                    margin="normal"
                    value={sifre}
                    onChange={(e) => setSifre(e.target.value)}
                />

                {error && <Alert severity="error">{error}</Alert>}

                <Button type="submit" variant="contained" fullWidth sx={{ mt: 2 }}>
                    Giriş Yap
                </Button>

                <Box mt={2}>
                    <Link component={RouterLink} to="/signup">
                        Hesabın yok mu? Kayıt ol
                    </Link>
                </Box>
            </Box>
        </Container>
    );
}

export default Login;
