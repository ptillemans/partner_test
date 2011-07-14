# partner_test

Partner_test is a framework for functional testing a new partner release.  It allows you to resend wafermaps to a given partner.

## Usage

First get all the dependencies using lein

    lein deps

Now you start a swank server to be able to connect using slime

    lein swank

Connect with emacs,  you should now be connected to the project.  To trigger:

    (partner_test.core/trigger {"A12345" "O_PACTECH"
                                "A54321" "O_CARSEM"})

This will trigger all wafermaps for both lots to their subcontractor.
