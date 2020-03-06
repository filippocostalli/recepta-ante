(defproject recepta-ante "0.1.0-SNAPSHOT"
  :description "Recepta ANTE BATCH - Clojure Version"
  :url "https://www.recepta.marnonet.it/ante"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [seancorfield/next.jdbc "1.0.13"]
                 [clojure.java-time "0.3.2"]
                 [ultra-csv "0.2.3"]
                 [cprop "0.1.16"]
                 [honeysql "0.9.8"]
                 [org.postgresql/postgresql "42.2.9"]
                 [com.zaxxer/HikariCP "3.4.2"]
                 [cambium/cambium.core "0.9.3"]
                 [cambium/cambium.codec-simple "0.9.3"]
                 [cambium/cambium.logback.core "0.4.3"]]
  :main recepta-ante.core
  :repl-options {:init-ns recepta-ante.core})
