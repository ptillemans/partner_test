(ns partner-test.ftpclient
  (:use [clj-time core coerce])
  (:import org.apache.commons.net.ftp.FTPClient))

(def *ftp-client*)

(defmacro with-ftp
  "Execute body within the context of an ftp connection"
  [host username password & body]
  `(binding [*ftp-client* (FTPClient.)]
     (try
       (doto *ftp-client*
         (.connect ~host)
         (.login ~username ~password))
       ~@body
       (finally
        (.disconnect *ftp-client*)))))

(defn change-working-directory
  "Change current directory"
  [new-dir]
  (.changeWorkingDirectory *ftp-client* new-dir))

(defn list-files
  "Return a list of filenames from the current directory."
  []
  (map
   #(.getName %)
   (.listFiles *ftp-client*)))


(defn recent?
  "Return true if the file's timestamp is less than max-age seconds ago"
  [ftp-file max-age]
  (let [ts (from-long (-> ftp-file .getTimestamp .getTime))
        age (in-secs (interval ts (now)))]
    (< age max-age)))

(defn list-recent-files
  "Return a list of files from the current directory which are less than max-age seconds old."
  [max-age]
  (map
   #(.getName %)
   (filter
    #(recent? % max-age)
    (.listFiles *ftp-client*))))
