(ns dadadata.core
  (:gen-class)
  (:require [taoensso.timbre :as log]
            [clojure.tools.reader.edn :as edn]
            [clojure-csv.core :as csv]
            [datomic.api :as d]
            ))

;; (defn make-publisher [connection exchange routing-key]
;;   (let [channel (lch/open connection)]
;;     (fn [message]
;;       (if (lch/open? channel)
;;         (do
;;           (lb/publish channel exchange routing-key message))
;;         (log/error "channel is closed")))))

;; (defn make-consumer [connection queue]
;;   (let [channel (lch/open connection)]
;;     (fn []
;;       (if (lch/open? channel)
;;         (do
;;           (lb/get channel queue))
;;         (log/error "channel is closed")))))

;; (defn publish [amqp-config message]
;;   (let [connection (rmq/connect)
;;         publisher (make-publisher connection (amqp-config :exchange) (amqp-config :routing-key))]
;;     (publisher message)))

;; (defn consume [queue]
;;   (let [connection (rmq/connect)
;;         consumer (make-consumer connection queue)]
;;     (consumer)))



;; Create database and load schema
(defn datomic-init
  [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)
        schema-tx (read-string (slurp "etc/datomic-schema.edn"))]
    @(d/transact conn schema-tx)
  )
)



(defn -main
  ([] (-main "etc/dadadata.edn"))
  ([config-file]
   (def config (edn/read-string (slurp config-file)))
   (get-in config [:datomic :uri])
))
