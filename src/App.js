import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import ChatOpenIcon from "./pages/ChatOpenIcon";
import MainNavBar from "./components/common/MainNavBar";
import Product from "./pages/Product";
import Signup from "./pages/Signup";

function App() {
  return (
    <BrowserRouter>
      <div>
        <MainNavBar />
        <ChatOpenIcon />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/product/*" element={<Product />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
