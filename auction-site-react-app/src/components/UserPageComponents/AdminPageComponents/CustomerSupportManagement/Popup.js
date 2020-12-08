import React, { Component } from "react";
import './style.css';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

class Popup extends Component {
    constructor(props) {
        super(props);

        this.state = {
            replyTitle: "Enter your subject...",
            replyContent: "Enter your reply...",
            showReplyPanel: false,

            show: props.show
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleReply = this.handleReply.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();

        const url = "http://localhost:5000/notification/reply_email";

        var data = {
            "email_to": this.props.email.from,
            "subject": this.state.replyTitle,
            "text": this.state.replyContent
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
                alert("Your reply has been successfully sent to the customer.")
                this.props.onClose();
                window.location.reload();
            },
            (error) => {
                alert("An error occured when attempted to send the reply...")
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

    handleReply() {
        this.setState({
            showReplyPanel: true
        })
    }

    render() {
        let replyPanel = (
            <div>
                <form onSubmit={e => this.handleSubmit(e)} id="replyForm">
                    <input className="subject" name="replyTitle" onChange={e => this.handleChange(e)} value={this.state.replyTitle}></input>
                    <textarea name="replyContent" form="replyForm" onChange={e => this.handleChange(e)} value={this.state.replyContent} />
                    <div>
                        <Button variant="info" type="submit">Send</Button>
                    </div>
                </form>
            </div>
        )

        return (
            // <div className="modal">
                // <div className="modal-content">
                //     <div className="modal-entry">
                //         <span>Title: </span>
                //         <div>{this.props.email.subject}</div>
                //     </div>
                //     <div className="modal-entry">
                //         <span>User ID: </span>
                //         <div>{this.props.email.from}</div>
                //     </div>
                //     <div className="modal-entry">
                //         <div>Content: </div>
                //         <div>{this.props.email.text}</div>
                //     </div>
                //     { this.state.showReplyPanel ? null : (
                //         <div>
                //             <button onClick={this.handleReply}>Reply</button>
                //             <button onClick={this.props.onClose}>Cancel</button>
                //         </div>
                //         ) }
                //     { this.state.showReplyPanel ? replyPanel : null }
                // </div>
            // </div>
            <div>
                <Modal show={this.state.show}>
                    <Modal.Body>
                        <div>
                            <div className="modal-entry">
                                <span>Title: </span>
                                <div>{this.props.email.subject}</div>
                            </div>
                            <div className="modal-entry">
                                <span>User ID: </span>
                                <div>{this.props.email.from}</div>
                            </div>
                            <div className="modal-entry">
                                <div>Content: </div>
                                <div>{this.props.email.text}</div>
                            </div>
                            { this.state.showReplyPanel ? null : (
                                <div>
                                    <Button variant="info" onClick={this.handleReply}>Reply</Button>
                                </div>
                                ) }
                            { this.state.showReplyPanel ? replyPanel : null }
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