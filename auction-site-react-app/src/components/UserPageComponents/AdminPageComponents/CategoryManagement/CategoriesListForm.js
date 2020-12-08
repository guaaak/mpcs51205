import React, { Component } from "react";
import Popup from "./Popup";
import "./Category.css";
import AddPopup from './AddCatPop';
import Button from 'react-bootstrap/Button';

class CategoriesListForm extends Component {
    constructor(props) {
        super(props);

        this.state = {
            categoriesFetchStatus: "fetching",
            categoriesList: [],
            showPopup: false,
            selectedCat: null,

            showAddPop: false
        }

        this.modifyCat = this.modifyCat.bind(this);
        this.onPopupClose = this.onPopupClose.bind(this);
        this.addCat = this.addCat.bind(this);
        this.onCloseAdd = this.onCloseAdd.bind(this);
    }

    componentDidMount() {
        const url = "http://localhost:8080/auction/item/category/all";

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
                    categoriesFetchStatus: 'success',
                    categoriesList: result.categoryList
                })
            },
            (error) => {
                this.setState({
                    categoriesFetchStatus: "fail"
                })
            }
        )
    }

    modifyCat(category) {
        this.setState({
            selectedCat: category,
            showPopup: true
        })
    }

    onPopupClose() {
        this.setState({
            showPopup: false
        })
    }

    removeCat(catId) {
        const url = "http://localhost:8080/auction/item/delete/category/" + catId;

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
                alert("The category has been removed.");
                window.location.reload();
            },
            (error) => {
                alert("An error occured when attemped to remove the category...");
            }
        )
    }

    addCat() {
        this.setState({
            showAddPop: true
        })
    }

    onCloseAdd() {
        this.setState({
            showAddPop: false
        })
    }

    render() {
        let body;

        if (this.state.categoriesFetchStatus === "success") {

            let form = this.state.categoriesList.map(entry => {

                return (
                    <div key={entry.categoryName} className="category-row">
                        <div className="category-cell">{entry.categoryName}</div>
                        <div className="category-cell">
                            <Button variant="info" onClick={() => this.modifyCat(entry)}>modify</Button>
                        </div>
                        <div className="category-cell">
                            <Button variant="info" onClick={() => this.removeCat(entry.categoryId)}>remove</Button>
                        </div>
                    </div>
                )
            })

            body = (
                <div>
                    <div className="category-body">
                        <Button variant="info" onClick={this.addCat}>Add a new category</Button>
                        <div className="category-row">
                            <div className="category-cell column-title">Category Name</div>
                            <div className="category-cell column-title">Modify</div>
                            <div className="category-cell column-title">Remove</div>
                        </div>
                        {form}
                    </div>
                    {this.state.showPopup ? <Popup category={this.state.selectedCat} onClose={this.onPopupClose} show={this.state.showPopup}></Popup> : null }
                    {this.state.showAddPop ? <AddPopup onClose={this.onCloseAdd} show={this.state.showAddPop}></AddPopup> : null }
                </div>
                
            )

        } else if (this.state.categoriesFetchStatus === "fail") {
            body =(
                <div>
                    <p>An error occured when attempted to load the categories...</p>
                </div>
            )
        } else {
            body = (
                <div>
                    <p>The categories are being loaded...</p>
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

export default CategoriesListForm;