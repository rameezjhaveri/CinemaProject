import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import AppNavbar from './AppNavbar';
import AppHomePage from './AppHomePage';
import AppContactUsPage from './AppContactUsPage';
import AppGettingHerePage from './AppGettingHerePage';

class AppPages extends Component {
    render() {
        return (
            <div className='AppPages'>
                <Router>
                    <AppNavbar/>
                    <Switch>
                        { /* Need the 'exact' property, as '/' matches all pages */}
                        <Route path='/' exact component={AppHomePage}/>
                        <Route path='/contact-us' component={AppContactUsPage}/>
                        <Route path='/getting-here' component={AppGettingHerePage}/>
                    </Switch>
                </Router>
            </div>
        )
    }
}

export default AppPages;