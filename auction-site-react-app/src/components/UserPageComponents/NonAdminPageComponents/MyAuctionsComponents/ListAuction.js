import React, { Component } from "react";
import { Link } from "react-router-dom";
import DateTimePicker from 'react-datetime-picker';
import Dropdown from 'react-bootstrap/Dropdown';
import Button from 'react-bootstrap/Button';

class ListAuction extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: props.match.params.userid,
            
            name: "",
            startPrice: 0,
            startTime: new Date(),
            quantity: 0,
            timeExpire: new Date(),
            shippingCosts: 0,
            buyNow: false,
            itemDescription: "",
            sellerRating: 0,
            category: "",
            buyNowPrice: 3453,

            itemId: ""
        }

        this.onSubmit = this.onSubmit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.onClickBuynow = this.onClickBuynow.bind(this);
    }

    onChange(e) {
        e.preventDefault()

        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    onStartTimeChange(date) {
        this.setState({
            startTime: date
        })
    }

    onEndTimeChange(date) {
        this.setState({
            timeExpire: date
        })
    }

    async onSubmit(e) {
        e.preventDefault()

        const createItemUrl = "http://localhost:8080/auction/item/create";
        const createAuctionUrl = "http://localhost:9090/auction/bidding/newBid";
        
        const dataItem = {
            "quantity": this.state.quantity,
            "ratings": this.state.sellerRating,
            "description": this.state.itemDescription,
            "name": this.state.name,
            "categoryId": this.state.category,
            "isFlagged": false,
            "shippingCosts": this.state.shippingCosts
        }   

        let itemID = "";

        await fetch(createItemUrl, {
            method: "POST",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                Accept :'application/json',
                'Origin': 'http://localhost:3000'
            },
            body: JSON.stringify(dataItem),
            referrerPolicy: 'no-referrer'
        })
        .then(res => res.json())
        .then(
            (result) => {
                if (result.success) {
                    console.log("add item", result)
                    itemID = result.item.id
                } else {
                    alert("An error occurred when attempted to list your auction...");
                }
            },
            (error) => {
                alert("An error occurred when attempted to list your auction...");
            }
        )

        const dataAuction = {
            "itemId": itemID,
            "sellerId": this.state.userId,
            "startTime": this.state.startTime.toISOString().split('.')[0] + 'Z',
            "endTime": this.state.timeExpire.toISOString().split('.')[0] + 'Z',
            "initPrice": this.state.startPrice,
            "canBuyNow": this.state.buyNow,
            "buyNowPrice": this.state.buyNowPrice
        }

        console.log("dataa", JSON.stringify(dataAuction))

        await fetch(createAuctionUrl, {
            method: "POST",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                Accept :'application/json',
                'Origin': 'http://localhost:3000'
            },
            body: JSON.stringify(dataAuction),
            referrerPolicy: 'no-referrer'
        })
        .then(res => res.json())
        .then(
            (result) => {
                if (result.success) {
                    console.log(result)
                    alert("Your auction has been successfully listed.")
                } else {
                    console.log(111)
                    alert("An error occurred when attempted to list your auction...");
                }
            },
            (error) => {
                console.log(error)
                alert("An error occurred when attempted to list your auction...");
            }
        )
    }

    onClickBuynow(bool) {
        this.setState({
            buyNow: bool
        })
    }

    render() {
        return (
            <div>
                <Link to={"/myauctions/" + this.state.userId}>
                    <Button>back to myAuction page</Button>
                </Link>
                <p className="list-text">List a new auction:</p>
                <form className="update-form" onSubmit={(e) => this.onSubmit(e)}>
                    <div className="update-row">
                        <span>Item Name: </span>
                        <input type="text" name="name" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Quantity: </span>
                        <input type="number" name="quantity" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Start Time: </span>
                        <DateTimePicker closeWidgets={true} value={this.state.startTime} onChange={date => this.onStartTimeChange(date)}></DateTimePicker>
                    </div>
                    <div className="update-row">
                        <span>Expire Time: </span>
                        <DateTimePicker value={this.state.timeExpire} onChange={date => this.onEndTimeChange(date)}></DateTimePicker>
                    </div>
                    <div className="update-row">
                        <span>Start Price: </span>
                        <input type="number" name="startPrice" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Shipping Cost: </span>
                        <input type="number" name="shippingCosts" onChange={e => this.onChange(e)}></input>
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
                        <input type="number" name="buyNowPrice" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Item Description: </span>
                        <input type="text" name="itemDescription" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Seller Rating: </span>
                        <input type="number" step="0.1" name="sellerRating" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="update-row">
                        <span>Category:</span>
                        <input type="text" name="category" onChange={e => this.onChange(e)}></input>
                    </div>
                    <div className="list-button">
                        <Button variant="info" type ="submit">List</Button>
                    </div>
                </form>
            </div>
        );
    }
}

export default ListAuction;