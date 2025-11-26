import React from "react";
import { useNavigate } from "react-router-dom";


const LandingPage = () =>{

    const navigate  = useNavigate();

    return(
        <di>
            <h1>Welcome to spend share</h1>
            <p>Split expense with friends easily</p>
            <button onClick={() => navigate('/register')}>
                Regsiter
            </button>

            <button onClick={() => navigate('/login')} style={{marginLeft:'10px'}}>
                Login
            </button>

            
        </di>
    );
};

export default LandingPage;