import React from 'react';
import { Typography, Container } from '@mui/material';

function Home() {
    return (
        <Container sx={{ mt: 5 }}>
            <Typography variant="h3">Welcome Home!</Typography>
            <Typography>You're logged in successfully.</Typography>
        </Container>
    );
}

export default Home;
