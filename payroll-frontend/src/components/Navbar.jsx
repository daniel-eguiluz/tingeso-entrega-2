// src/components/Navbar.jsx

import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          {/* Título del Navbar */}
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, textDecoration: 'none', color: 'inherit' }}
          >
            PRESTA BANCO 
          </Typography>
          
          {/* Botón de Home */}
          <Button color="inherit" component={Link} to="/">
            Home
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
