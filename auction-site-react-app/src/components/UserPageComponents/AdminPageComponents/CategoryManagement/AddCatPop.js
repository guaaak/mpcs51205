import React, { Component } from "react";
import "./Category.css";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class AddPopup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            catName: "",
            show: props.show
        }

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(e) {
        const target = e.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        })
    }

    handleSubmit(e) {
        e.preventDefault();

        const url = "http://localhost:8080/auction/item/create/category";

        var data = {
            "categoryId": this.state.catName,
            "categoryName": this.state.catName
        }

        fetch(url, {
            method: "POST",
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
                Accept :'application/json',
                'Origin': 'http://localhost:3000'
            },
            body: JSON.stringify(data),
            referrerPolicy: 'no-referrer'
        })
        .then(res => res.json())
        .then(
            (result) => {
                alert("The category is created successfully.")
                this.props.onClose();
                window.location.reload();
            },
            (error) => {
                alert("An error occured when attempted to create the category...")
            }
        )
    }

    render() {
        return (
            // <div className="modal">
                // <div className="modal-content">
                //     <form onSubmit={e => this.handleSubmit(e)}>
                //         <div>
                //             <span>Category Name: </span>
                //             <input name="catName" type="text" onChange={e => this.handleChange(e)}></input>
                //         </div>
                //         <div>
                //             <input type="submit" value="Add"></input>
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
                                <div>
                                    <span>Category Name: </span>
                                    <input name="catName" type="text" onChange={e => this.handleChange(e)}></input>
                                </div>
                                <div>
                                    <Button type="submit">Add</Button>
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

export default AddPopup;