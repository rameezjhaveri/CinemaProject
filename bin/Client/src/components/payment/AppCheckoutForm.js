import React, {Component} from 'react';
import {CardElement, injectStripe} from 'react-stripe-elements';


class CheckoutForm extends Component {
  constructor(props) {
    super(props);
    this.state = {complete: false};
    this.submit = this.submit.bind(this);
  }

  async submit(ev) {
    let {token} = await this.props.stripe.createToken({name: "Name"});
    
    let response = await fetch("http://localhost:8080/payment", {
        method: "POST",
        headers: {"Content-Type": "text/plain"},
        body: token.id
    });
    
    if (response.ok) this.setState({complete: true})
    console.log(response)
  }

  render() {
    if (this.state.complete) return <h1>Purchase Complete</h1>;
    return (
      <div className="checkout">
        <CardElement />
        <button onClick={this.submit}>Send</button>
      </div>
    );
  }
}

export default injectStripe(CheckoutForm);