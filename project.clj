(defproject dadadata "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :repositories [["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username [:env/my_datomic_username]
                                    :password [:env/my_datomic_password]}]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.stuartsierra/component "0.2.2"]
                 [clj-time "0.9.0"]
                 [com.datomic/datomic-pro "0.9.5067" :exclusions [joda-time]]
                 [com.novemberain/langohr "3.0.1"]]
  :main dadadata.core)
