import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    // 1. NAVIGATION TOOL (To go to login page after success)
    const navigate = useNavigate();

    // 2. THE BACKPACK (State)
    // We store all 5 fields required by your Java DTO
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        mobileNumber: '',
        password: '',
        confirmPassword: ''
    });

    // We also need a backpack for Error messages or Success messages
    const [message, setMessage] = useState('');

    // 3. THE ROBOT ARM (Handle Typing)
    // When you type in ANY box, this function updates the right spot in the backpack
    const handleChange = (e) => {
        setFormData({
            ...formData,                 // Keep the other fields the same
            [e.target.name]: e.target.value // Update only the field that changed
        });
    };

    // 4. THE CARRIER PIGEON (Handle Submit)
    const handleSubmit = async (e) => {
        e.preventDefault(); // Stop page from refreshing

        // Basic check: Do passwords match?
        if (formData.password !== formData.confirmPassword) {
            setMessage("Passwords do not match!");
            return;
        }

        try {
            // SENDING DATA TO SPRING BOOT
            // We assume your backend is running on port 8080
            const response = await axios.post('http://localhost:8080/api/users/register', formData);
            
            console.log("Success:", response.data);
            setMessage("Registration Successful! Redirecting...");
            
            // Wait 2 seconds, then go to Login page
            setTimeout(() => {
                navigate('/login');
            }, 2000);

        } catch (error) {
            console.error("Error:", error);
            // If Spring Boot sends a validation error (like "Email invalid"), show it
            if (error.response && error.response.data) {
                // Ideally, backend returns a message string. 
                // If it returns a complex object, we might need to adjust this line.
                setMessage("Registration Failed: " + JSON.stringify(error.response.data)); 
            } else {
                setMessage("Server is down or not responding.");
            }
        }
    };

    return (
        <div style={{ padding: '50px' }}>
            <h2>Create Account</h2>
            
            {/* Show message if it exists (Red for error, usually) */}
            {message && <p style={{ color: 'red' }}>{message}</p>}

            <form onSubmit={handleSubmit}>
                {/* NAME */}
                <div>
                    <input
                        type="text"
                        name="name"  // MUST MATCH JAVA 'name'
                        placeholder="Full Name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* EMAIL */}
                <div>
                    <input
                        type="email"
                        name="email" // MUST MATCH JAVA 'email'
                        placeholder="Email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* MOBILE NUMBER */}
                <div>
                    <input
                        type="text"
                        name="mobileNumber" // MUST MATCH JAVA 'mobileNumber'
                        placeholder="Mobile (10 digits)"
                        value={formData.mobileNumber}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* PASSWORD */}
                <div>
                    <input
                        type="password"
                        name="password" // MUST MATCH JAVA 'password'
                        placeholder="Password (Min 6 chars)"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* CONFIRM PASSWORD */}
                <div>
                    <input
                        type="password"
                        name="confirmPassword" // MUST MATCH JAVA 'confirmPassword'
                        placeholder="Confirm Password"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" style={{ marginTop: '20px' }}>
                    Register Now
                </button>
            </form>
            
            <button onClick={() => navigate('/')} style={{ marginTop: '10px' }}>
                Back to Home
            </button>
        </div>
    );
};

export default RegisterPage;