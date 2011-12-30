(use '(partner-test core ftpclient exceptions))
(use '(clojure.contrib str-utils))
(use '[clojure.string :only (blank?)])
(use 'clojure.test)
(use '[clj-time core coerce])


(import [java.net URI URLDecoder])

(def broker-url (atom "tcp://ewaf-test.colo.elex.be:61616"))
(def lots (ref {}))
(def locations (ref {}))

(defn reset-all! []
  (dosync
   (ref-set lots {})
   (ref-set locations {})))

(defn add-lot!
  [lot device results]
  (let
   [lot-map {:lot lot :device device :results results}]
   (dosync (alter lots merge lot-map))))

(defn uri-decode [s]
  (URLDecoder/decode s))

(defn location-query-entry
  [s]
  (if (blank? s)
    {}
    (let
        [[k value] (re-split #"=" s)]
      (hash-map
       (keyword (uri-decode k))
       (uri-decode value)))))

(defn parse-query
  [query]
  (if query
    (let [pairs (re-split #"&" query)]
      (apply merge (map location-query-entry pairs)))
    {}))

(defn parse-location-uri [location]
  (let [uri (URI. location)
        scheme (.getScheme uri)
        host (.getHost uri)
        port (.getPort uri)
        user-info (.getUserInfo uri)
        [user pass] (re-split #":" user-info)
        path (.getPath uri)
        query (.getRawQuery uri)
        base-info  {:scheme scheme
                    :host host
                    :user user
                    :password pass
                    :path path
                    :directory (.substring path 1)
                    :query query}
        query-info  (parse-query query)]
    (merge base-info query-info)))

(defn add-location!
  [partner location]
  (dosync
   (alter locations
          assoc partner (parse-location-uri location))))


(defn list-uploaded-files
  "Return a list names of all files at that loactaion"
  [location max-age]
  (with-ftp (:host location) (:user location) (:password location)
    (change-working-directory (:directory location))
    (list-recent-files max-age))) ; server-offset is a dirty hack !!!!

(defn list-confirmed-files
  "Return a list names of all files at that loactaion"
  [location]
  (with-ftp (:host location) (:user location) (:password location)
    (change-working-directory (:move location))
    (list-files)))

(defn count-matching-uploaded-files
  "List the files in the directory moved to after confirmation and count the ones matching a given reg-ex"
  [location regex max-age]
  (println "Loaction : " location)
  (println "searching for files like " regex " no older than " max-age)
  (count
   (filter #(re-matches regex %)
           (list-uploaded-files (@locations location) max-age))))

(defn count-matching-confirmed-files
  "List the files in the directory moved to after confirmation and count the ones matching a given reg-ex"
  [location regex]
  (println location)
  (println regex)
  (count
   (filter #(re-matches regex %)
           (list-confirmed-files (@locations location)))))

(defn assert-with-retry
  "Retries a predicate till it is true. Throws exceptoin when timeout expires"
  [predicate timeout]
  (let [deadline (plus (now) (secs timeout))]
    (while (not (predicate))
      (assert (before? (now) deadline))
      (Thread/sleep 5000))))

(Given #"Broker is listening at (.*)"
       (fn [url]
         (reset! broker-url url)))

(Given #"Lot (.*) of device (.*) with wafer results (.*)"
       (fn
         [lot device results-str]
         (let [results (re-split #"," results-str)]
           (add-lot! lot device results)))
       )

(Given #"Partner (.*) confirms wafermaps at location (.*)"
       add-location!)

(Given #"Partner (.*) expects wafermaps at location (.*)"
       add-location!)

(Given #"Lot (.*) is shipped to (.*)"
       (fn [lot partner]
         (trigger {lot partner} :brokerurl @broker-url)))

(Given #"I expect (.*) confirmed files like (.*) within (.*) seconds from (.*)"
       (fn [n template timeout partner]
         (let [nr-wafers (Integer/parseInt n)
               regex (re-pattern template)
               nr-found (count-matching-confirmed-files partner regex)]
           (println (str "    Expected : " nr-wafers ", Found : " nr-found ))
           (assert (= nr-wafers nr-found)))))

(Given #"I expect (.*) uploaded files like (.*) within (.*) seconds for (.*)"
       (fn [n template timeout partner]

         (let [nr-wafers (Integer/parseInt n)
               regex (re-pattern template)
               nsecs (Integer/parseInt timeout)]

           (defn expected-equals-actual []
             (let [nr-found (count-matching-uploaded-files partner regex nsecs)]
               (println (str "    Expected : " nr-wafers ", Found : " nr-found " at " (now) ))
               (= nr-wafers nr-found)))

           (assert-with-retry expected-equals-actual nsecs))))


(Given #"an 0(useexception occurs with message (.*)"
       send-exception-message)

