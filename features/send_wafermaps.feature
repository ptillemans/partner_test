Feature: Send Wafermaps
  In order to reduce manual work
  As a Probing Process Resposible
  I want inkless wafermaps sent automatically to partners after shipping


Scenario: Shipment to Carsem2
  Given Lot B89476 of device 31409
  Given Partner O_CARSEM2 expects incoming wafermaps in directory ftp://user:pass@ftp.carsem.com/incoming
  Given Partner O_CARSEM2
  When Lot is shipped
  Then I expect 25 filenames like .*B89476.*  within 120 seconds
