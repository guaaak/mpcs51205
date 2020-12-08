import React, { Component } from "react";
import Button from 'react-bootstrap/Button';

class OngoingAuctionListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            auctionList: props.auctionList
        }

        this.onClickSort = this.onClickSort.bind(this);
        this.dateFormat = this.dateFormat.bind(this);
    }

    stopAuction(auctionId) {
        const url = "http://localhost:9090/auction/bidding/closeBid/" + auctionId;

        fetch(url, {
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
                alert("The auction has been successfully stopped.");
                window.location.reload()
            },
            (error) => {    
                alert("An error occured when attempted to stop the auction...");
            }
        )
    }

    onClickSort() {
        this.setState({
            auctionList: this.state.auctionList.sort(function(a, b) {
                var d1 = new Date(a.endTime);
                var d2 = new Date(b.endTime);

                return d1 - d2;
            })
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
                    <div className="auction-cell">{dateS}</div>
                    <div className="auction-cell">{dateE}</div>
                    <div className="auction-cell">{entry.canBuyNow ? "Available" : "Not Available"}</div>
                    <div className="auction-cell">{entry.bidStatus}</div>
                    <div className="auction-cell">
                        <Button variant="info" onClick={() => this.stopAuction(entry.itemId)}>Stop</Button>
                    </div>
                </div>
            )
        })

        return (
            <div>
                <div className="auction-row">
                    <div className="auction-cell column-title">Auction Id</div>
                    <div className="auction-cell column-title">Start Time</div>
                    <div className="auction-cell column-title">Expire Time
                        <Button variant="info" onClick={this.onClickSort}>Sort by</Button>
                    </div>
                    <div className="auction-cell column-title">Buynow</div>
                    <div className="auction-cell column-title">Auction Status</div>
                    <div className="auction-cell column-title">
                        Stop Auction
                    </div>
                </div>
                {body}
            </div>
        );
    }
}

export default OngoingAuctionListForm;