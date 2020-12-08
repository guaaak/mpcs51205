import React, { Component } from "react";
import './style.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class Popup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            itemId: "",
            lessThanPrice: 0,
            show: props.show
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this)
    }

    handleSubmit(e) {
        e.preventDefault();

        const url = "http://localhost:23333/addItemToWatchlist/?uid=" + this.props.userId + "&item_id="+ this.state.itemId + "&criteria=" + this.state.lessThanPrice;
        console.log(url)
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
                alert("Your item has been added to your watchlist.")
                this.props.onClose();
                window.location.reload();
            },
            (error) => {
                console.log(error)
                alert("An error occured when attempted to add to your watchlist...")
            }
        )
    }

    handleChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    render() {
        return (
            // <div className="modal">
                // <div className="modal-content">
                //     <form onSubmit={e => this.handleSubmit(e)}>
                //         <div className="modal-entry">
                //             <span>Item Name: </span>
                //             <input name="itemId" type="text" onChange={e => this.handleChange(e)}></input>
                //         </div>
                //         <div className="modal-entry">
                //             <span>Notify when price less than: </span>
                //             <input name="lessThanPrice" type="number" onChange={e => this.handleChange(e)}></input>
                //         </div>
                //         <div className="modal-entry">
                //             <input type ="submit" value="Add"></input>
                //             <button onClick={this.props.onClose}>Cancel</button>
                //         </div>
                //     </form>
                // </div>
            // </div>
            <div>
                <Modal show={this.state.show}>
                    <Modal.Body>
                        <div>
                            <form onSubmit={e => this.handleSubmit(e)}>
                                <div className="modal-entry">
                                    <span>Item ID: </span>
                                    <input name="itemId" type="text" onChange={e => this.handleChange(e)}></input>
                                </div>
                                <div className="modal-entry">
                                    <span>Notify when price less than: </span>
                                    <input name="lessThanPrice" type="number" onChange={e => this.handleChange(e)}></input>
                                </div>
                                <div className="modal-entry">
                                    <Button type ="submit" variant="info">Add</Button>
                                </div>
                            </form>
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