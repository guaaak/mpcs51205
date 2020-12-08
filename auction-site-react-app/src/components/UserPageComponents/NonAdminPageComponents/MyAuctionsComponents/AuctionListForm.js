import React, { Component } from "react";
import AuctionRow from './AuctionRow';
import Button from 'react-bootstrap/Button';

class AuctionListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            userId: props.userId,
            auctionList: props.auctionList 
        }

        this.updateAuction = this.updateAuction.bind(this);
    }

    updateAuction(auctionId) {
        window.location.href = "/updateAuction/" + this.state.userId + "/" + auctionId;
    }

    render() {
        let form = this.state.auctionList.map(entry => {
            let auctionId = entry.itemId;

            if (entry.bidStatus !== "Closed") {
                return (
                    <AuctionRow key={entry.itemId} onClick={() => this.updateAuction(auctionId)} auction={entry}></AuctionRow>
                )
            } else {
                return (<div></div>);
            }
        })
        
        return (
            <div>
                <div className="auction-row">
                    <div className="auction-cell column-title">Item Name</div>
                    <div className="auction-cell column-title">Start Time</div>
                    <div className="auction-cell column-title">Expire Time</div>
                    <div className="auction-cell column-title">Status</div>
                    <div className="auction-cell column-title">Quantity</div>
                    <div className="auction-cell column-title">Shipping Cost</div>
                    <div className="auction-cell column-title">Buynow</div>
                    <div className="auction-cell column-title">Buynow Price</div>
                    <div className="auction-cell column-title">Item Description</div>
                    <div className="auction-cell column-title">Bid Count</div>
                </div>
                {form}
            </div>
        );
    }
}

export default AuctionListForm;