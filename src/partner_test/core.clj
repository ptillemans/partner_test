(ns partner-test.core
  (:use partner-test.postgres
        partner-test.activemq))

(defn- trigger-wafermap [wafermap partner]
  (let [msg (create-bytemessage :body (:wafermap wafermap)
                                :stringproperties {"lotName" (:lotname wafermap)
                                                   "condition" ""
                                                   "devicename" "blaat"
                                                   "item" (:item wafermap)
                                                   "partner" partner}
                                :intproperties {"waferNumber" (:wafer wafermap)
                                                "totalWafersInLot" 1})]
    (send-to-queue msg "partner")))


(defn trigger [lot-destinations & {:keys [brokerurl]
                                   :or {brokerurl "tcp://partner-test.elex.be:61616"}}]
  (with-session (str brokerurl)
    (doseq [lotname (keys lot-destinations)]
      (let [wafermaps (valid-wafermaps :lotname lotname)]
        (doseq [wafermap wafermaps]
          (trigger-wafermap wafermap (get lot-destinations lotname)))))))



