import React, { Component } from "react";
import { Link } from "react-router-dom";
import Dropdown from 'react-bootstrap/Dropdown';
import Button from 'react-bootstrap/Button';

class UpdateAuction extends Component {
    constructor(props) {
        super(props);

        this.state = {
            userId: props.match.params.userid,
            auctionId: props.match.params.auctionid,

            itemName: "",
            quantity: 0,
            shippingCosts: 0,
            buyNow: false,
            buyNowPrice: 0,
            itemDescription: "",

            itemFetchStatus: "fetching"
        }

        this.onUpdate = this.onUpdate.bind(this);
        this.cancelAuction = this.cancelAuction.bind(this);
    }

    async componentDidMount() {
        const fetchAuctionUrl = "http://localhost:8080/auction/item/" + this.state.auctionId;

        await fetch(fetchAuctionUrl, {
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
                    itemFetchStatus: "fetched",

                    itemName: result.name,
                    quantity: result.quantity,
                    shippingCosts: result.shippingCosts,
                    itemDescription: result.description
                })
            },
            (error) => {
                window.location.href = "/updateauctionfail/" + this.state.userId;
            }
        )
    }

    onChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    onUpdate(e) {
        e.preventDefault();

        const updateUrl = "http://localhost:8080/auction/item/update/" + this.state.auctionId;

        var data = {
            "quantity": this.state.quantity,
            "description": this.state.itemDescription,
            "shippingCost": this.state.shippingCosts,
            "canBuyNow": this.state.buyNow,
            "buyNowPrice": this.state.buyNowPrice
        }

        fetch(updateUrl, {
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
            (res) => {
                alert("The auction was updated successfully.");
                window.location.href = "/myauctions/" + this.state.userId;
            },
            (error) => {
                console.log(error)
                alert("An error occurred when attempted to update the auction...");
            }
        )
    }

    cancelAuction(e) {
        e.preventDefault();

        console.log(this.state.auctionId)

        const cancelUrl = "http://localhost:8080/auction/item/delete/" + this.state.auctionId;

        fetch(cancelUrl, {
            method: "POST",
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
                if (result.success) {
                    alert("This auction has been cancelled.");
                } else {
                    alert("There are bids currently on the auction, it cannot be cancelled.");
                }
                window.location.href = "/myauctions/" + this.state.userId;
            },
            (error) => {
                alert("An error occured when attempted to cancel the auction...");
            }
        )
    }

    onClickBuynow(bool) {
        this.setState({
            buyNow: bool
        })
    }

    render() {
        let body;
        if (this.state.itemFetchStatus === "fetched") {
            body = (
                <div>
                    <Link to={"/myauctions/" + this.state.userId}>
                        <Button>back to my auctions</Button>
                    </Link>
                    <form className="update-form" onSubmit={e => this.onUpdate(e)}>
                        <div className="update-row">
                            <span>Item Name: {this.state.itemName}</span>
                        </div>
                        <div className="update-row">
                            <span>Quantity: </span>
                            <input type="text" name="quantity" value={this.state.quantity} onChange={e => this.onChange(e)}></input>
                        </div>
                        <div className="update-row">
                            <span>Shipping Cost: </span>
                            <input type="number" name="shippingCosts" value={this.state.shippingCosts} onChange={e => this.onChange(e)}></input>
                        </div>
                        <div className="buynow-button update-row">
                            <span>Buynow Option: </span>
                            <Dropdown key={'Primary'}>
                                <Dropdown.Toggle variant="success" id="dropdown-basic">
                                    { this.state.buyNow ? "Available" : "Not Available" }
                                </Dropdown.Toggle>
                                <Dropdown.Menu>
                                    <Dropdown.Item onClick={() => this.onClickBuynow(true)}>Available</Dropdown.Item>
                                    <Dropdown.Item onClick={() => this.onClickBuynow(false)}>Not Available</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </div>
                        <div className="update-row">
                            <span>Buynow Price: </span>
                            <input type="number" name="buyNowPrice" value={this.state.buyNowPrice} onChange={e => this.onChange(e)}></input>
                        </div>
                        <div className="update-row">
                            <span>Item Description: </span>
                            <input type="text" name="itemDescription" value={this.state.itemDescription} onChange={e => this.onChange(e)}></input>
                        </div>
                        <div className="update-row">
                            <Button variant="info" type ="submit">Update</Button>
                        </div>
                    </form>
                    <Button variant="info" onClick={e => this.cancelAuction(e)}>Cancel the auction</Button>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>Your auction is being loaded...</p>
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

export default UpdateAuction;