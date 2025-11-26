import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const navigate = useNavigate();
    
    // --- STATE ---
    const [formData, setFormData] = useState({
        identifier: '', // Handles both Email or Mobile
        password: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // --- HANDLERS ---
    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setIsLoading(true);

        try {
            // 1. Send data to Backend
            const response = await axios.post('http://localhost:8080/api/users/login', formData);
            
            // 2. Save user details to LocalStorage
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            
            // 3. Redirect to Dashboard
            navigate('/dashboard');

        } catch (error) {
            console.error("Login Error", error);
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message || "Invalid Credentials");
            } else {
                setErrorMessage("Server error. Is the backend running?");
            }
        } finally {
            setIsLoading(false);
        }
    };

    // --- STYLES ---
    const styles = {
        container: {
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh',
            backgroundColor: '#f6f6f6', // Light gray background matching sidebar
            fontFamily: "'Segoe UI', sans-serif"
        },
        card: {
            width: '100%',
            maxWidth: '400px',
            backgroundColor: 'white',
            borderRadius: '8px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.1)', // Soft shadow
            padding: '40px',
            boxSizing: 'border-box'
        },
        logo: {
            fontSize: '32px',
            fontWeight: 'bold',
            color: '#5bc5a7', // Brand Teal Color
            textAlign: 'center',
            marginBottom: '10px'
        },
        subtitle: {
            textAlign: 'center',
            color: '#888',
            marginBottom: '30px',
            fontSize: '16px'
        },
        label: {
            display: 'block',
            marginBottom: '8px',
            fontSize: '14px',
            fontWeight: '600',
            color: '#333'
        },
        input: {
            width: '100%',
            padding: '12px',
            marginBottom: '20px',
            border: '1px solid #ddd',
            borderRadius: '4px',
            fontSize: '16px',
            boxSizing: 'border-box' // Ensures padding doesn't break width
        },
        button: {
            width: '100%',
            padding: '12px',
            backgroundColor: '#5bc5a7',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            fontSize: '16px',
            fontWeight: 'bold',
            cursor: 'pointer',
            transition: 'background 0.2s'
        },
        error: {
            backgroundColor: '#ffebee',
            color: '#c62828',
            padding: '10px',
            borderRadius: '4px',
            marginBottom: '20px',
            textAlign: 'center',
            fontSize: '14px'
        },
        linkText: {
            textAlign: 'center',
            marginTop: '20px',
            color: '#666',
            fontSize: '14px'
        },
        linkAction: {
            color: '#5bc5a7',
            cursor: 'pointer',
            fontWeight: 'bold',
            marginLeft: '5px'
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <div style={styles.logo}>SpendShare</div>
                <div style={styles.subtitle}>Log in to split expenses</div>
                
                {errorMessage && <div style={styles.error}>{errorMessage}</div>}

                <form onSubmit={handleSubmit}>
                    <div>
                        <label style={styles.label}>Email address or Phone</label>
                        <input
                            type="text"
                            name="identifier"
                            value={formData.identifier}
                            onChange={handleChange}
                            required
                            style={styles.input}
                        />
                    </div>

                    <div>
                        <label style={styles.label}>Password</label>
                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            style={styles.input}
                        />
                    </div>

                    <button 
                        type="submit" 
                        style={styles.button}
                        disabled={isLoading}
                    >
                        {isLoading ? "Logging in..." : "Log in"}
                    </button>
                </form>

                <div style={styles.linkText}>
                    New to SpendShare? 
                    <span 
                        style={styles.linkAction} 
                        onClick={() => navigate('/register')}
                    >
                        Sign up
                    </span>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;