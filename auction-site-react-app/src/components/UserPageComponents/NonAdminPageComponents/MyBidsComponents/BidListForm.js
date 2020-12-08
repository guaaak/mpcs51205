import React, { Component } from "react";
import BidRow from "./BidRow";
// import { Link } from "react-router-dom";

class BidListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            userId: props.userId,

            bidList: props.bidList
        }
    }

    render() {
        let form = this.state.bidList.map(entry => {
            // let bidId = entry.bidId;
            return (
                <BidRow userId={this.state.userId} key={entry.itemId} bid={entry}></BidRow>
            )
        })
        
        return (
            <div>
                <div className="bid-row">
                    <div className="bid-cell column-title">Item Name</div>
                    <div className="bid-cell column-title">Current Highest Bid</div>
                    <div className="bid-cell column-title">My Current Bid Price</div>
                    <div className="bid-cell column-title">Your New Bid Amount</div>
                    <div className="bid-cell column-title">Bid Status</div>
                    <div className="bid-cell column-title">Add to Cart</div>
                </div>
                {form}
            </div>
        );
    }
}

export default BidListForm;