import React, { Component } from "react";
import { Link } from "react-router-dom";
import Button from 'react-bootstrap/Button';

class RegisterSuccess extends Component {
    render() {
        return (
            <div>
                <p className="register-title">You've been registered successfully!</p>
                <Link to="/login">
                    <Button>Login</Button>
                </Link>
            </div>
        );
    }
}

export default RegisterSuccess;