import React, { Component } from "react";
import './style.css';
// import CountdownWindow from "./CountdownWindow"
import Countdown from "react-countdown";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class Popup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            fetchWindowListStatus: "fetching",
            windowList: [],

            show: props.show
        }
    }

    componentDidMount() {
        const url = "http://localhost:9090/auction/bidding/countdown/" + this.props.userId;

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
                    fetchWindowListStatus: "success",
                    windowList: result.windowList
                })
            },
            (error) => {
                alert("An error occured when attempted to fetch your auction window...")
                this.props.onClose()
            }
        )
    }

    render() {
        let body;

        if (this.state.fetchWindowListStatus === "success") {
            let subbody = this.state.windowList.map(entry => {
                var _date = new Date(entry.startTime);

                return (
                    <div key={entry.itemID} className="window-list-row">
                        <div className="window-list-cell">{entry.itemName}</div>
                        <div className="window-list-cell">
                            <Countdown date={_date}>Started</Countdown>
                        </div>
                    </div>
                )
            })

            body = (
                // <div className="modal">
                //     <div className="modal-content">
                //         <div className="window-list-row">
                //             <div className="window-list-cell">Item Name</div>
                //             <div className="window-list-cell">Countdown til Auction Start</div>
                //         </div>
                //         {subbody}
                //         <button onClick={this.props.onClose}>Back</button>
                //     </div>
                // </div>
                <div>
                    <Modal show={this.state.show}>
                        <Modal.Body>
                            <div>
                                <div className="window-list-row">
                                    <div className="window-list-cell column-title">Item Name</div>
                                    <div className="window-list-cell column-title">Countdown til Auction Start</div>
                                </div>
                                {subbody}
                            </div>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={this.props.onClose}>Close</Button>
                        </Modal.Footer>
                    </Modal>
                </div>
            )
        } else {
            body = (
                <div>
                    <Modal show={this.state.show}>
                        <Modal.Body>
                            <p>Loading...</p>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={this.props.onClose}>Close</Button>
                        </Modal.Footer>
                    </Modal>
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

export default Popup;