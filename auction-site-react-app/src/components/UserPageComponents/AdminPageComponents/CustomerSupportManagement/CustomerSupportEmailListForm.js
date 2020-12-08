import React, { Component } from "react";
import Popup from './Popup';
import './style.css';
import Button from 'react-bootstrap/Button';

class CustomerSupportEmailListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            emailFetchStatus: "fetching",
            emailList: [],
            selectedEmail: null,
            showPopup: false
        }

        this.onClose = this.onClose.bind(this);
    }

    componentDidMount() {
        const url = "http://localhost:5000/notification/receive_email";

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
                this.setState({
                    emailFetchStatus: "success",
                    emailList: result.email_list
                })
            },
            (error) => {
                // this.setState({
                //     emailFetchStatus: "fail"
                // })
                console.log(error)
                this.setState({
                    emailFetchStatus: "success",
                    emailList: [
                        {
                            subject: "subject",
                            from: "xx@xxx",
                            text: "ttttttttt"
                        },
                        {
                            subject: "subject",
                            from: "xx@xxx",
                            text: "ttttttttt"
                        },
                        {
                            subject: "subject",
                            from: "xx@xxx",
                            text: "ttttttttt"
                        }
                    ]
                })
            }
        )
    }

    onClick(entry) {
        this.setState({
            selectedEmail: entry,
            showPopup: true
        })
    }

    onClose() {
        this.setState ({
            showPopup: false
        })
    }

    render() {
        let body;

        if (this.state.emailFetchStatus === "success") {

            let form = this.state.emailList.map(entry => {
                return (
                    <div key={entry.subject} className="email-row">
                        <div className="email-cell">{entry.from}</div>
                        <div className="email-cell">{entry.subject}</div>
                        <div className="email-cell">
                            <Button variant="info" onClick={() => this.onClick(entry)}>Reply</Button>
                        </div>
                    </div>
                )
            })

            body = (
                <div>
                    <div className="email-section-body">
                        <div className="email-row">
                            <div className="email-cell column-title">From</div>
                            <div className="email-cell column-title">Email Title</div>
                            <div className="email-cell column-title">Reply</div>
                        </div>
                        {form}
                    </div>
                    {this.state.showPopup ? <Popup email={this.state.selectedEmail} onClose={this.onClose} show={this.state.showPopup}></Popup> : null}
                </div>
            )
        } else if (this.state.flaggedItemListFetchStatus === "fail") {
            body =(
                <div>
                    <p>An error occured when attempted to load the emails...</p>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>The emails are being loaded...</p>
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

export default CustomerSupportEmailListForm;