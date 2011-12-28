(ns partner-test.ftpclient
  (:use [clj-time
         [core :only [now interval in-secs]]
         [coerce :only [from-long]]])
  (:import (org.apache.commons.net.ftp FTPClient FTPClientConfig)))

(def *ftp-client*)

(defmacro with-ftp
  "Execute body within the context of an ftp connection"
  [host username password & body]
  `(binding [*ftp-client* (FTPClient.)]
     (try
       (doto *ftp-client*
         (.configure
          (doto (FTPClientConfig.)
            (.setServerTimeZoneId "GMT")))
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

(defn list-recent-ftpfiles
  "Return a list of ftp-files object from the current directory which are less than max-age seconds old."
  [max-age]
  (filter
   #(recent? % max-age)
   (.listFiles *ftp-client*)))
