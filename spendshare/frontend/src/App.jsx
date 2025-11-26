import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import Dashboard from "./pages/Dashboard";
import CreateGroupPage from "./pages/CreateGroupPage";
import GroupDetails from "./pages/GroupDetails";

function App(){
  return(
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />}/>
        <Route path="/register" element={<RegisterPage />}/>
        <Route path="/login" element={<LoginPage />}/>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/create-group" element={<CreateGroupPage />} />
        <Route path="/group/:groupId" element={<GroupDetails />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;