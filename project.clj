(defproject partner_test "1.0.0-SNAPSHOT"
  :description "Application to facilitate testing the new partner releases."
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.apache.activemq/activemq-core "5.5.0"]
                 [org.slf4j/slf4j-simple "1.5.11"]
                 [postgresql/postgresql "9.0-801.jdbc4"]
                ]
  :dev-dependencies [[swank-clojure "1.3.2"]
                     [org.clojars.ptillemans/lein-cuke "1.1.2-SNAPSHOT"]]
  :main partner_test.main)