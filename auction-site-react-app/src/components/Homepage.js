import React, { Component } from "react";
import { Link } from "react-router-dom";
import Button from 'react-bootstrap/Button';

class Homepage extends Component {
    render() {
        return (
            <div>
                <Link to="/register">
                    <Button className="normal-button">New User</Button>
                </Link>
                <Link to="/login">
                    <Button className="normal-button">Login</Button>
                </Link>
            </div>
        );
    }
}

export default Homepage;