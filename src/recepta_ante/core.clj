(ns recepta-ante.core
  (:require
    [next.jdbc.connection :as connection]
    [cambium.core :as log]
    [recepta-ante.conf :as conf]
    [recepta-ante.services.csv-service :as csvservice]
    [recepta-ante.model.sdo :as sdo-dao]
    [recepta-ante.model.reparto :as reparto-dao])
 (:import
   (com.zaxxer.hikari HikariDataSource)))

;;(def dbspec-ante (:recepta-ante-db conf/configuration))
;;(defonce
;;  ^HikariDataSource ds-ante (connection/->pool HikariDataSource dbspec-ante)))

(defn -main
  [& args]
  (let [ dbspec-ante (:recepta-ante-db conf/configuration)
         ^HikariDataSource ds-ante (connection/->pool HikariDataSource dbspec-ante)]
    (log/info "Application started")
    (log/info {:args (vec args) :argc (count args)} "Arguments received")
    (let
      [ente-id (Integer/parseInt (first args))
       sdo-list (csvservice/parse-data ente-id)
       sdo-to-insert (filter #(sdo-dao/exists ds-ante %) sdo-list)]
      (log/info (str "Sdo da inserire: " (count sdo-to-insert)))
      (doseq [sdo sdo-to-insert] (sdo-dao/insert! ds-ante sdo)))))
