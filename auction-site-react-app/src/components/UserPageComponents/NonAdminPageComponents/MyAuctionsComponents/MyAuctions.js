import React, { Component } from "react";
import { Link } from "react-router-dom";
import AuctionListForm from "./AuctionListForm";
import Button from 'react-bootstrap/Button';

class MyAuctions extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: props.match.params.userid,
            
            auctionListFetchSuccess: false,
            auctionList: []
        }

        this.addAuction = this.addAuction.bind(this);
    }

    componentDidMount() {
        const url = "http://localhost:9090/auction/bidding/prevAuctions/" + this.state.userId;

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
                    auctionListFetchSuccess: true,
                    auctionList: result.prevAuctionList
                })
            },
            (error) => {
                window.location.href = "/myauctions/fail/" + this.state.userId;
            }
        )
    }

    addAuction() {
        window.location.href = "/listauction/" + this.state.userId;
    }

    render() {
        let body;

        if (this.state.auctionListFetchSuccess !== false) {
            body = (
                <div>
                    <Link to={`/user/nonadmin/` + this.state.userId}>
                        <Button className="auction-button">back to user page</Button>
                    </Link>
                    <Button className="auction-button" onClick={this.addAuction}>List a new auction</Button>
                    <AuctionListForm userId={this.state.userId} className="auction-form" auctionList={this.state.auctionList}></AuctionListForm>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>Your auctions are being loaded...</p>
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

export default MyAuctions;