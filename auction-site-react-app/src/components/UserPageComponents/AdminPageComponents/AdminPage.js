import React, { Component } from "react";
import { Link } from "react-router-dom";
import CategoriesListForm from "./CategoryManagement/CategoriesListForm";
import UserListForm from "./UserListForm";
import FlaggedItemListForm from "./FlaggedItemListForm"
import CustomerSupportEmailListForm from "./CustomerSupportManagement/CustomerSupportEmailListForm";
import AuctionManagement from "./AuctionManagement/AuctionManagement";
import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';

class AdminPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            userId: props.userId,
            operation: 1
        }
    }

    componentDidMount() {
        const userIdentityUrl = "http://localhost:23333/fetchUserIdentity/?uid=" + this.state.userId;
        fetch(userIdentityUrl, {
            method: "GET",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                Accept :'application/json',
                'Origin': 'http://localhost:3000'
            },
            referrerPolicy: 'no-referrer'
        })
        .then(res => res.json())
        .then(
            (result) => {
                this.setState({
                    username: result.username,
                });
            },
            (error) => {
                window.location.href = "/user/fail";
            }
        )
    }

    chooseOperation(operation) {
        this.setState({ operation: operation})
    }

    render() {
        let body;
        let subbody;

        switch(this.state.operation) {
            case 1:
                subbody = <UserListForm userId={this.state.userId}></UserListForm>;
                break;
            case 2:
                subbody = <AuctionManagement userId={this.state.userId}></AuctionManagement>;
                break;
            case 3:
                subbody = <FlaggedItemListForm userId={this.state.userId}></FlaggedItemListForm>;
                break;
            case 4:
                subbody = <CategoriesListForm userId={this.state.userId}></CategoriesListForm>;
                break;
            case 5:
                subbody = <CustomerSupportEmailListForm userId={this.state.userId}></CustomerSupportEmailListForm>;
                break;
            default:

        }

        if (this.state.username !== "") {

            body = (
                <div>
                    <div className="subtitle">
                        <span>{this.state.username}, WELCOME BACK!  </span>
                        <Link to="/">
                            <Button>Logout</Button>
                        </Link>
                    </div>
                    <div className="button-group">
                        <ButtonGroup aria-label="Basic example">
                            <Button onClick={() => this.chooseOperation(1)}>Users Management</Button>
                            <Button onClick={() => this.chooseOperation(2)}>Auctions Management</Button>
                            <Button onClick={() => this.chooseOperation(3)}>Flagged Items</Button>
                            <Button onClick={() => this.chooseOperation(4)}>Categories Management</Button>
                            <Button onClick={() => this.chooseOperation(5)}>Check Customer Support Emails</Button>
                        </ButtonGroup>
                    </div>
                    {subbody}
                </div>
            )
        } else {
            body = (
                <div>
                    <p>Your contents are being loaded...</p>
                </div>
            )
        }

        return (
            <div>
                {body}
            </div>
        );
    }
}

export default AdminPage;