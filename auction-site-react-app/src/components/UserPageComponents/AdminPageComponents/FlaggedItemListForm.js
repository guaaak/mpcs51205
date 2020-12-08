import React, { Component } from "react";

class FlaggedItemListForm extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            flaggedItemListFetchStatus: "fetching",
            flaggedItemList: []
        }
    }

    componentDidMount() {
        const url = "http://localhost:8080/auction/item/flagged";

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
                   flaggedItemListFetchStatus: "success",
                   flaggedItemList: result.flaggedItemList
               })
            },
            (error) => {
                this.setState({
                    flaggedItemListFetchStatus: "fail"
                })
            }
        )
    }

    render() {
        let body;

        if (this.state.flaggedItemListFetchStatus === "success") {

            let form = this.state.flaggedItemList.map(entry => {
                let flaggedBy = "";
                const flaggedList = entry.flaggedByUserIdList;
                for (var i = 0; i < flaggedList.length; i++) {
                    flaggedBy = flaggedBy + flaggedList[i];
                    if (i !== flaggedList.length - 1) {
                        flaggedBy = flaggedBy + ", "
                    }
                }

                return (
                    <div key={entry.itemId} className="flagged-row">
                        <div className="flagged-cell">{entry.itemName}</div>
                        <div className="flagged-cell">{entry.itemId}</div>
                        <div className="flagged-cell">{flaggedBy}</div>
                    </div>
                )
            })

            body = (
                <div>
                    <div className="flagged-row">
                        <div className="flagged-cell column-title">Item Name</div>
                        <div className="flagged-cell column-title">Item ID</div>
                        <div className="flagged-cell column-title">Flagged By (userid)</div>
                    </div>
                    {form}
                </div>
            )
        } else if (this.state.flaggedItemListFetchStatus === "fail") {
            body =(
                <div>
                    <p>An error occured when attempted to load the flagged items...</p>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>The flagged items are being loaded...</p>
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

export default FlaggedItemListForm;