import React, { Component } from "react";
import Popup from "./Popup";
import Button from 'react-bootstrap/Button';

class SearchResultList extends Component {
    constructor(props) {
        super(props)
        
        this.state = {
            selectedItem: null,
            showPopup: false
        }

        this.onClose = this.onClose.bind(this)
        this.onCartClick = this.onCartClick.bind(this)
    } 

    onDetailClick(entry) {
        this.setState({
            selectedItem: entry,
            showPopup: true
        })
    }

    onClose() {
        this.setState({
            showPopup: false
        })
    }

    onCartClick(entry) {
        const url = " http://localhost:23334/addItemToCart/?uid=" + this.props.userId + "&item_id=" + entry.item.id + "&price=" + entry.buyNowPrice;

        fetch(url)
        .then(res => res.json())
        .then(
            (result) => {
                alert("The item has been added to your shopping cart successfully.");
            },
            (error) => {
                alert("An error occurred when attempted to add the item to your shopping cart...");
            }
        )
    }

    dateFormat(date) {
        var month = parseInt(date.getMonth()) + 1;
        return date.getFullYear() + "/" + month.toString() + "/" + date.getUTCDate() + " " + date.getHours() + ":" + date.getMinutes()
    }

    render() {
        let body = this.props.searchResult.map(entry => {

            var startTime = new Date(entry.startTime)
            var endTime = new Date(entry.endTime)
            var dateS = this.dateFormat(startTime)
            var dateE = this.dateFormat(endTime)

            return (
                <div key={entry.item.id} className="search-row">
                    <div className="search-cell">{entry.item.name}</div>
                    <div className="search-cell">{entry.item.quantity}</div>
                    <div className="search-cell">{dateS}</div>
                    <div className="search-cell">{dateE}</div>
                    <div className="search-cell">{entry.bidStatus}</div>
                    <div className="search-cell">{entry.initPrice}</div>
                    <div className="search-cell">{entry.currentHighestBid}</div>
                    <div className="search-cell">
                        <Button variant="info" onClick={() => this.onDetailClick(entry)}>Detail</Button>
                    </div>
                    <div className="search-cell">
                        { entry.canBuyNow ? <Button variant="info" onClick={() => this.onCartClick(entry)}>Add to cart for ${entry.buyNowPrice}</Button> : null }
                    </div>
                </div>
            )
        })

        return (
            <div>
                <div className="search-result-body">
                    <div className="search-row">
                        <div className="search-cell column-title">Item Name</div>
                        <div className="search-cell column-title">Quantity</div>
                        <div className="search-cell column-title">Start Time</div>
                        <div className="search-cell column-title">End Time</div>
                        <div className="search-cell column-title">Auction Status</div>
                        <div className="search-cell column-title">Start Price</div>
                        <div className="search-cell column-title">Current Highest Bid</div>
                        <div className="search-cell column-title">Detail</div>
                        <div className="search-cell column-title">BuyNow</div>
                    </div>
                    {body}
                    <Button className="back-button" variant="info" onClick={this.props.onClickBack}>Back to search</Button>
                </div>
                {this.state.showPopup ? <Popup userID={this.props.userId} entry={this.state.selectedItem} onClose={this.onClose} show={this.state.showPopup}></Popup> : null}
            </div>
        );
    }
}

export default SearchResultList;