(ns partner_test.activemq
  (:import org.apache.activemq.ActiveMQConnectionFactory
           javax.jms.Session
           javax.jms.DeliveryMode))

(defn- create-connection [connectionFactory]
  (let [connection (.createConnection connectionFactory)]
    (.start connection)
    connection))

(defn create-session [& {:keys [brokerurl]
                         :or {brokerurl "tcp://partner-test.elex.be:61616"}}]
  (let [connectionFactory (ActiveMQConnectionFactory. brokerurl)
        connection (create-connection connectionFactory)]
    (.createSession connection false Session/AUTO_ACKNOWLEDGE)))

(defn create-bytemessage [session & {:keys [body stringproperties intproperties]}]
  (let [message (.createBytesMessage session)]
    (.writeBytes message body)
    (doseq [k (keys stringproperties)] (.setStringProperty message k (get stringproperties k)))
    (doseq [k (keys intproperties)] (.setIntProperty message k (get intproperties k)))
    message))

(defn send-to-queue [message session queuename]
  (let [queue (.createQueue session queuename)
        producer (.createProducer session nil)]
    (.setDeliveryMode producer DeliveryMode/NON_PERSISTENT)
    (.send producer queue message)))
