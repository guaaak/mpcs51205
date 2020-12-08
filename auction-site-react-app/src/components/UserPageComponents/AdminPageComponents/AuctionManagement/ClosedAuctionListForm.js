import React, { Component } from "react";
import Popup from './Popup';
import Button from 'react-bootstrap/Button';

class ClosedAuctionListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            auctionList: props.auctionList,

            prevBid: [],
            showPopup: false
        }

        this.checkBidHistory = this.checkBidHistory.bind(this);
        this.onClose = this.onClose.bind(this);
    }

    checkBidHistory(prevBid) {
        // popover window
        this.setState({
            showPopup: true,
            prevBid: prevBid
        })
    }

    onClose() {
        this.setState({
            showPopup: false,
        })
    }

    dateFormat(date) {
        var month = parseInt(date.getMonth()) + 1;
        return date.getFullYear() + "/" + month.toString() + "/" + date.getUTCDate() + " " + date.getHours() + ":" + date.getMinutes()
    }

    render() {
        let body;
        body = this.state.auctionList.map(entry => {

            var startTime = new Date(entry.startTime)
            var endTime = new Date(entry.endTime)
            var dateS = this.dateFormat(startTime)
            var dateE = this.dateFormat(endTime)

            return (
                <div key={entry.itemId} className="auction-row">
                    <div className="auction-cell">{entry.itemId}</div>
                    <div className="auction-cell">{entry.sellerId}</div>
                    <div className="auction-cell">{dateS}</div>
                    <div className="auction-cell">{dateE}</div>
                    <div className="auction-cell">{entry.bidCount}</div>
                    <div className="auction-cell">{entry.winnerId}</div>
                    <div className="auction-cell">{entry.finalOffer}</div>
                    <div className="auction-cell">
                        <Button onClick={() => this.checkBidHistory(entry.prevBid)}>Check</Button>
                    </div>
                </div>
            )
        })

        return (
            <div>
                <div className="closed-auctions-body">
                    <div className="auction-row">
                        <div className="auction-cell column-title">Auction ID</div>
                        <div className="auction-cell column-title">Seller UserID</div>
                        <div className="auction-cell column-title">Start Time</div>
                        <div className="auction-cell column-title">Expire Time</div>
                        <div className="auction-cell column-title">Bid Count</div>
                        <div className="auction-cell column-title">Winner UserID</div>
                        <div className="auction-cell column-title">Final Offer Price</div>
                        <div className="auction-cell column-title">Check Bid History</div>
                    </div>
                    {body}
                </div>
                { this.state.showPopup ? <Popup onClose={this.onClose} bidList={this.state.prevBid} show={this.state.showPopup}></Popup> : null}
            </div>
        );
    }
}

export default ClosedAuctionListForm;