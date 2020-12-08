import React, { Component } from "react";
import { Link } from "react-router-dom";
import SearchResultList from "../../SearchComponents/SearchResultList";
import Popup from "./Popup";
import CartPopup from "./CartPopup";
import Button from 'react-bootstrap/Button';
import ButtonGroup from 'react-bootstrap/ButtonGroup';

class NonAdminPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            userId: props.userId,
            isSuspended: false,
            operation: 1,

            itemKeyword: "",
            categoryKeyword: "",
            showSearchResult: false,
            searchResult: [],

            showPopup: false,
            showCart: false
        }

        this.onClickBack = this.onClickBack.bind(this);
        this.onClose = this.onClose.bind(this);
        this.onCloseCart = this.onCloseCart.bind(this);
        this.onClickCart = this.onClickCart.bind(this);
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
                    isSuspended: result.suspended
                });
            },
            (error) => {
                window.location.href = "/user/fail";
            }
        )
    }

    chooseOperation(operation) {
        switch(operation) {
            case 1:
                window.location.href = "/manageaccount/" + this.state.userId;
                break;
            case 2:
                window.location.href = "/myauctions/" + this.state.userId;
                break;
            case 3:
                window.location.href = "/mybids/" + this.state.userId;
                break;
            case 4:
                window.location.href = "/mywatchlist/" + this.state.userId;
                break;
            case 5:
                this.setState({
                    showPopup: true
                })
                break;
            default:
        }
    }

    handleChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    handleKeywordSearchSubmit(e) {
        e.preventDefault()

        let keyword = this.state.itemKeyword;

        const url = "http://localhost:8080/auction/item/keyword/" + keyword;

        fetch(url, {
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
                console.log("search result", result)
                this.setState({
                    showSearchResult: true,
                    searchResult: result.searchResults
                })
            },  
            (error) => {
                alert("Some errors occured during the searching, please retry...");
            }
        )
    }

    handleCategorySearchSubmit(e) {
        e.preventDefault()

        let keyword = this.state.categoryKeyword;

        const url = "http://localhost:8080/auction/item/category/" + keyword;

        fetch(url, {
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
                    showSearchResult: true,
                    searchResult: result.searchResults
                })
            },  
            (error) => {
                alert("Some errors occured during the searching, please retry...");
            }
        )
    }

    onClickBack() {
        this.setState({
            showSearchResult: false
        })
    }

    onClose() {
        this.setState({
            showPopup: false
        })
    }

    onClickCart() {
        this.setState({
            showCart: true
        })
    }

    onCloseCart() {
        this.setState({
            showCart: false
        })
    }

    render() {
        let body;
        if (this.state.username !== "") {
            let searchSection;

            if (this.state.showSearchResult) {
                searchSection = (
                    <div>
                        <SearchResultList userId={this.state.userId} onClickBack={this.onClickBack} searchResult={this.state.searchResult}></SearchResultList>
                    </div>
                )
                
            } else {
                searchSection = (
                    <div>
                        <div className="subtitle subtitle-size">Search for items to bid on!</div>
                        <form className="search-bar" onSubmit={e => this.handleKeywordSearchSubmit(e)}>
                            <div>
                                <span>Search by Keyword: </span>
                                <input name="itemKeyword" type="text" onChange={e => this.handleChange(e)}></input>
                            </div>
                            <Button variant="info" className="go-button" type="submit">GO!</Button>
                        </form>
                        <form className="search-bar" onSubmit={e => this.handleCategorySearchSubmit(e)}>
                            <div>
                                <span>Search by Category: </span>
                                <input name="categoryKeyword" type="text" onChange={e => this.handleChange(e)}></input>
                            </div>
                            <Button variant="info" className="go-button" type="submit">GO!</Button>
                        </form>
                    </div>
                )
            }

            body = (
                <div>
                    <div className="subtitle">
                        <span>{this.state.username}, WELCOME BACK!   </span>
                        <Link to="/">
                            <Button>Logout</Button>
                        </Link>
                        <Button className="cart-button" onClick={this.onClickCart}>Cart</Button>
                    </div>
                    <div className="button-group">
                        <ButtonGroup aria-label="Basic example">
                            <Button onClick={() => this.chooseOperation(1)}>Manage Account</Button>
                            <Button onClick={() => this.chooseOperation(2)}>My Auctions</Button>
                            <Button onClick={() => this.chooseOperation(3)}>My Bids</Button>
                            <Button onClick={() => this.chooseOperation(4)}>My Watchlist</Button>
                            <Button onClick={() => this.chooseOperation(5)}>My Auction Window</Button>
                        </ButtonGroup>
                    </div>
                    {searchSection}
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
                <div className="non-admin-page-body">
                    {body}
                </div>
                {this.state.showPopup ? <Popup userId={this.props.userId} onClose={this.onClose} show={this.state.showPopup}></Popup> : null}
                {this.state.showCart ? <CartPopup userId={this.props.userId} onClose={this.onCloseCart} show={this.state.showCart}></CartPopup> : null}
            </div>
        );
    }
}

export default NonAdminPage;