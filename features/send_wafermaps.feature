Feature: Send Wafermaps
  In order to reduce manual work
  As a Probing Process Resposible
  I want inkless wafermaps sent automatically to partners after shipping


#Scenario: Shipment to Carsem2
#  Given Lot B89476 of device 31409 with wafer results 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
#  Given Partner O_CARSEM2 expects wafermaps at location ftp://carsem@openft-uat.elex.be:21/?directory=/wafermaps/s-site&password=carsem&move=/confirmed/s-site&delay=900000&disconnect=true
#  When Lot B89476 is shipped to O_CARSEM2
#  Then I expect 24 uploaded files like B89476.* within 120 seconds for O_CARSEM2

Scenario: Shipment to Carsem2
  Given Partner O_CARSEM2 expects wafermaps at location ftp://carsem@openft-uat.elex.be:21/?directory=/wafermaps/s-site&password=carsem&move=/confirmed/s-site&delay=900000&disconnect=true
  When Lot B89476 is shipped to O_CARSEM2
  Then I expect 24 uploaded files like B89476.* within 240 seconds for O_CARSEM2
