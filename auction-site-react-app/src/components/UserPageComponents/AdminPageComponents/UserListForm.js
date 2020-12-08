import React, { Component } from "react";
import Button from 'react-bootstrap/Button';

class UserListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            userListFetchStatus: "fetching",
            userList: []
        }
    }

    componentDidMount() {
        const url = "http://127.0.0.1:23333/getUserList/";

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
                if (result.status === 'success') {
                    result.users.sort(function(a, b) {return a.uid - b.uid});
                    this.setState({
                        userListFetchStatus: "success",
                        userList: result.users
                    })
                } else {
                    this.setState({
                        userListFetchStatus: "fail"
                    })
                }
            },
            (error) => {
                this.setState({
                    userListFetchStatus: "fail"
                })
            }
        )
    }

    suspendOrUnsuspend(isSuspend, userId) {
        if (!isSuspend) {
            console.log(userId)
            const url = "http://localhost:23333/userSuspend/?uid=" + userId;

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
                    alert("The user has been suspended.")
                    window.location.reload()
                },
                (error) => {
                    alert("An error occured when attempted to suspend the user.")
                }
            )
        } else {

            const url = "http://localhost:23333/userUnSuspend/?uid=" + userId;

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
                    alert("The user has been unsuspended.")
                    window.location.reload()
                },
                (error) => {
                    alert("An error occured when attempted to unsuspend the user.")
                }
            )
        }
    }

    remove(userId) {
        const url = "http://localhost:23333/userDelete/?uid=" + userId;
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
                alert("The user has been removed.");
                window.location.reload(); 
            },
            (error) => {
                console.log(error)
                alert("An error occured when attempted to remove the user.");
            }
        )
    }

    render() {
        let body;
        if (this.state.userListFetchStatus === "success") {
            let form = this.state.userList.map(entry => {
                return (
                    <div key={entry.uid} className="user-row">
                        <div className="user-cell">{entry.uid}</div>
                        <div className="user-cell">{ entry.admin ? 'yes' : 'no'}</div>
                        <div className="user-cell">{entry.username}</div>
                        <div className="user-cell">{entry.email}</div>
                        <div className="user-cell">
                            <Button variant="info" onClick={() => this.suspendOrUnsuspend(entry.suspend, entry.uid)}>{entry.suspend ? "Unsuspend" : "Suspend"}</Button>
                        </div>
                        <div className="user-cell">
                            <Button variant="info" onClick={() => this.remove(entry.uid)}>Remove</Button>
                        </div>
                    </div>
                )
            })
            body = (
                <div>
                    <div className="user-row">
                        <div className="user-cell column-title">User ID</div>
                        <div className="user-cell column-title">Is Admin</div>
                        <div className="user-cell column-title">Username</div>
                        <div className="user-cell column-title">Email</div>
                        <div className="user-cell column-title">
                            Suspend
                        </div>
                        <div className="user-cell column-title">
                            Remove
                        </div>
                    </div>
                    {form}
                </div>
            )
        } else if (this.state.userListFetchStatus === "fail") {
            body = (
                <div>
                    <p>An error occured when attempted to load the users...</p>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>The user list is being loaded...</p>
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

export default UserListForm;