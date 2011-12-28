Feature: Send Wafermaps
  In order to reduce manual work
  As a Probing Process Resposible
  I want inkless wafermaps sent automatically to partners after shipping


#Scenario: Shipment to Carsem2
#  Given Lot B89476 of device 31409 with wafer results 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25
#  Given Partner O_CARSEM2 expects wafermaps at location ftp://carsem@openft-uat.elex.be:21/?directory=/wafermaps/s-site&password=carsem&move=/confirmed/s-site&delay=900000&disconnect=true
#  When Lot B89476 is shipped to O_CARSEM2
#  Then I expect 24 uploaded files like B89476.* within 120 seconds for O_CARSEM2

Background:
  Given Partner O_CARSEM2 expects wafermaps at location ftp://carsem@openft-uat.elex.be:21/?directory=/wafermaps/s-site&password=carsem&move=/confirmed/s-site&delay=900000&disconnect=true
  Given Partner O_CARSEM1 expects wafermaps at location ftp://carsem@openft-uat.elex.be:21/?password=carsem&directory=wafermaps/m-site&disconnect=true
  Given Partner O_AMKOR_P3 expects wafermaps at location ftp://amkor@openft-uat.elex.be:21/?password=amkor&disconnect=true
  Given Partner O_ISPL expects wafermaps at location ftp://ispl@openft-uat.elex.be:21/?password=6gZ5flFv&disconnect=true
  Given Partner O_UNISEM expects wafermaps at location ftp://unisem@openft-uat.elex.be:21/?password=wrinweho&disconnect=true


Scenario: Shipment to Carsem2
  When Lot B89476 is shipped to O_CARSEM2
  Then I expect 24 uploaded files like B89476.* within 60 seconds for O_CARSEM2

Scenario: Shipment UMC wafers to Carsem2
  When Lot HM9PF is shipped to O_CARSEM2
  Then I expect 25 uploaded files like HM9PF.* within 60 seconds for O_CARSEM2

Scenario: Shipment UMC wafers of a split lot to Carsem2
  When Lot HN4T7X2 is shipped to O_CARSEM2
  Then I expect 17 uploaded files like HN4T7.* within 60 seconds for O_CARSEM2

Scenario: Shipment to Carsem1
  When Lot T38910 is shipped to O_CARSEM1
  Then I expect 22 uploaded files like T38910-\d\d-.* within 60 seconds for O_CARSEM1


Scenario Outline: Rest of test lots for Erfurt
  When Lot <Lot> is shipped to <Partner>
  Then I expect <NrWafers> uploaded files like <Template> within 60 seconds for <Partner>

  Examples:
    | Lot      | NrWafers | Partner    | Template |
    | HMPGP    |   25     | O_AMKOR_P3 | HMPGP.*  |
    | HNQFC    |   25     | O_AMKOR_P3 | HNQFC.*  |
    | C20629   |   25     | O_ISPL     | C20629.* |
    | M31469   |   25     | O_ISPL     | M31469.* |
    | B42877A  |    8     | O_UNISEM   | B42877.* |
    | B88957   |   24     | O_UNISEM   | B88957.* |
    | B89958   |   23     | O_UNISEM   | B89958.* |
    | B89620   |   24     | O_UNISEM   | B89620.* |
    | E02264   |   11     | O_ISPL     | E02264.* |
    | E03340   |    7     | O_ISPL     | E03340.* |
    | E06963   |   11     | O_ISPL     | E06963.* |
    | Q6B741.1 |    2     | O_ISPL     | Q6B741.* |
    | Q6B061   |    0     | O_ISPL     | Q6B061.* |
    | M31265   |   15     | O_ISPL     | M31265.* |


Scenario Outline: Test lots for Ieper
  When Lot <Lot> is shipped to <Partner>
  Then I expect <NrWafers> uploaded files like <Template> within 60 seconds for <Partner>

  Examples:
    | Lot     | NrWafers | Partner    | Template |
    | T39144  |    24    | O_AMKOR_P1 | T39144.* |
    | T39224  |    24    | O_AMKOR_P1 | T39224.* |
    | B87545  |    24    | O_ISPL     | B87545.* |
    | T42859  |    25    | O_ISPL     | T42859.* |
    | B85599  |    11    | O_UNISEM   | B85599.* |
    | B89675  |    24    | O_AMKOR_P1 | B89675.* |
    | M31744  |     4    | O_ISPL     | M31744.* |
    | M31744J |     4    | O_ISPL     | M31744.* |


Scenario Outline: Test lots for Sofia
  When Lot <Lot> is shipped to <Partner>
  Then I expect <NrWafers> uploaded files like <Template> within 60 seconds for <Partner>

  Examples:
    | Lot        | NrWafers | Partner  | Template |
    | T36614     |   25     | O_ISPL   | T36614.* |
    | T35379X2   |   20     | O_ISPL   | T35379.* |
    | P29166.1X4 |    6     | O_ISPL   | P29166.* |
    | T36623     |   25     | O_ISPL   | T36623.* |
    | B89910     |   22     | O_ISPL   | B89910.* |
    | T36807     |   25     | O_ISPL   | T36807.* |
    | T42018     |   25     | O_UNISEM | T42018.* |
    | T41930     |   25     | O_ISPL   | T41930.* |
    | B89909     |   21     | O_ISPL   | B89909.* |

