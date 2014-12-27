(ns dadadata.core
  (:gen-class)
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]
            [langohr.basic :as lb]
            [taoensso.timbre :as log]
            [clojure.tools.reader.edn :as edn]))


(defn make-publisher [connection exchange routing-key]
  (let [channel (lch/open connection)]
    (fn [message]
      (if (lch/open? channel)
        (do
          (lb/publish channel exchange routing-key message))
        (log/error "channel is closed")))))

(defn make-consumer [connection queue]
  (let [channel (lch/open connection)]
    (fn []
      (if (lch/open? channel)
        (do
          (lb/get channel queue))
        (log/error "channel is closed")))))

(defn publish [amqp-config message]
  (let [connection (rmq/connect)
        publisher (make-publisher connection (amqp-config :exchange) (amqp-config :routing-key))]
    (publisher message)))

(defn consume [queue]
  (let [connection (rmq/connect)
        consumer (make-consumer connection queue)]
    (consumer)))

(defn -main
  ([] (-main "etc/dadadata.edn"))
  ([config-file]
   (def config (edn/read-string (slurp config-file)))
   (publish (config :amqp) "aaaaaaaaaaaa")
   (consume ((config :amqp) :queue))))

(-main)




;(defn -main [config-file]
;  (let [config (edn/read-string (slurp config-file))
;        system (component/start (new-system config))]
;    (.addShutdownHook (Runtime/getRuntime)
;                      (Thread. #(component/stop system)))))

