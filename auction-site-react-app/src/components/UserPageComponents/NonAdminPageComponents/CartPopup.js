import React, { Component } from "react";
import './style.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class CartPopup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            fetchCartStatus: "fetching",
            cart: [],
            show: props.show
        }

        this.onClickCheckout = this.onClickCheckout.bind(this);
    }

    componentDidMount() {
        this.fetchCart()
    }

    fetchCart() {
        console.log(this.props.userId)
        const url = "http://localhost:23334/getItemsInCart/?uid=" + this.props.userId;
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
                this.setState({
                    fetchCartStatus: "success",
                    cart: result.records
                })
            },
            (error) => {
                alert("An error occured when attempted to fetch your cart...")
                this.props.onClose()
            }
        )
    }

    onClickCheckout() {

        const url = "http://localhost:23334/checkout/?uid=" + this.props.userId;

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
                alert("Your have successfully checked out the items.")
                this.fetchCart()
            },
            (error) => {
                console.log(error)
                alert("An error happened when trying to check out the items...")
            }
        )
    }

    onClickDelete(id) {
        const url = "http://localhost:23334/deleteItemInCart/?uid=" + this.props.userId + "&item_id=" + id;
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
                if (result.status === 'fail') {
                    alert("An error happened when trying to delete the item...")
                } else {
                    alert("Your have successfully deleted the item.")
                    this.fetchCart()
                }
            },
            (error) => {
                alert("An error happened when trying to delete the item...")
            }
        )
    }

    render() {
        let body;

        if (this.state.fetchCartStatus === "success") {
            let subbody = this.state.cart.map(entry => {
                return (
                    <div className="window-list-row">
                        <div className="window-list-cell">{entry.item_name}</div>
                        <div className="window-list-cell">{entry.item_price}</div>
                        <div className="window-list-cell">
                            <Button variant="info" onClick={() => this.onClickDelete(entry.item_id)}>delete</Button>
                        </div>
                    </div>
                )
            })

            body = (
                // <div className="modal">
                    // <div className="modal-content">
                    //     <div className="window-list-row">
                    //         <div className="window-list-cell">Item Name</div>
                    //         <div className="window-list-cell">Winning Price</div>
                    //         <div className="window-list-cell">Delete Item</div>
                    //     </div>
                    //     {subbody}
                    //     <button onClick={this.props.onClose}>Back</button>
                    //     <button onClick={() => this.onClickCheckout()}>checkout</button>
                    // </div>
                // </div>
                <div>
                    <Modal show={this.state.show}>
                        <Modal.Body>
                            <div>
                                <div className="window-list-row">
                                    <div className="window-list-cell column-title">Item Name</div>
                                    <div className="window-list-cell column-title">Winning Price</div>
                                    <div className="window-list-cell column-title">Delete Item</div>
                                </div>
                                {subbody}
                                <Button className="checkout-button" variant="info" onClick={() => this.onClickCheckout()}>checkout</Button>
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

export default CartPopup;