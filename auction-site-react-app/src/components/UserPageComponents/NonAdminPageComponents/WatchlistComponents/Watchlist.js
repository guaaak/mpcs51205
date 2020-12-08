import React, { Component } from "react";
import { Link } from "react-router-dom";
import Popup from "./Popup";
import Button from 'react-bootstrap/Button';

class Watchlist extends Component {
    constructor(props) {
        super(props);

        this.state = {
            userId: props.match.params.userid,

            fetchWatchlistStatus: "fetching",
            watchlist: [],

            showPopup: false
        }

        this.onClickAddWatchlist = this.onClickAddWatchlist.bind(this);
        this.onClose = this.onClose.bind(this);
    }

    componentDidMount() {
        const url = "http://localhost:23333/getItemsInWatchlist/?uid=" + this.state.userId;

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
                    fetchWatchlistStatus: "success",
                    watchlist: result.records
                })
            },
            (error) => {
                alert("An error occured when attempted to fetch your watchlist...")
                window.location.href = "/user/nonadmin/" + this.state.userId

            }
        )
    }

    onClickAddWatchlist() {
        console.log("showup")
        this.setState({
            showPopup: true
        })
        console.log(this.state.showUp)
    }

    onClose() {
        this.setState({
            showPopup: false
        })
    }

    render() {
        let body;

        if (this.state.fetchWatchlistStatus === "success") {
            let list = this.state.watchlist.map(entry => {
                return (
                    <div key={entry.item_name} className="watchlist-row">
                        <div className="watchlist-cell">{entry.item_name}</div>
                        <div className="watchlist-cell">{entry.criteria}</div>
                    </div>
                )
            })

            body = (
                <div>
                    <div className="watchlist-row">
                        <div className="watchlist-cell column-title">Watchlist Item Name</div>
                        <div className="watchlist-cell column-title">Notify When Price is Less Than</div>
                    </div>
                    {list}
                </div>
            )
        } else {
            body = (
                <div>
                    <p>Your watchlist is being loaded...</p>
                </div>
            )
        }

        return (
            <div>
                <div className="watchlist-body">
                    <Link to={`/user/nonadmin/` + this.state.userId}>
                        <Button className="auction-button">back to user page</Button>
                    </Link>
                    <Button className="auction-button" onClick={this.onClickAddWatchlist}>add to watchlist</Button>
                    {body}
                </div>
                {this.state.showPopup ? <Popup userId={this.state.userId} onClose={this.onClose} show={this.state.showPopup}></Popup> : null}
            </div>
        );
    }
}

export default Watchlist;