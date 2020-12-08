import React, { Component } from "react";
import './style.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class Popup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            show: this.props.show
        }
    }

    render() {
        let list = [];

        for (var key in this.props.bidList) {
            if (this.props.bidList.hasOwnProperty(key)) {
                list.push({'userId': key, 'price': this.props.bidList[key]})
            }
        }

        let body;
        body = list.map(entry => {
            return (
                <div>
                    <div className="bid-history-row">
                        <div className="bid-history-cell"> {entry.userId}</div>
                        <div className="bid-history-cell"> {entry.price}</div>
                    </div>
                </div>
            )
        })

        return (


            // <div className="modal">
            //     <div className="modal-content">
            //         <div className="bid-history-row">
            //             <div className="bid-history-cell">Bidder UserID</div>
            //             <div className="bid-history-cell">Bid Price</div>
            //         </div>
            //         {body}
            //         <button onClick={this.props.onClose}>Cancel</button>
            //     </div>
                
            <div>
                <Modal show={this.state.show}>
                    <Modal.Body>
                        <div>
                            <div className="bid-history-row">
                                <div className="bid-history-cell">Bidder UserID</div>
                                <div className="bid-history-cell">Bid Price</div>
                            </div>
                                {body}
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.props.onClose}>Close</Button>
                    </Modal.Footer>
                </Modal>
            </div>

        );
    }
}

export default Popup;