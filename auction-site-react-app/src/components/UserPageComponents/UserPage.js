import React, { Component } from "react";
import AdminPage from "./AdminPageComponents/AdminPage";
import NonAdminPage from "./NonAdminPageComponents/NonAdminPage";

class UserPage extends Component {
    constructor(props) {
        super(props);
        this.state ={ 
            userType: props.match.params.usertype,
            userId: props.match.params.userid
        }
    }

    render() {
        const isAdmin = this.state.userType === "admin" ? true : false;
        let subpage;
        if (isAdmin) {
            subpage = <AdminPage userId={this.state.userId}></AdminPage>;
        } else {
            subpage = <NonAdminPage userId={this.state.userId}></NonAdminPage>
        }
        return (
            <div>
                {subpage}
            </div>
        );
    }
}

export default UserPage;