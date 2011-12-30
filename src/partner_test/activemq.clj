(ns partner-test.activemq
  (:import org.apache.activemq.ActiveMQConnectionFactory
           javax.jms.Session
           javax.jms.DeliveryMode))

(defn create-connection [broker-url]
  (let [connection (.createConnection (ActiveMQConnectionFactory. broker-url))]
    (.start connection)
    connection))

(def *connection*)
(def *session*)
(defmacro with-session
  "Execute body within the context an activemq session"
  [broker-url & body]

  `(binding [*connection* (create-connection ~broker-url)]
     (binding [*session* (.createSession *connection* false Session/AUTO_ACKNOWLEDGE)]
       (println *connection*)
       (println *session*)
       (try
         (try
           (do ~@body)
           (finally
            (.close *session*)))
         (finally
          (.close *connection*))))))

(defn- add-properties
  "Add the properties specified by string-properties and int-properties to the message and returns the message"
  [message string-properties int-properties]
  (doseq [k (keys string-properties)] (.setStringProperty message k (get string-properties k)))
  (doseq [k (keys int-properties)] (.setIntProperty message k (get int-properties k)))
  message)

(defn create-bytemessage
  [& {:keys [body stringproperties intproperties]} ]
  (let [message (.createBytesMessage *session*)]
    (.writeBytes message body)
    (add-properties message stringproperties intproperties)
    message))

(defn create-textmessage
  [& {:keys [body stringproperties intproperties]}]
  (let [message (.createTextMessage *session*)]
    (.setText message body)
    (add-properties message stringproperties intproperties)
    message))

(defn send-to-queue [message queuename]
  (let [queue (.createQueue *session* queuename)
        producer (.createProducer *session* nil)]
    (.setDeliveryMode producer DeliveryMode/NON_PERSISTENT)
    (.send producer queue message)))


