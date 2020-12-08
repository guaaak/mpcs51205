import './App.css';
import Header from "./components/Header"
import Homepage from "./components/Homepage"
import Login from "./components/LoginComponents/Login"
import Register from "./components/RegisterComponents/Register"
import { BrowserRouter as Router, Route } from "react-router-dom";
import RegisterSuccess from './components/RegisterComponents/RegisterSuccess';
import UserPage from './components/UserPageComponents/UserPage';
import ErrorPage from './components/ErrorPage';
import ManageAccountPage from './components/UserPageComponents/NonAdminPageComponents/ManageAccountPage';
import MyAuctions from './components/UserPageComponents/NonAdminPageComponents/MyAuctionsComponents/MyAuctions';
import MyBids from './components/UserPageComponents/NonAdminPageComponents/MyBidsComponents/MyBids';
import ListAuction from './components/UserPageComponents/NonAdminPageComponents/MyAuctionsComponents/ListAuction';
import UpdateAuction from './components/UserPageComponents/NonAdminPageComponents/MyAuctionsComponents/UpdateAuction';
import Watchlist from './components/UserPageComponents/NonAdminPageComponents/WatchlistComponents/Watchlist';

function App() {
  return (
    <Router>
      <div className="App">
        <Header></Header>
        <Route exact path="/" component={Homepage} />

        <Route exact path="/login" component={Login} />
        <Route exact path="/login/fail" render={ (props) => <ErrorPage {...props} errorMessage="Your login failed..." route="/login" buttonName="Retry login" /> } />

        <Route exact path="/register" component={Register} />
        <Route exact path="/register/fail" render={ (props) => <ErrorPage {...props} errorMessage="Your register failed..." route="/register" buttonName="Retry register" /> } />
        <Route exact path="/register/success" component={RegisterSuccess} />

        <Route exact path="/user/:usertype/:userid" component={UserPage} />
        <Route exact path="/user/fail" render={ (props) => <ErrorPage {...props} errorMessage="Your contents failed to load due to some reasons..." route="/" buttonName="Return to homepage" /> } />

        <Route exact path="/manageaccount/:userid" component={ManageAccountPage} />
        <Route exact path="/myauctions/:userid" component={MyAuctions} />
        <Route exact path="/myauctions/fail/:userid" render={ (props) => <ErrorPage {...props} errorMessage="Your auctions failed to load due to some reasons..." route="/user/nonadmin/" buttonName="Return to user page" /> } />
        
        <Route exact path="/mybids/:userid" component={MyBids} />
        <Route exact path="/mybids/fail/:userid" render={ (props) => <ErrorPage {...props} errorMessage="Your bids failed to load due to some reasons..." route="/user/nonadmin/" buttonName="Return to user page" /> } />

        <Route exact path="/mywatchlist/:userid" component={Watchlist} />

        <Route exact path="/listauction/:userid" component={ListAuction} />
        <Route exact path="/updateauction/:userid/:auctionid" component={UpdateAuction} />
        <Route exact path="/updateauctionfail/:userid" render={ (props) => <ErrorPage {...props} errorMessage="Your auction failed to load due to some reasons..." route="/myauctions/" buttonName="Return to myAuctions page" /> } />
      </div>
    </Router>
  );
}

export default App;
