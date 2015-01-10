(ns dadadata.core
  (:gen-class)
  (:require [taoensso.timbre :as log]
            [clojure.tools.reader.edn :as edn]
            [clojure-csv.core :as csv]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [datomic.api :as d]
            ))



;; Define the DB's uri globally until I'll have something better
(def uri "datomic:dev://localhost:23456/dadadata1")



;; Create database and load schema
(defn datomic-init
  [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)
        schema-tx (read-string (slurp "etc/datomic-schema.edn"))]
    @(d/transact conn schema-tx)
    )
  )


;; Perform transactions from file
(defn datomic-edn-transact
  [file]
  (let [conn (d/connect uri)
        tx (read-string (slurp file))]
    (doseq [item tx]
      @(d/transact conn (list item)))))



;; :tick/time format from sample CSV format
;; Expects `08/25/2014 0930` format
(defn format-time
  [d t]
  (tf/parse (tf/formatter "MM/dd/yyyy hhmm") (str d " " t)))

;; Performs single datomic transaction
(defn datomic-transaction
  [tx]
  (let [conn (d/connect uri)]
    @(d/transact conn (list tx))))


;; Load CSVs data from file into datomic
(defn data-load
  [file]
  (let [data (csv/parse-csv (slurp file))
        names (first data)
        records (rest data)]
    (doseq [r records]
      (println r))
    )
  )



(defn -main
  ([] (-main "etc/dadadata.edn"))
  ([config-file]
   (def config (edn/read-string (slurp config-file)))
   (get-in config [:datomic :uri])
   ))
