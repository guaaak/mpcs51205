import React, { Component } from "react";
import { Link } from "react-router-dom";
import Button from 'react-bootstrap/Button';

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: ""
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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

        const url = "http://localhost:23333/login/";

        var data = {
            "email": this.state.email,
            "password": this.state.password
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
                if (result.status === "fail") {
                    window.location.href = "/login/fail";
                } else {
                    let userId = result.user_id;

                    if (result.admin === false) {
                        window.location.href = "/user/nonadmin/" + userId
                    } else {
                        window.location.href = "/user/admin/" + userId
                    }
                }
            },
            (error) => {
                window.location.href = "/login/fail";
            }
        )
    }

    render() {
        return (
            <div>
                <p>Please login:</p>
                <div className="login-form-container">
                    <form className="login-form" onSubmit={e => this.handleSubmit(e)}>
                        <div className="email-input">
                            <span>Email: </span>
                            <input name="email" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div className="password-input">
                            <span>Password: </span>
                            <input name="password" type="text" onChange={e => this.handleChange(e)}></input>
                        </div>
                        <div>
                            <Button className="normal-button2" type ="submit">login</Button>
                        </div>
                        <Link to="/register">
                            <Button className="normal-button2">Don't have an account? Go register here</Button>
                        </Link>
                    </form>
                </div>
            </div>
        );
    }
}

export default Login;