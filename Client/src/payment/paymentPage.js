import React from 'react';
import AppNavbar from './components/AppNavbar';

<script src="https://js.stripe.com/v3/"></script>

var stripe = Stripe('pk_test_TYooMQauvdEDq54NiTphI7jx');

function paymentPage() {
  return ( 
    <div className = "paymentPage" >
      <AppNavbar/>
        stripe.redirectToCheckout({
        // Make the id field from the Checkout Session creation API response
        // available to this file, so you can provide it as parameter here
        // instead of the {{CHECKOUT_SESSION_ID}} placeholder.
        sessionId: '{{CHECKOUT_SESSION_ID}}'
        }).then(function (result) {
            // If `redirectToCheckout` fails due to a browser or network
            // error, display the localized error message to your customer
            // using `result.error.message`.
    });
    </div>
  );
}
export default paymentPage;