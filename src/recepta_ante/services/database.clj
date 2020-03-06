(ns recepta-ante.services.database
  (:require
    [clojure.string :as s]
    [honeysql.helpers :as hh]
    [honeysql.core :as honey]
    [honeysql.format :as fmt]
    [next.jdbc :as jdbc]
    [next.jdbc.prepare :as prepare]
    [next.jdbc.result-set :as result-set]
    [next.jdbc.date-time :as date-time]
    [cambium.core :as log]
    [recepta-ante.utils :as myutils]))

(defn execute!
  "Execute query (select/insert/update/delete) using the map in `sqlmap` provided by honeysql"
  [datasource sqlmap]
  (let [q (honey/format sqlmap)]
    (try
      (jdbc/execute! datasource q)
      (catch Exception e
        (log/error (myutils/stacktrace e))
        (throw (ex-info "Exception in execute!" {:sqlmap sqlmap :query q}))))))

(defn snake-case->kebab-case
    [column]
    (when (keyword? column)
        (keyword (s/replace (name column) #"_" "-"))))

(defn format-output-keywords
  "Convert `output` keywords from snake_case to kebab-case."
  [output]
  (reduce-kv (fn [m k v]
               (assoc m (snake-case->kebab-case k) v))
             {}
             output))

(defn query!
    "Run a SELECT query using the map in sqlmap and format output keywords."
    [datasource sqlmap]
    (->> (execute! datasource sqlmap)
         (map format-output-keywords)))
