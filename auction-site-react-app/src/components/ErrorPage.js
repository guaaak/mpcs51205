import React, { Component } from "react";
import { Link } from "react-router-dom";
import Button from 'react-bootstrap/Button';

class ErrorPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            routeSubfixUserid: props.match.params.userid
        }
    }

    render() {
        let subfix = this.state.routeSubfixUserid ? this.state.routeSubfixUserid : "";
        return (
            <div>
                <p>{this.props.errorMessage}</p>
                <Link to={this.props.route + subfix}>
                    <Button>{this.props.buttonName}</Button>
                </Link>
            </div>
        );
    }
}

export default ErrorPage;