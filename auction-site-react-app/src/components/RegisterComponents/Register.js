import React, { Component } from "react";
import { Link } from "react-router-dom";
import Dropdown from 'react-bootstrap/Dropdown';
import Button from 'react-bootstrap/Button';
import './style.css';

class Register extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            email: "",
            password: "",
            admin: false
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.onClickAdmin = this.onClickAdmin.bind(this);
    }

    handleChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    handleSubmit(e) {
        e.preventDefault()

        const url = "http://localhost:23333/userCreate/";

        var data = {
            "username": this.state.username,
            "email": this.state.email,
            "password": this.state.password,
            "user_bio": this.state.userBio,
            "admin": this.state.admin
        }

        fetch(url, {
            method: "POST",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                Accept :'application/json',
                'Origin': 'http://localhost:3000'
            },
            body: JSON.stringify(data),
            referrerPolicy: 'no-referrer'
        })
        .then(res => res.json())
        .then(
            (result) => {
                window.location.href = "/register/success";
            },
            (error) => {
                window.location.href = "/register/fail";
            }
        )
    }

    onClickAdmin(bool) {
        this.setState({
            admin: bool
        })
    }

    render() {
        return (
            <div>
                <p className="register-title">Register as a member!</p>
                <div className="register-form-container">
                    <form className="register-form" onSubmit={e => this.handleSubmit(e)}>
                        <div className="register-input">
                            <span>Username: </span>
                            <input name="username" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div className="register-input">
                            <span>Email: </span>
                            <input name="email" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div className="register-input">
                            <span>Password: </span>
                            <input name="password" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div className="register-input">
                            <span>Bio: </span>
                            <input name="userBio" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div className="register-input">
                            <span>Admin: </span>
                            <Dropdown key={'Primary'}>
                                <Dropdown.Toggle variant="success" id="dropdown-basic">
                                    options
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    <Dropdown.Item onClick={() => this.onClickAdmin(true)}>Yes</Dropdown.Item>
                                    <Dropdown.Item onClick={() => this.onClickAdmin(false)}>No</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                        <div className="register-input">
                            <Button className="register-submit-button" type ="submit">Register</Button>
                        </div>
                        <Link to='/login'>
                            <Button className="login-button">Already have an account? Login here</Button>
                        </Link>
                    </form>
                </div>
            </div>
        );
    }
}

export default Register;