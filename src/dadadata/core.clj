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

(defn -main
  ([] (-main "etc/dadadata.edn"))
  ([config-file]
    (def config (edn/read-string (slurp config-file)))
    (let [connection (rmq/connect)
          publisher (make-publisher connection ((config :ampq) :exchange) ((config :ampq) :routing-key))]
      (println (publisher "dflkdf"))
      (publisher "abcd")
      (println (class publisher)))))
;(defn -main [config-file]
;  (let [config (edn/read-string (slurp config-file))
;        system (component/start (new-system config))]
;    (.addShutdownHook (Runtime/getRuntime)
;                      (Thread. #(component/stop system)))))

